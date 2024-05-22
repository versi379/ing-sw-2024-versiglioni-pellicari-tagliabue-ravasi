package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.Messages.*;
import it.polimi.sw.GC50.net.util.*;
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

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface {
    private final String serverName;
    private ServerRmiRemote serverRmi;
    private GameControllerRemote gameController;
    //////////////////////////////////////////
    private String nickname;
    private View view;
    private ViewType viewType;
    private GameView gameView;
    ///////////////////////////////////////////
    private boolean error;
    private boolean notifyUpdateChat;
    ///////////////////////////////////////////
    /////////////////////////////////////////
    //////////////////////////////////////////////////////////

    public ClientRmi(String name) throws RemoteException {
        this.serverName = name;
        serverRmi = null;
        gameController = null;
        error = false;
        notifyUpdateChat = false;
    }

    public void addView(View view, ViewType viewType) {
        this.view = view;
        this.viewType = viewType;
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

    synchronized public void run() {
        try {
            connect();
            lobby();
            view.showEndSession();
        } catch (GameException e) {
            System.err.println(e.getMessage());
        }
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    synchronized private void lobby() throws GameException {
        while (!setPlayer(view.selectName())) {
            view.showError("Player name not valid");
        }

        while (true) {
            switch (view.selectJoinOrCreate()) {
                case 1 -> {
                    createGame(view.selectGameName(), view.selectNumberOfPlayers());
                }

                case 2 -> {
                    Map<String, List<String>> freeGames = getFreeGames();
                    view.showFreeGames(freeGames);
                    if (!freeGames.isEmpty()) {
                        joinGame(view.selectGameName());
                    }
                }

                case 3 -> {
                    return;
                }
            }
            if (gameController != null) {
                waitingPhase();
            } else {
                view.showError("Game name not valid");
            }
        }
    }

    synchronized private boolean setPlayer(String nickname) throws GameException {
        try {
            if (serverRmi.setPlayer(this, nickname)) {
                this.nickname = nickname;
                gameView = new GameView(nickname);
                return true;
            }
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
        return false;
    }

    synchronized private void createGame(String gameId, int numPlayers) throws GameException {
        try {
            gameController = serverRmi.createGame(this, gameId, numPlayers, nickname);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    synchronized private Map<String, List<String>> getFreeGames() throws GameException {
        try {
            return serverRmi.getFreeGames();
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    synchronized private void joinGame(String gameId) throws GameException {
        try {
            gameController = serverRmi.joinGame(this, gameId, nickname);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // WAITING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    synchronized private void waitingPhase() throws GameException {
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
    synchronized private void setupPhase() throws GameException {
        view.showSetup();
        selectObjectiveCard(view.selectObjectiveCard());
        selectStarterFace(view.selectStarterFace());

        while (gameView.getGameStatus().equals(GameStatus.SETUP)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new GameException("Interruption error", e.getCause());
            }
        }
        playingPhase();
    }

    synchronized private void selectObjectiveCard(int index) throws GameException {
        try {
            gameController.updateController(Request.SELECT_OBJECTIVE_CARD, index, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    synchronized private void selectStarterFace(boolean index) throws GameException {
        try {
            gameController.updateController(Request.SELECT_STARTER_FACE, index, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    synchronized private void playingPhase() throws GameException {
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

    synchronized private void playTurn() throws GameException {
        do {
            error = false;
            placeCard(view.selectPlaceCard());
            while (gameView.getPlayingPhase().equals(PlayingPhase.PLACING) && !error) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new GameException("Interruption error", e.getCause());
                }
            }
        } while (gameView.getPlayingPhase().equals(PlayingPhase.PLACING));

        do {
            error = false;
            drawCard(view.selectDrawingPosition());
            while (gameView.getPlayingPhase().equals(PlayingPhase.DRAWING) && !error) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new GameException("Interruption error", e.getCause());
                }
            }
        } while (gameView.getPlayingPhase().equals(PlayingPhase.DRAWING));
    }

    synchronized private void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        try {
            gameController.updateController(Request.PLACE_CARD, placeCardRequest, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    synchronized private void drawCard(DrawingPosition position) throws GameException {
        try {
            gameController.updateController(DRAW_CARD, position, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    synchronized private void sendMessage(String message) throws GameException {
        try {
            gameController.updateChat(message, this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    synchronized private void endPhase() {
        view.showEnd();
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

                updateView();
                view.showPlayerJoined(player);
            }

            case NOTIFY_PLAYER_LEFT_GAME -> {
                String player = ((PlayerMex) message).getNickname();
                gameView.removePlayerArea(player);

                updateView();
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

                updateView();
            }

            case NOTIFY_PLAYER_READY -> {
                PlayerReadyMex playerReadyMex = (PlayerReadyMex) message;
                String player = playerReadyMex.getNickname();
                if (gameView.getNickname().equals(playerReadyMex.getNickname())) {
                    gameView.setSecretObjective(playerReadyMex.getSecretObjective());
                }
                gameView.setPlayerArea(player, playerReadyMex.getCardsMatrix(),
                        playerReadyMex.getTotalScore(), 0);

                updateView();
                view.showPlayerReady(player);
            }

            case NOTIFY_GAME_STARTED -> {
                gameView.setGameStatus(GameStatus.PLAYING);
                gameView.setPlayingPhase(PlayingPhase.PLACING);
                gameView.setCurrentPlayer(((PlayerMex) message).getNickname());

                updateView();
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
                updateView();
                view.showPlayerArea(player);
                view.showScores();
            }

            case NOTIFY_CARD_DRAWN -> {
                DecksUpdateMex decksUpdateMex = (DecksUpdateMex) message;
                gameView.setDecks(decksUpdateMex.getDecks());
                if (gameView.getNickname().equals(decksUpdateMex.getNickname())) {
                    gameView.setHand((decksUpdateMex.getHand()));
                }

                updateView();
                view.showDecks();
            }

            case NOTIFY_NEXT_TURN -> {
                gameView.setPlayingPhase(PlayingPhase.PLACING);
                gameView.setCurrentPlayer(((PlayerMex) message).getNickname());

                updateView();
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

                updateView();
            }

            case NOTIFY_CHAT_MESSAGE -> {
                notifyUpdateChat = true;
            }

            case NOTIFY_ERROR -> {
                ErrorMex errorMex = (ErrorMex) message;
                if (gameView.getNickname().equals(errorMex.getNickname())) {
                    error = true;
                    view.showError(errorMex.getContent());
                }
            }

            case GET_CHAT_MODEL_RESPONSE -> {

                updateView();
                view.showMessage("chat");
            }

            default -> {
            }
        }
    }

    private void updateView() {
        view.setModel(gameView);
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
