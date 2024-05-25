package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.Messages.*;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.socket.ClientSCK;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.ViewType;
import it.polimi.sw.GC50.view.View;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class Client {
    private final ServerInterface serverInterface;
    private final View view;
    private GameView gameView;

    public Client(String serverIp, String serverPort, ConnectionType connection, View view) throws RemoteException {
        switch (connection) {
            case RMI -> {
                this.serverInterface = new ClientRmi(this, serverIp, serverPort);
            }
            case SOCKET -> {
                this.serverInterface = new ClientSCK(this, serverIp, serverPort);
            }
            default -> {
                this.serverInterface = null;
            }
        }
        this.view = view;
        view.setClient(this);
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized void start() {
        try {
            connect();
            lobby();
        } catch (GameException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void connect() throws GameException {
        serverInterface.connect();
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void lobby() throws GameException, InterruptedException {
        while (!setPlayer(view.selectName())) {
            view.showError("Player name not valid");
        }

        while (true) {
            boolean inGame = false;
            switch (view.selectJoinOrCreate()) {
                case 1 -> {
                    if (AppClient.getViewType().equals(ViewType.GUI)) {
                        ((GuiView) view).waitGameParams();
                    }
                    inGame = createGame(view.selectGameName(), view.selectNumberOfPlayers(), view.selectEndScore());
                }

                case 2 -> {
                    Map<String, List<String>> freeGames = getFreeGames();
                    view.showFreeGames(freeGames);
                    if (!freeGames.isEmpty()) {
                        if (AppClient.getViewType().equals(ViewType.GUI)) {
                            inGame = joinGame(((GuiView) view).selectedJoinGame());
                        } else {
                            inGame = joinGame(view.selectGameName());
                        }
                    }
                }

                case 3 -> {
                    resetPlayer();
                    view.showEndSession();
                    return;
                }
            }
            if (inGame) {
                gameView.clear();
                listenCommands();
                waitingPhase();
            } else {
                view.showError("Game name not valid");
            }
        }
    }

    private boolean setPlayer(String nickname) throws GameException {
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
            while (!gameView.getGameStatus().equals(GameStatus.ENDED)) {
                view.listen();
            }
        }).start();
    }

    public void addCommand(Command command, String[] args) {
        new Thread(() -> {
            synchronized (this) {
                try {
                    switchCommand(command, args);
                } catch (GameException e) {
                    throw new RuntimeException(e);
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
        view.showWaitPlayers();

        while (gameView.getGameStatus().equals(GameStatus.WAITING)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }
        setupPhase();
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setupPhase() throws GameException {
        view.showSetup();

        while (gameView.getGameStatus().equals(GameStatus.SETUP)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }
        playingPhase();
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

        while (gameView.getGameStatus().equals(GameStatus.PLAYING)) {
            playTurn();
        }
        endPhase();
    }

    private void playTurn() throws GameException {
        view.showCurrentPlayer();

        if (gameView.getNickname().equals(gameView.getCurrentPlayer())) {
            view.showPlacingPhase();
        }

        while (gameView.getPlayingPhase().equals(PlayingPhase.PLACING)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }

        if (gameView.getNickname().equals(gameView.getCurrentPlayer())) {
            view.showDrawingPhase();
        }

        while (gameView.getPlayingPhase().equals(PlayingPhase.DRAWING)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
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
        serverInterface.sendChatMessage(message);
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void endPhase() {
        view.showEnd();
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
        System.err.println("> Update from server: " + notify);
        switch (notify) {
            case NOTIFY_PLAYER_JOINED_GAME -> {
                String player = ((PlayerMex) message).getNickname();
                gameView.setPlayerArea(player, null, 0, 0);

                view.showPlayerJoined(player);
            }

            case NOTIFY_PLAYER_LEFT_GAME -> {
                String player = ((PlayerMex) message).getNickname();
                gameView.removePlayerArea(player);

                view.showPlayerLeft(player);
            }

            case NOTIFY_GAME_SETUP -> {
                SetupMex setupMex = (SetupMex) message;
                gameView.setGameStatus(GameStatus.SETUP);
                gameView.setCommonObjectives(setupMex.getCommonObjectives());
                gameView.setDecks(setupMex.getDecks());
                gameView.setHand(setupMex.getHand(gameView.getNickname()));
                gameView.setSecreteObjectivesList(setupMex.getSecretObjectivesMap(gameView.getNickname()));
                gameView.setStarterCard(setupMex.getStarterCard(gameView.getNickname()));
            }

            case NOTIFY_PLAYER_READY -> {
                PlayerReadyMex playerReadyMex = (PlayerReadyMex) message;
                String player = playerReadyMex.getNickname();
                if (gameView.getNickname().equals(playerReadyMex.getNickname())) {
                    gameView.setSecretObjective(playerReadyMex.getSecretObjective());
                }
                gameView.setPlayerArea(player, playerReadyMex.getCardsMatrix(),
                        playerReadyMex.getTotalScore(), 0);

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
                        boardUpdateMex.getTotalScore(), 0);
                if (gameView.getNickname().equals(boardUpdateMex.getNickname())) {
                    gameView.setHand((boardUpdateMex.getHand()));
                }

                view.showCardsArea(player);
                view.showScores();
            }

            case NOTIFY_CARD_DRAWN -> {
                DecksUpdateMex decksUpdateMex = (DecksUpdateMex) message;
                gameView.setDecks(decksUpdateMex.getDecks());
                if (gameView.getNickname().equals(decksUpdateMex.getNickname())) {
                    gameView.setHand((decksUpdateMex.getHand()));
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
                            endMex.getTotalScore(player), endMex.getObjectivesScore(player));
                }
            }

            case NOTIFY_CHAT_MESSAGE -> {
                ChatMex chatMex = (ChatMex) message;
                view.showMessage(chatMex.getChatMessage().getSender().getNickname() + " : " + chatMex.getChatMessage().getContent());
            }

            case NOTIFY_ERROR -> {
                ErrorMex errorMex = (ErrorMex) message;
                if (gameView.getNickname().equals(errorMex.getNickname())) {
                    view.showError(errorMex.getContent());
                }
            }

            default -> {
            }
        }
    }

    public GameView getGameView() {
        return gameView;
    }

    // FUFFA ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    public Object getModel() {
        try {
            modelMex = (ModelMex) gameController.getModel(this);
        } catch (RemoteException e) {
            return null;
        }
        view.addModel(modelMex);
        myTurn = modelMex.getCurrentPlayer().equals(nickname);
        return modelMex;
    }


    @Override
    public void onUpdate(Message mex) throws RemoteException {
        thread2 = new Thread(() -> {
            synchronized (this) {
                switchMex(mex);
                notifyAll();
            }
        });
        thread2.start();
    }


    private void switchMex(Message mex) {
        switch (mex.getRequest()) {
            case NOTIFY_PLAYER_JOINED_GAME -> {
                view.printMessage("Giocatore " + mex.getObject() + " è entrato in partita");
            }

            case NOTIFY_PLAYER_LEFT_GAME -> {
                view.printMessage("Giocatore " + mex.getObject() + " ha abbandonato la partita");
            }

            case NOTIFY_GAME_SETUP -> {

            }

            case NOTIFY_PLAYER_READY -> {
                view.printMessage("Giocatore " + mex.getObject() + " pronto");
            }

            case NOTIFY_CARD_PLACED, NOTIFY_NEXT_TURN, NOTIFY_CARD_DRAWN -> {
                notify = true;
                error = false;
                notifyAll();
                /*
                synchronized (lock2) {
                    while (thread2pause) {
                        try {
                            lock2.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
                synchronized (lock) {
                    notify = true;
                    error = false;
                    lock.notifyAll();
                }


            }

            case NOTIFY_GAME_STARTED -> {
                gameStatus = GameStatus.PLAYING;

                System.out.println("Partita iniziata");
                notifyAll();
            }

            case NOTIFY_CHAT_MESSAGE -> {
                notifyUpdateChat = true;
            }

            case NOTIFY_CARD_NOT_FOUND, NOTIFY_DRAWING_POSITION_NOT_AVAILABLE, NOTIFY_INVALID_INDEX,
                    NOTIFY_OPERATION_NOT_AVAILABLE, NOTIFY_NOT_YOUR_PLACING_PHASE, NOTIFY_CARD_NOT_PLACEABLE -> {
                if (mex.getObject().equals(nickname)) {

                    synchronized (lock2) {
                        while (thread2pause) {
                            try {
                                lock2.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }

                    synchronized (lock) {
                        error = true;
                        notify = true;
                        lock.notifyAll();
                    }



                    error = true;
                    notify = true;
                }
            }

            case GET_CHAT_MODEL_RESPONSE -> {
                System.out.println("chat");
            }

            default -> {
            }

        }
        //thread2.interrupt();
    }

    private void waitNotifyFromServer() {
        synchronized (lock2) {
            thread2pause = false;
            lock2.notifyAll();
        }
        synchronized (lock) {
            while (!notify) {
                try {
                    lock.wait();

                } catch (InterruptedException e) {

                }

            }
        }
        System.out.println("notify from server");
        if (!error) {
            getModel();
        }
        error = false;
        notify = false;
        thread2pause = true;
    }


    private void waitMyTurn() {
        while (!myTurn) {
            waitNotifyFromServer();
            if (!error) {
                getModel();
            }
            error = false;
        }
    }

     */
}