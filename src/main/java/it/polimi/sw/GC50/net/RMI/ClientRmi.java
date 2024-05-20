package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    //private ModelMex modelMex;
    ///////////////////////////////////////////
    private GameStatus gameStatus;
    private PlayingPhase playingPhase;
    private boolean myTurn;
    private boolean notify;
    private boolean error;
    private boolean notifyUpdateChat;
    ///////////////////////////////////////////
    /////////////////////////////////////////
    //////////////////////////////////////////////////////////

    public ClientRmi(String name) throws RemoteException {
        this.serverName = name;
        gameStatus = GameStatus.WAITING;
        playingPhase = PlayingPhase.PLACING;
        myTurn = false;
        notify = false;
        error = false;
        notifyUpdateChat = false;
    }

    public void addView(View view, ViewType viewType) {
        this.view = view;
        this.viewType = viewType;
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    private void connect() throws RemoteException {
        try {
            serverRmi = (ServerRmiRemote) Naming.lookup(serverName + "/server");
            serverRmi.addClient(this);
            System.err.println("Connected to server");
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.err.println("Error in connection to the server");
            throw new RemoteException();
        }
    }

    private void connectController(String gameId) throws RemoteException {
        try {
            gameController = (GameControllerRemote) Naming.lookup(serverName + "/" + gameId);
            System.err.println("Connected to controller");
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.err.println("Error in connection to the controller");
            throw new RemoteException();
        }
    }

    public synchronized void run() {
        try {
            connect();
            lobby();
            view.endSession();
        } catch (RemoteException e) {
            System.err.println("Connection error");
        }
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void lobby() throws RemoteException {
        while (!setPlayer(view.selectName())) {
            view.printMessage("Player name not valid");
        }

        while (true) {
            boolean isInGame = false;
            String gameId = null;
            switch (view.selectJoinOrCreate()) {
                case 1 -> {
                    gameId = view.selectGameName();
                    if (createGame(gameId, view.selectNumberOfPlayers())) {
                        isInGame = true;
                    } else {
                        view.printMessage("Game name not valid");
                    }
                }

                case 2 -> {
                    List<String> freeMatches = getFreeMatches();
                    if (freeMatches.isEmpty()) {
                        view.printMessage("No free matches");
                    } else {
                        view.printMessage("Free matches:");
                        for (String s : freeMatches) {
                            view.printMessage(s);
                        }

                        gameId = view.selectGameName();
                        if (joinGame(gameId)) {
                            isInGame = true;
                        } else {
                            view.printMessage("Game name not valid");
                        }
                    }
                }

                case 3 -> {
                    view.printMessage("Quitting");
                    return;
                }
            }
            if (isInGame) {
                connectController(gameId);
                waitingPhase();
            }
        }
    }

    private boolean setPlayer(String nickname) throws RemoteException {
        if (serverRmi.setPlayer(this, nickname)) {
            this.nickname = nickname;
            return true;
        }
        return false;
    }

    private boolean createGame(String gameId, int numPlayers) throws RemoteException {
        return serverRmi.createGame(this, numPlayers, gameId, nickname);
    }

    private List<String> getFreeMatches() throws RemoteException {
        return serverRmi.getFreeMatches();
    }

    private boolean joinGame(String gameId) throws RemoteException {
        return serverRmi.joinGame(this, gameId, nickname);
    }

    // WAITING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void waitingPhase() throws RemoteException {
        view.waitPlayers();

        while (gameStatus.equals(GameStatus.WAITING)) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Interruption error");
                return;
            }
        }
        setupPhase();
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setupPhase() throws RemoteException {
        view.setup();
        getModel();
        selectObjectiveCard(view.selectObjectiveCard());
        selectStarterFace(view.selectStarterFace());

        while (gameStatus.equals(GameStatus.SETUP)) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Interruption error");
                return;
            }
        }
        playingPhase();
    }

    private void selectObjectiveCard(int index) throws RemoteException {
        gameController.updateController(Request.SELECT_OBJECTIVE_CARD, index, this);
    }

    private void selectStarterFace(boolean index) throws RemoteException {
        gameController.updateController(Request.SELECT_STARTER_FACE, index, this);
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void playingPhase() throws RemoteException {
        view.printMessage("Partita iniziata");

        while (gameStatus.equals(GameStatus.PLAYING)) {
            while (!myTurn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.err.println("Interruption error");
                    return;
                }
            }
            playTurn();
        }
        endPhase();
    }

    private void playTurn() throws RemoteException {
        getModel();

        do {
            if (error) {
                view.error();
                error = false;
            }
            placeCard(view.selectPlaceCard());
            while (playingPhase.equals(PlayingPhase.PLACING) && !error) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.err.println("Interruption error");
                    return;
                }
            }
        } while (playingPhase.equals(PlayingPhase.PLACING));

        do {
            if (error) {
                view.error();
                error = false;
            }
            drawCard(view.selectDrawingPosition());
            while (playingPhase.equals(PlayingPhase.DRAWING) && !error) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.err.println("Interruption error");
                    return;
                }
            }
        } while (playingPhase.equals(PlayingPhase.DRAWING));
    }

    private void placeCard(PlaceCardMex placeCardMex) throws RemoteException {
        gameController.updateController(Request.PLACE_CARD, placeCardMex, this);
    }

    private void drawCard(DrawingPosition position) throws RemoteException {
        gameController.updateController(DRAW_CARD, position, this);
    }

    private void sendMessage(String message) throws RemoteException {
        gameController.updateChat(message, this);
    }

    // END /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void endPhase() {
        getModel();
        view.printMessage("Fine partita spe poi metto a posto");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void getModel() {
        try {
            gameView = (GameView) gameController.getModel(this);
            view.addModel(gameView);
        } catch (RemoteException e) {
            System.err.println("Error in fetching model from server");
        }
    }

    //////////////////////////////////////////
//OBSERVER
///////////////////////////////////////////
    @Override
    public void ping() throws RemoteException {
    }

    @Override
    public void update(Request request, Object arg) {
        System.err.println("Update from server");
        new Thread(() -> {
            synchronized (this) {
                switchRequest(request, arg);
                notifyAll();
            }
        }).start();
    }

    private void switchRequest(Request request, Object arg) {
        switch (request) {
            case NOTIFY_PLAYER_JOINED_GAME -> {
                view.playerJoined((String) arg);
            }

            case NOTIFY_PLAYER_LEFT_GAME -> {
                view.playerLeft((String) arg);
            }

            case NOTIFY_GAME_SETUP -> {
                getModel();
                gameStatus = GameStatus.SETUP;
            }

            case NOTIFY_PLAYER_READY -> {
                getModel();
                view.playerReady((String) arg);
            }

            case NOTIFY_GAME_STARTED -> {
                getModel();
                gameStatus = GameStatus.PLAYING;
                playingPhase = PlayingPhase.PLACING;
                myTurn = arg.equals(nickname);
            }

            case NOTIFY_CARD_PLACED -> {
                getModel();
                playingPhase = PlayingPhase.DRAWING;
                view.printPlayerArea((String) arg);
                view.printScores();
            }

            case NOTIFY_CARD_DRAWN -> {
                getModel();
                myTurn = false;
                view.printDecks();
            }

            case NOTIFY_NEXT_TURN -> {
                getModel();
                playingPhase = PlayingPhase.PLACING;
                myTurn = arg.equals(nickname);
            }

            case NOTIFY_GAME_ENDED -> {
                getModel();
                gameStatus = GameStatus.ENDED;
            }

            case NOTIFY_CHAT_MESSAGE -> {
                notifyUpdateChat = true;
            }

            case NOTIFY_CARD_NOT_FOUND, NOTIFY_DRAWING_POSITION_NOT_AVAILABLE, NOTIFY_INVALID_INDEX,
                    NOTIFY_OPERATION_NOT_AVAILABLE, NOTIFY_NOT_YOUR_PLACING_PHASE, NOTIFY_CARD_NOT_PLACEABLE -> {
                if (arg.equals(nickname)) {
                    error = true;
                }
            }

            case GET_CHAT_MODEL_RESPONSE -> {
                view.printMessage("chat");
            }

            default -> {
            }
        }
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
