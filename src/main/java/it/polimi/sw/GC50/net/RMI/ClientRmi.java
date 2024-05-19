package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.observ.GameObservable;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.ViewType;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static it.polimi.sw.GC50.net.util.Request.DRAW_CARD;

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface {
    private ServerRmi serverRmi;
    private GameControllerRemote gameController;
    private String serverName;
    //////////////////////////////////////////
    private String nickname;
    private ViewType viewType;
    private View view;
    private String gameId;
    ModelMex modelMex;
    ///////////////////////////////////////////
    Boolean myTurn;
    Boolean notify;
    Boolean error;
    Boolean allPlayersReady;
    Boolean statusGame;
    Boolean notifyUpdateChat;
    ///////////////////////////////////////////
    GameStatus gameStatus;
    /////////////////////////////////////////
    private final ExecutorService executorService;
    private final ExecutorService executorService2;
    private Thread thread1;
    private Thread thread2;
    //////////////////////////////////////////////////////////
    private final Object lock;
    Lock lock2;
    private boolean thread2pause;

    public ClientRmi(String name) throws RemoteException {
        this.serverName = name;
        connect();
        gameStatus = GameStatus.SETUP;
        myTurn = false;
        notify = false;
        error = false;
        allPlayersReady = false;
        statusGame = false;
        notifyUpdateChat = false;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService2 = Executors.newSingleThreadScheduledExecutor();
        lock = new Object();
        lock2 = new ReentrantLock();
    }

    public void addView(View view, ViewType viewType) {
        this.view = view;
        this.viewType = viewType;
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void connect() {
        try {
            serverRmi = (ServerRmi) Naming.lookup(serverName + "/server");
            serverRmi.addClient(this);
            System.out.println("Connected to server");
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println("Error in connection");
        }
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void run() {
        thread1 = new Thread(this::lobby);
        thread1.setPriority(10);
        thread1.start();
    }

    public void connectController(String gameId) {
        try {
            gameController = (GameControllerRemote) Naming.lookup(serverName + "/" + gameId);
            System.out.println("Connected to controller");
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println("Error in connection to the controller");
        }
    }

    private void lobby() {
        while (!setPlayer(view.selectName())) {
            System.out.println("Player name not valid");
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
                        System.out.println("Game name not valid");
                    }
                }

                case 2 -> {
                    List<String> freeMatches = getFreeMatches();
                    if (freeMatches.isEmpty()) {
                        System.out.println("No free matches");
                    } else {
                        System.out.println("Free matches:");
                        for (String s : freeMatches) {
                            System.out.println(s);
                        }

                        gameId = view.selectGameName();
                        if (joinGame(gameId)) {
                            isInGame = true;
                        } else {
                            System.out.println("Game name not valid");
                        }
                    }
                }

                case 3 -> {
                    System.out.println("Quitting");
                    return;
                }
            }
            if (isInGame) {
                connectController(gameId);
                waitingPhase();
            }
        }
    }

    private boolean setPlayer(String nickname) {
        try {
            if (serverRmi.setPlayer(this, nickname)) {
                this.nickname = nickname;
                return true;
            } else {
                return false;
            }
        } catch (RemoteException e) {
            return false;
        }
    }

    private boolean createGame(String gameId, int numPlayers) {
        try {
            return serverRmi.createGame(this, numPlayers, gameId, nickname);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Errore creazione gioco");
            return false;
        }
    }

    private List<String> getFreeMatches() {
        try {
            return serverRmi.getFreeMatches();
        } catch (RemoteException e) {
            return new ArrayList<>();
        }
    }

    private boolean joinGame(String gameId) {
        try {
            return serverRmi.joinGame(this, gameId, nickname);
        } catch (RemoteException e) {
            return false;
        }
    }

    // WAITING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void waitingPhase() {
        view.waitPlayers();

        long startTime = System.currentTimeMillis();
        while (!allPlayersReady) {
            try {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                if (elapsedTime >= 4 * 60 * 1000) {
                    System.out.println("Timeout");
                    break;
                }
                Thread.sleep(11);
            } catch (InterruptedException e) {
                return;
            }
        }
        if (allPlayersReady) {
            view.allPlayerReady();
            setupPhase();
        } else {
            return;
        }
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setupPhase() {
        getModel();
        view.addModel(modelMex);
        selectObjectiveCard(view.selectObjectiveCard());
        selectStarterFace(view.selectStarterFace());
        playingPhase();
    }

    public void selectObjectiveCard(int index) {
        try {
            gameController.updateController(Request.SELECT_OBJECTIVE_CARD, index, this);
        } catch (RemoteException e) {
            return;
        }
    }

    public void selectStarterFace(Boolean index) {
        try {
            gameController.updateController(Request.SELECT_STARTER_FACE, index, this);
        } catch (RemoteException e) {
            return;
        }
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void playingPhase() {
        while (!statusGame) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                System.out.println("AAAAAAAAAAAAAA " + nickname);
            }
        }
        getModel();

        while (statusGame) {
            if (myTurn) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("error");
                }
                view.updateBoard();
                if (modelMex.getPlayingPhase().equals(PlayingPhase.PLACING)) {
                    placeCard(view.selectPlaceCard());
                } else {
                    drawCard(view.selectDrawingPosition());
                }
                waitNotifyFromServer();
            } else {
                waitMyTurn();
            }
        }
        endPhase();
    }

    public void placeCard(PlaceCardMex placeCardMex) {
        try {
            gameController.updateController(Request.PLACE_CARD, placeCardMex, this);
        } catch (RemoteException e) {
            return;
        }
    }

    public void drawCard(DrawingPosition position) {
        try {
            gameController.updateController(DRAW_CARD, position, this);
        } catch (RemoteException e) {
            return;
        }
    }

    public void sendMessage(String message) {
        try {
            gameController.updateChat(message, this);
        } catch (RemoteException e) {
            return;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Object getModel() {
        try {
            modelMex = (ModelMex) gameController.getModel(this);
        } catch (RemoteException e) {

        }
        view.addModel(modelMex);
        if (modelMex.getCurrentPlayer().equals(nickname)) {
            myTurn = true;
        } else {
            myTurn = false;
        }
        return modelMex;
    }


    @Override
    public void ping() throws RemoteException {

    }


    //////////////////////////////////////////
    //OBSERVER
    ///////////////////////////////////////////
    private void switchMex(Message mex) {
        switch (mex.getRequest()) {

            case NOTIFY_GAME_SETUP -> {
                allPlayersReady = true;
            }

            case NOTIFY_CARD_PLACED, NOTIFY_NEXT_TURN, NOTIFY_CARD_DRAW -> {
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

            case NOTIFY_PLAYER_JOINED_GAME -> {
            }

            case NOTIFY_PLAYER_LEFT_GAME -> {
            }

            case NOTIFY_PLAYER_READY -> {
            }

            case NOTIFY_GAME_STARTED -> {
                gameStatus = GameStatus.PLAYING;
                statusGame = true;
                System.out.println("Game started");
            }

            case NOTIFY_CHAT_MESSAGE -> {
                notifyUpdateChat = true;
            }

            case NOTIFY_ALL_PLAYER_JOINED_THE_GAME -> {
            }

            case NOTIFY_CARD_NOT_FOUND, NOTIFY_POSITION_DRAWING_NOT_AVAILABLE, NOTIFY_INVALID_INDEX,
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

                }
            }

            case GET_CHAT_MODEL_RESPONSE -> {
                System.out.println("chat");
            }

            default -> {
            }

        }
        thread2.interrupt();
    }

    @Override
    public void update(GameObservable o, Request request, Object arg) {
        System.out.println("Update from server");

        /*
        if (!(o instanceof Game)) {
            return;
        }
        switch (request) {

        }

         */
    }

    @Override
    synchronized public void onUpdate(Message mex) throws RemoteException {
        thread2 = new Thread(() -> {
            switchMex(mex);
        });
        thread2.start();
    }

    //////////////////////////////////////////
    //ACTIVE_GAME_CONTROLLER
    ///////////////////////////////////////////


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

    private void endPhase() {
        getModel();
    }

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
