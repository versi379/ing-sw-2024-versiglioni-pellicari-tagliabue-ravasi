package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.Messages.*;
import it.polimi.sw.GC50.net.util.*;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.ViewType;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import static it.polimi.sw.GC50.net.util.Request.DRAW_CARD;

public class ClientRmi extends UnicastRemoteObject implements Serializable, Client, ClientInterface {
    private final String serverName;
    private ServerRmiRemote serverRmi;
    private GameControllerRemote gameController;
    private final View view;
    private GameView gameView;

    public ClientRmi(String serverName, View view) throws RemoteException {
        this.serverName = serverName;
        serverRmi = null;
        gameController = null;

        this.view = view;
        view.setClient(this);
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    private void connect() throws GameException {
        try {
            serverRmi = (ServerRmiRemote) Naming.lookup(serverName + "/server");
            serverRmi.addClient(this);
            System.err.println("Connected to server");
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new GameException("Error in connection to the server", e.getCause());
        }
    }

    public synchronized void run() {
        try {
            connect();
            lobby();
        } catch (GameException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void lobby() throws GameException, InterruptedException {
        while (!setPlayer(view.selectName())) {
            view.showError("Player name not valid");
        }

        while (true) {
            switch (view.selectJoinOrCreate()) {
                case 1 -> {
                    createGame(view.selectGameName(), view.selectNumberOfPlayers(), view.selectEndScore());
                }

                case 2 -> {
                    Map<String, List<String>> freeGames = getFreeGames();
                    view.showFreeGames(freeGames);
                    if (!freeGames.isEmpty()) {
                        joinGame(view.selectGameName());
                    }
                }

                case 3 -> {
                    resetPlayer();
                    view.showEndSession();
                    return;
                }
            }
            if (gameController != null) {
                gameView.clear();
                listenCommands();
                waitingPhase();
            } else {
                view.showError("Game name not valid");
            }
        }
    }

    private boolean setPlayer(String nickname) throws GameException {
        try {
            nickname = serverRmi.setPlayer(this, nickname);
            if (nickname != null) {
                gameView = new GameView(nickname);
                return true;
            }
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
        return false;
    }

    private void resetPlayer() throws GameException {
        try {
            serverRmi.resetPlayer(this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    private void createGame(String gameId, int numPlayers, int endScore) throws GameException {
        try {
            gameController = serverRmi.createGame(this, gameId, numPlayers, endScore);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    private void joinGame(String gameId) throws GameException {
        try {
            gameController = serverRmi.joinGame(this, gameId);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    private Map<String, List<String>> getFreeGames() throws GameException {
        try {
            return serverRmi.getFreeGames();
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // VIEW LISTENER ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void listenCommands() {
        new Thread(() -> {
            while (!gameView.getGameStatus().equals(GameStatus.ENDED)) {
                view.listen();
            }
        }).start();
    }

    @Override
    public void addCommand(Command command, List<Integer> args) {
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

    private void switchCommand(Command command, List<Integer> args) throws GameException {
        switch (command) {
            case CHOOSE_OBJECTIVE -> {
                selectObjectiveCard(args.getFirst());
            }
            case CHOOSE_STARTER_FACE -> {
                selectStarterFace(args.getFirst());
            }
            case PLACE_CARD -> {
                placeCard(new PlaceCardRequest(args.getFirst(),
                        args.get(1),
                        args.get(2),
                        args.get(3)));
            }
            case DRAW_CARD -> {
                drawCard(args.getFirst());
            }
            case CHAT -> {
                sendMessage(args.toString());
            }
            case HELP -> {
                view.showHelp();
            }
            case NOT_A_COMMAND -> {
                view.showError("Not a command");
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
        view.showHelp();

        while (gameView.getGameStatus().equals(GameStatus.SETUP)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }
        playingPhase();
    }

    private void selectObjectiveCard(int index) throws GameException {
        try {
            gameController.updateController(Request.SELECT_OBJECTIVE_CARD, index, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    private void selectStarterFace(int face) throws GameException {
        try {
            gameController.updateController(Request.SELECT_STARTER_FACE, face, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void playingPhase() throws GameException {
        view.showStart();

        while (gameView.getGameStatus().equals(GameStatus.PLAYING)) {
            while (!gameView.getNickname().equals(gameView.getCurrentPlayer()) &&
                    gameView.getGameStatus().equals(GameStatus.PLAYING)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new GameException("Interruption error", e.getCause());
                }
            }
            if (gameView.getGameStatus().equals(GameStatus.PLAYING)) {
                playTurn();
            }
        }
        endPhase();
    }

    private void playTurn() throws GameException {
        do {
            while (gameView.getPlayingPhase().equals(PlayingPhase.PLACING)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new GameException("Interruption error", e.getCause());
                }
            }
        } while (gameView.getPlayingPhase().equals(PlayingPhase.PLACING));

        do {
            while (gameView.getPlayingPhase().equals(PlayingPhase.DRAWING)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new GameException("Interruption error", e.getCause());
                }
            }
        } while (gameView.getPlayingPhase().equals(PlayingPhase.DRAWING));
    }

    private void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        try {
            gameController.updateController(Request.PLACE_CARD, placeCardRequest, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    private void drawCard(int position) throws GameException {
        try {
            gameController.updateController(DRAW_CARD, position, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    private void sendMessage(String message) throws GameException {
        try {
            gameController.updateChat(message, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void endPhase() {
        view.showEnd();
        gameController = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////
//OBSERVER
///////////////////////////////////////////
    @Override
    public void ping() throws RemoteException {
    }

    @Override
    public void update(Request request, Message message) {
        new Thread(() -> {
            synchronized (this) {
                switchRequest(request, message);
                notifyAll();
            }
        }).start();
    }

    private void switchRequest(Request request, Message message) {
        System.err.println("Update from server: " + request);
        switch (request) {
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

                view.showPlayerArea(player);
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

                view.showCurrentPlayer();
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    @Override
    public String getNickname() {
        return nickname;
    }


    @Override
    public void playerJoined(String nickname) {

        view.playerJoined(String nickname);
    }

    @Override
    public void playerLeft(String nickname);

    @Override
    public void gameSetup();

    @Override
    public void playerReady(String nickname);

    @Override
    public void gameStarted();

    @Override
    public void cardAdded(String nickname, PhysicalCard card);

    @Override
    public void cardRemoved(String nickname, int index);

    @Override
    public void cardPlaced(String nickname, PlayableCard card, int x, int y);

    @Override
    public void cardDrawn(DrawingPosition drawingPosition);

    @Override
    public void gameEnd(List<String> winnerList, int totalScore, int objectivesScore);

    @Override
    public void chatMessage(String nickname, String message);
     */
}
