package it.polimi.sw.GC50.net.client;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.ServerInterface;
import it.polimi.sw.GC50.net.messages.*;
import it.polimi.sw.GC50.net.requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.requests.PlaceCardRequest;
import it.polimi.sw.GC50.net.socket.ClientSCK;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * class that implements client
 */
public class Client {
    private final View view;
    private ServerInterface serverInterface;
    private GameView gameView;
    private int exit;

    /**
     * Constructs an instance of client
     *
     * @param view type of view
     */
    public Client(View view) {
        this.view = view;
        view.setClient(this);

        serverInterface = null;
        gameView = null;
        exit = 0;
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that starts connection with the server
     */
    public synchronized int run() {
        try {
            if (connect()) {
                lobby();
            }
        } catch (GameException e) {
            view.showError(e.getMessage());
            exit = 1;
        }

        view.showEndSession();
        return exit;
    }

    /**
     * method used to connect a client
     *
     * @return true if client is connected
     * @throws GameException if there is an error
     */
    private boolean connect() throws GameException {
        String serverIp = view.selectServerIp();

        boolean connected = false;
        while (!connected) {
            switch (view.selectConnectionType()) {
                case 1 -> {
                    serverInterface = new ClientSCK(this, serverIp, AppClient.serverSckPort);
                }

                case 2 -> {
                    try {
                        serverInterface = new ClientRmi(this, serverIp, AppClient.serverRmiPort);
                    } catch (RemoteException e) {
                        serverInterface = null;
                    }
                }

                case 3 -> {
                    return false;
                }
            }
            if (serverInterface != null) {
                connected = serverInterface.connect();
            }

            if (!connected) {
                view.showError("Failure in connecting to the server");
            }
        }
        return true;
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method used to connect with a lobby
     *
     * @throws GameException if there is an error
     */
    private void lobby() throws GameException {

        while (!setPlayer(view.selectName())) {
            view.showError("Player nickname not available");
        }

        while (true) {
            switch (view.selectJoinOrCreate()) {
                case 1 -> {
                    gameView.setInGame(createGame(view.selectGameName(), view.selectNumberOfPlayers(), view.selectEndScore()));
                }

                case 2 -> {
                    Map<String, List<String>> freeGames = getFreeGames();
                    view.showFreeGames(freeGames);
                    if (!freeGames.isEmpty()) {
                        gameView.setInGame(joinGame(view.selectJoinGameName()));
                    }
                }

                case 3 -> {
                    resetPlayer();
                    return;
                }
            }
            if (gameView.isInGame()) {
                listenCommands();
                waitingPhase();
            } else {
                view.showError("Game not available");
            }
        }
    }

    /**
     * method used to set a player
     *
     * @param nickname player's nickname
     * @return a boolean true if player is set correctly
     * @throws GameException if there is an error
     */
    public boolean setPlayer(String nickname) throws GameException {
        nickname = serverInterface.setPlayer(nickname);
        if (nickname != null) {
            gameView = new GameView(nickname);
            return true;
        }
        return false;
    }

    /**
     * method used to reset a player
     *
     * @throws GameException if there is an error
     */
    private void resetPlayer() throws GameException {
        serverInterface.resetPlayer();
    }

    /**
     * method used to create a new game
     *
     * @param gameId     id of the game
     * @param numPlayers number of players
     * @param endScore   final score
     * @return a boolean with a new game
     * @throws GameException if there is an error
     */
    private boolean createGame(String gameId, int numPlayers, int endScore) throws GameException {
        return serverInterface.createGame(gameId, numPlayers, endScore);
    }

    /**
     * method used to join a game
     *
     * @param gameId id of the game
     * @return game we joint
     * @throws GameException if there is an error
     */
    private boolean joinGame(String gameId) throws GameException {
        return serverInterface.joinGame(gameId);
    }

    /**
     * @return a map of free games
     * @throws GameException if there is an error
     */
    private Map<String, List<String>> getFreeGames() throws GameException {
        return serverInterface.getFreeGames();
    }

    // VIEW LISTENER ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method used for listenCommands
     */
    private void listenCommands() {
        new Thread(() -> {
            while (gameView.isInGame()) {
                view.listen();
            }
        }).start();
    }

    /**
     * method used to add a new command
     *
     * @param command type of command
     * @param args    args of command
     */
    public void addCommand(Command command, String[] args) {
        if (command.equals(Command.LEAVE)) {
            gameView.clear();
        }

        new Thread(() -> {
            synchronized (this) {
                try {
                    switchCommand(command, args);
                } catch (GameException e) {
                    view.showError(e.getMessage());
                    gameView.clear();
                    exit = 1;
                }
                notifyAll();
            }
        }).start();
    }

    /**
     * method used to switch command
     *
     * @param command type of command
     * @param args    args of command
     * @throws GameException if there is an error
     */
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

    /**
     * method that puts game in a waiting phase
     *
     * @throws GameException if there is an error
     */
    private void waitingPhase() throws GameException {
        view.showWaitPlayers();

        while ((gameView.getGameStatus().equals(GameStatus.WAITING) || !gameView.allJoined())
                && gameView.isInGame()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }

        if (gameView.isInGame()) {
            setupPhase();
        }
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method used for setup phase
     *
     * @throws GameException if player name is not valid
     */
    private void setupPhase() throws GameException {
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
            playingPhase();
        }
    }

    /**
     * method used to select secret objective
     *
     * @param index index of the card
     * @throws GameException if there is an error
     */
    private void selectSecretObjective(int index) throws GameException {
        serverInterface.selectSecretObjective(index);
    }

    /**
     * method used to select starter face
     *
     * @param face back or front face
     * @throws GameException if there is an error
     */
    private void selectStarterFace(int face) throws GameException {
        serverInterface.selectStarterFace(face);
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method used for playing phase
     *
     * @throws GameException if there is an error
     */
    private void playingPhase() throws GameException {
        view.showStart();

        while (gameView.getGameStatus().equals(GameStatus.PLAYING) && gameView.isInGame()) {
            playTurn();
        }

        if (gameView.isInGame()) {
            endPhase();
        }
    }

    /**
     * method used when there is a play turn
     *
     * @throws GameException if there is an error
     */
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

    /**
     * method used to place a card
     *
     * @param placeCardRequest request to place a card
     * @throws GameException if there is an error
     */
    private void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        serverInterface.placeCard(placeCardRequest);
    }

    /**
     * method used to draw a card
     *
     * @param position where the card is placed
     * @throws GameException if there is an error
     */
    private void drawCard(int position) throws GameException {
        serverInterface.drawCard(position);
    }

    /**
     * method used to send a chat message
     *
     * @param message that is sent
     * @throws GameException if there is an error
     */
    private void sendChatMessage(String message) throws GameException {
        serverInterface.sendChatMessage(new ChatMessageRequest(message));
    }

    /**
     * method used to send a private chat message
     *
     * @param receiver nickname of receiver
     * @param message  message that is sent
     * @throws GameException if there is an error
     */
    private void sendPrivateChatMessage(String receiver, String message) throws GameException {
        serverInterface.sendChatMessage(new ChatMessageRequest(receiver, message));
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method used to end connection
     *
     * @throws GameException if there is an error
     */
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

    /**
     * method used to leave a game
     *
     * @throws GameException if there is an error
     */
    private void leaveGame() throws GameException {
        serverInterface.leaveGame();
    }

    // OBSERVER ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method used to update a message
     *
     * @param notify  notify of update
     * @param message message of update
     */
    public void update(Notify notify, Message message) {
        new Thread(() -> {
            synchronized (this) {
                switchNotify(notify, message);
                notifyAll();
            }
        }).start();
    }

    /**
     * method used to switch a request
     *
     * @param notify  of switch
     * @param message of switch
     */
    private void switchNotify(Notify notify, Message message) {
//       System.err.println("> Update from server: " + notify);
        switch (notify) {
            case NOTIFY_PLAYER_JOINED_GAME -> {
                PlayerJoinedMex playerJoinedMex = (PlayerJoinedMex) message;
                gameView.setPlayersLeft(playerJoinedMex.getPlayersLeft());
                gameView.setPlayerArea(playerJoinedMex.getNickname(), new CardsMatrix(1),
                        0, 0, false);

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
            }
        }
    }

    /**
     * @return game view
     */
    public GameView getGameView() {
        return gameView;
    }

    /*
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
        if (view.getClass().getSimpleName().equals("GuiView")) {
            ((GuiView) view).serverError = false;
        }
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
        if (view.getClass().getSimpleName().equals("GuiView")) {
            ((GuiView) view).serverError = false;
        }
    }

            case NOTIFY_NEXT_TURN -> {
        gameView.setPlayingPhase(PlayingPhase.PLACING);
        gameView.setCurrentPlayer(((PlayerMex) message).getNickname());
        if (view.getClass().getSimpleName().equals("GuiView")) {
            ((GuiView) view).playerAreaUpdated = false;
            ((GuiView) view).playerHandUpdated = false;
        }
    }

            case NOTIFY_ERROR -> {
        ErrorMex errorMex = (ErrorMex) message;
        if (gameView.getNickname().equals(errorMex.getNickname())) {
            view.showError(errorMex.getContent());
        }
        if (view.getClass().getSimpleName().equals("GuiView")) {
            ((GuiView) view).serverError = true;
        }
    }

     */
}
