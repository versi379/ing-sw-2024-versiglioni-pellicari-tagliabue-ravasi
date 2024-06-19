package it.polimi.sw.GC50.net.client;

import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.messages.*;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.requests.PlaceCardRequest;
import it.polimi.sw.GC50.net.socket.ClientSCK;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.net.ConnectionType;
import it.polimi.sw.GC50.net.ServerInterface;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class Client {
    private final ServerInterface serverInterface;
    private final View view;
    private GameView gameView;

    public Client(String serverIp, String serverPort, ConnectionType connection, View view) {
        ServerInterface serverInterface;
        switch (connection) {
            case RMI -> {
                try {
                    serverInterface = new ClientRmi(this, serverIp, serverPort);
                } catch (RemoteException e) {
                    serverInterface = null;
                }
            }
            case SOCKET -> {
                try {
                    serverInterface = new ClientSCK(this, serverIp, serverPort);
                } catch (IOException e) {
                    serverInterface = null;
                }
            }
            default -> {
                serverInterface = null;
            }
        }
        this.serverInterface = serverInterface;
        this.view = view;
        view.setClient(this);
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized void start() {
        try {
            connect();
            lobby();
        } catch (GameException e) {
            System.err.println("> " + e.getMessage());
        }
    }

    private void connect() throws GameException {
        serverInterface.connect();
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void lobby() throws GameException {

        while (!setPlayer(view.selectName())) {
            view.showError("Player name not valid");
        }

        while (true) {
            switch (view.selectJoinOrCreate()) {
                case 1 -> {
                    if (view.getClass().getSimpleName().equals("GuiView")) {
                        ((GuiView) view).waitGameParams();
                    }
                    gameView.setInGame(createGame(view.selectGameName(), view.selectNumberOfPlayers(), view.selectEndScore()));
                }

                case 2 -> {
                    Map<String, List<String>> freeGames = getFreeGames();
                    view.showFreeGames(freeGames);
                    if (!freeGames.isEmpty()) {
                        if (view.getClass().getSimpleName().equals("GuiView")) {
                            gameView.setInGame(joinGame(((GuiView) view).selectedJoinGame()));
                        } else {
                            gameView.setInGame(joinGame(view.selectGameName()));
                        }
                    }
                }

                case 3 -> {
                    resetPlayer();
                    view.showEndSession();
                    return;
                }
            }
            if (gameView.isInGame()) {
                listenCommands();
                waitingPhase();
            } else {
                view.showError("Invalid choice");
            }
        }
    }

    public boolean setPlayer(String nickname) throws GameException {
        nickname = serverInterface.setPlayer(nickname);
        if (nickname != null) {
            gameView = new GameView(nickname);
            return true;
        }
        return false;
    }

    private void resetPlayer() throws GameException {
        serverInterface.resetPlayer();
    }

    private boolean createGame(String gameId, int numPlayers, int endScore) throws GameException {
        return serverInterface.createGame(gameId, numPlayers, endScore);
    }

    private boolean joinGame(String gameId) throws GameException {
        return serverInterface.joinGame(gameId);
    }

    private Map<String, List<String>> getFreeGames() throws GameException {
        return serverInterface.getFreeGames();
    }

    // VIEW LISTENER ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void listenCommands() {
        new Thread(() -> {
            while (gameView.isInGame()) {
                view.listen();
            }
        }).start();
    }

    public void addCommand(Command command, String[] args) {
        if (command.equals(Command.LEAVE)) {
            gameView.clear();
        }

        new Thread(() -> {
            synchronized (this) {
                try {
                    switchCommand(command, args);
                } catch (GameException e) {
                    System.err.println("> " + e.getMessage());
                }
                notifyAll();
            }
        }).start();
    }

    private void switchCommand(Command command, String[] args) throws GameException {
        switch (command) {
            case CHOOSE_OBJECTIVE -> {
                selectSecretObjective(Integer.parseInt(args[0]));
            }
            case CHOOSE_STARTER_FACE -> {
                selectStarterFace(Integer.parseInt(args[0]));
            }
            case SHOW_OBJECTIVES -> {
                view.showObjectives();
            }
            case PLACE_CARD -> {
                placeCard(new PlaceCardRequest(Integer.parseInt(args[0]),
                        Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]),
                        Integer.parseInt(args[3])));
            }
            case DRAW_CARD -> {
                drawCard(Integer.parseInt(args[0]));
            }
            case CHAT -> {
                sendChatMessage(args[0]);
            }
            case CHAT_PRIVATE -> {
                sendPrivateChatMessage(args[0], args[1]);
            }
            case LEAVE -> {
                leaveGame();
            }
            case HELP -> {
                view.showHelp();
            }
            case NOT_A_COMMAND -> {
                view.showError(args[0]);
            }
        }
    }

    // WAITING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void waitingPhase() throws GameException {

//        System.err.println("> waiting phase entered");

        if (!view.getClass().getSimpleName().equals("GuiView")) {
            view.showWaitPlayers();
        }

        while ((gameView.getGameStatus().equals(GameStatus.WAITING) || !gameView.allJoined())
                && gameView.isInGame()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }

        if (gameView.isInGame()) {

            if (view.getClass().getSimpleName().equals("GuiView")) {
                Platform.runLater(() -> {
                    Stage stage = ((GuiView) view).getPrimaryStage();
                    FXMLLoader gameLoader = new FXMLLoader(getClass().getResource(ScenePath.SETUPGAME.getPath()));
                    Parent gameRoot = null;
                    try {
                        gameRoot = gameLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Scene gameScene = new Scene(gameRoot);
                    gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
                    stage.setScene(gameScene);
                });
            }

//            System.err.println("> setup completed -> min num of players reached!");
            setupPhase();
        }
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setupPhase() throws GameException {

//        System.err.println("> setup phase entered");

        view.showSetup();

        while ((gameView.getGameStatus().equals(GameStatus.SETUP) || !gameView.allReady())
                && gameView.isInGame()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }

        if (gameView.isInGame()) {

//            System.err.println("> setup finished");

            playingPhase();
        }
    }

    private void selectSecretObjective(int index) throws GameException {
        serverInterface.selectSecretObjective(index);
    }

    private void selectStarterFace(int face) throws GameException {
        serverInterface.selectStarterFace(face);
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void playingPhase() throws GameException {
        view.showStart();

        if (view.getClass().getSimpleName().equals("GuiView")) {
            if (gameView.getNickname().equals(gameView.getCurrentPlayer())) {
//                view.showHand();
            }
            view.showHand();
            view.showCardsArea(getGameView().getCurrentPlayer());
            Platform.runLater(() -> {
                Stage stage = ((GuiView) view).getPrimaryStage();
                FXMLLoader gameLoader = new FXMLLoader(getClass().getResource(ScenePath.PLAYGAME.getPath()));
                Parent gameRoot = null;
                try {
                    gameRoot = gameLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene gameScene = new Scene(gameRoot);
                gameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
                stage.setScene(gameScene);
            });
        }

        while (gameView.getGameStatus().equals(GameStatus.PLAYING) && gameView.isInGame()) {
            playTurn();
        }

        if (gameView.isInGame()) {
            endPhase();
        }
    }

    private void playTurn() throws GameException {
        gameView.setTurnEnded(false);
        view.showCurrentPlayer();

        if (gameView.getNickname().equals(gameView.getCurrentPlayer())) {
            view.showPlacingPhase();
        }

        while (gameView.getPlayingPhase().equals(PlayingPhase.PLACING) && gameView.isInGame()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }

        if (gameView.isInGame()) {

            if (gameView.getNickname().equals(gameView.getCurrentPlayer())) {
                view.showDrawingPhase();
            }

            while ((gameView.getPlayingPhase().equals(PlayingPhase.DRAWING) || !gameView.isTurnEnded())
                    && gameView.isInGame()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new GameException("Interruption error", e.getCause());
                }
            }
        }
    }

    private void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        serverInterface.placeCard(placeCardRequest);
    }

    private void drawCard(int position) throws GameException {
        serverInterface.drawCard(position);
    }

    private void sendChatMessage(String message) throws GameException {
        serverInterface.sendChatMessage(new ChatMessageRequest(message));
    }

    private void sendPrivateChatMessage(String receiver, String message) throws GameException {
        serverInterface.sendChatMessage(new ChatMessageRequest(receiver, message));
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void endPhase() throws GameException {
        view.showEnd();

        while (gameView.getGameStatus().equals(GameStatus.ENDED)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }
    }

    private void leaveGame() throws GameException {
        serverInterface.leaveGame();
    }

    // OBSERVER ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void update(Notify notify, Message message) {
        new Thread(() -> {
            synchronized (this) {
                switchRequest(notify, message);
                notifyAll();
            }
        }).start();
    }

    private void switchRequest(Notify notify, Message message) {
//       System.err.println("> Update from server: " + notify);
        switch (notify) {
            case NOTIFY_PLAYER_JOINED_GAME -> {
                PlayerJoinedMex playerJoinedMex = (PlayerJoinedMex) message;
                gameView.setPlayersLeft(playerJoinedMex.getPlayersLeft());

                view.showPlayerJoined(playerJoinedMex.getNickname());
            }

            case NOTIFY_PLAYER_LEFT_GAME -> {
                String player = ((PlayerMex) message).getNickname();
                gameView.removePlayerArea(player);

                if (!getGameView().getNickname().equals(player)) {
                    view.showPlayerLeft(player);
                }
            }

            case NOTIFY_GAME_SETUP -> {
                SetupMex setupMex = (SetupMex) message;
                gameView.setGameStatus(GameStatus.SETUP);
                gameView.setCommonObjectives(setupMex.getCommonObjectives());
                gameView.setDecks(setupMex.getDecks());
                gameView.setHand(setupMex.getHand(gameView.getNickname()));
                gameView.setSecreteObjectivesSelection(setupMex.getSecretObjectivesMap(gameView.getNickname()));
                gameView.setStarterCard(setupMex.getStarterCard(gameView.getNickname()));
            }

            case NOTIFY_PLAYER_READY -> {
                PlayerReadyMex playerReadyMex = (PlayerReadyMex) message;
                String player = playerReadyMex.getNickname();
                if (gameView.getNickname().equals(playerReadyMex.getNickname())) {
                    gameView.setSecretObjective(playerReadyMex.getSecretObjective());
                }
                gameView.setPlayerArea(player, playerReadyMex.getCardsMatrix(),
                        playerReadyMex.getTotalScore(), 0, true);

                view.showPlayerReady(player);
            }

            case NOTIFY_GAME_STARTED -> {
                gameView.setGameStatus(GameStatus.PLAYING);
                gameView.setPlayingPhase(PlayingPhase.PLACING);
                gameView.setCurrentPlayer(((PlayerMex) message).getNickname());
            }

            case NOTIFY_CARD_PLACED -> {
                BoardUpdateMex boardUpdateMex = (BoardUpdateMex) message;
                String player = boardUpdateMex.getNickname();
                gameView.setPlayingPhase(PlayingPhase.DRAWING);

                gameView.setPlayerArea(player, boardUpdateMex.getCardsMatrix(),
                        boardUpdateMex.getTotalScore(), 0, true);
                if (gameView.getNickname().equals(boardUpdateMex.getNickname())) {
                    gameView.setHand((boardUpdateMex.getHand()));
                }

                view.showCardsArea(player);
                view.showScores();
            }

            case NOTIFY_CARD_DRAWN -> {
                DecksUpdateMex decksUpdateMex = (DecksUpdateMex) message;
                gameView.setTurnEnded(true);
                gameView.setDecks(decksUpdateMex.getDecks());
                if (gameView.getNickname().equals(decksUpdateMex.getNickname())) {
                    gameView.setHand((decksUpdateMex.getHand()));
                }

                if (gameView.getNickname().equals(decksUpdateMex.getNickname())) {
                    view.showHand();
                }
                view.showDecks();
            }

            case NOTIFY_NEXT_TURN -> {
                gameView.setPlayingPhase(PlayingPhase.PLACING);
                gameView.setCurrentPlayer(((PlayerMex) message).getNickname());
            }

            case NOTIFY_GAME_ENDED -> {
                EndMex endMex = (EndMex) message;
                gameView.setGameStatus(GameStatus.ENDED);
                gameView.setPlayingPhase(PlayingPhase.PLACING);
                gameView.setWinnerList((endMex).getWinnerList());
                for (String player : gameView.getPlayerList()) {
                    gameView.setPlayerArea(player, gameView.getPlayerArea(player).getCardsMatrix(),
                            endMex.getTotalScore(player), endMex.getObjectivesScore(player), true);
                }
            }

            case NOTIFY_CHAT_MESSAGE -> {
                ChatMex chatMex = (ChatMex) message;
                if (chatMex.getReceiver() == null ||
                        gameView.getNickname().equals(chatMex.getSender()) ||
                        gameView.getNickname().equals(chatMex.getReceiver())) {
                    view.showChatMessage(chatMex.getSender(), chatMex.getContent(), chatMex.getTime());
                }
            }

            case NOTIFY_ERROR -> {
                ErrorMex errorMex = (ErrorMex) message;
                if (gameView.getNickname().equals(errorMex.getNickname())) {
                    view.showError(errorMex.getContent());
                }
                //((GuiView) view).serverError = true;
            }

            default -> {
            }
        }
    }

    public GameView getGameView() {
        return gameView;
    }
}