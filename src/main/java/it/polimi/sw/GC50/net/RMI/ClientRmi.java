package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.ViewType;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.rmi.Naming;
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
    private String servername;
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
    Boolean allPlayerReady;
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
        this.servername = name;
        connection();
        gameStatus = GameStatus.SETUP;
        myTurn = false;
        notify = false;
        error = false;
        allPlayerReady = false;
        statusGame = false;
        notifyUpdateChat = false;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService2 = Executors.newSingleThreadScheduledExecutor();
        lock = new Object();
        lock2 = new ReentrantLock();
    }
    //////////////////////////////////////////
    //COMMUNICATION WITH SERVER
    ///////////////////////////////////////////

    public void connection() throws RemoteException {
        try {
            serverRmi = (ServerRmi) Naming.lookup(servername);
            serverRmi.addClient(this);
            System.out.println("Connected to server");
        } catch (Exception e) {
            System.out.println("Error in connection");
        }
    }

    public void addView(View view, ViewType viewType) {
        this.view = view;
        this.viewType = viewType;
    }


    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void lobby() {
        thread1 = new Thread(() -> {
            lobby0();
        });
        thread1.setPriority(10);
        thread1.start();
    }

    private void lobby0() {
        while (!setPlayer(view.selectName())) {
            System.out.println("Player name not valid");
        }

        while (true) {
            boolean isInGame = false;
            switch (view.selectJoinOrCreate()) {
                case 1 -> {
                    if (createGame(view.selectGameName(), view.selectNumberOfPlayers())) {
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
                        if (joinGame(view.selectGameName())) {
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
            if(isInGame) {
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
        while (!allPlayerReady) {
            try {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                if (elapsedTime >= 4 * 60 * 1000) {
                    System.out.println("Timeout");
                    break;
                }
                Thread.sleep(11);
            } catch (InterruptedException e) {
                lobby();
            }
        }
        if (allPlayerReady) {
            view.allPlayerReady();
            setupPhase();
        } else {
            lobby();
        }
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////
    public void placeCard(PlaceCardMex placeCardMex) {
        try {
            serverRmi.message(Request.PLACE_CARD, placeCardMex, this);
        } catch (RemoteException e) {
            return;
        }
    }

    public void sendMessage(String message) {
        try {
            serverRmi.message(Request.MEX_CHAT, message, this);
        } catch (RemoteException e) {
            return;
        }
    }


    public void selectStarterFace(Boolean index) {
        try {
            serverRmi.message(Request.SELECT_STARTER_FACE, index, this);
        } catch (RemoteException e) {
            return;
        }
    }

    public void selectObjectiveCard(int index) {
        try {
            serverRmi.message(Request.SELECT_OBJECTIVE_CARD, index, this);
        } catch (RemoteException e) {
            return;
        }
    }


    public void drawCard(DrawingPosition position) {
        try {
            serverRmi.message(DRAW_CARD, position, this);
        } catch (RemoteException e) {
            return;
        }
    }


    public Object getModel() {
        try {
            modelMex = (ModelMex) serverRmi.getModel(Request.GET_MODEL, null, this);
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

            case NOTIFY_GAME_SETUP: {
                allPlayerReady = true;
                break;
            }

            case NOTIFY_CARD_PLACED, NOTIFY_NEXT_TURN, NOTIFY_CARD_DRAW: {
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
                break;
            }
            case NOTIFY_PLAYER_JOINED_GAME: {
                break;
            }
            case NOTIFY_PLAYER_LEFT_GAME: {
                break;
            }
            case NOTIFY_PLAYER_READY: {
                break;
            }

            case NOTIFY_GAME_STARTED: {
                gameStatus = GameStatus.PLAYING;
                statusGame = true;
                System.out.println("Game started");
                break;
            }
            case NOTIFY_CHAT_MESSAGE: {
                notifyUpdateChat = true;
                break;
            }
            case NOTIFY_ALL_PLAYER_JOINED_THE_GAME: {
                break;
            }
            case NOTIFY_CARD_NOT_FOUND, NOTIFY_POSITION_DRAWING_NOT_AVAILABLE, NOTIFY_INVALID_INDEX,
                    NOTIFY_OPERATION_NOT_AVAILABLE, NOTIFY_NOT_YOUR_PLACING_PHASE, NOTIFY_CARD_NOT_PLACEABLE: {
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
                break;
            }
            case GET_CHAT_MODEL_RESPONSE: {
                System.out.println("chat");
                break;
            }

            default: {
                break;
            }

        }
        thread2.interrupt();

    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Update from server");
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


    private void setupPhase() {
        //lock = new Object();
        getModel();
        view.addModel(modelMex);
        selectObjectiveCard(view.selectObjectiveCard());
        selectStarterFace(view.selectStarterFace());
        playingPhase();
    }

    private void playingPhase() {
        while (!statusGame) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {

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
                    try {
                        serverRmi.message(Request.PLACE_CARD, view.selectPlaceCard(), this);
                    } catch (RemoteException e) {
                        System.out.println("error");
                    }
                } else {
                    try {
                        serverRmi.message(DRAW_CARD, view.selectDrawingPosition(), this);
                    } catch (RemoteException e) {
                        System.out.println("error");
                    }
                }

                waitNotifyFromServer();
            } else {
                waitMyTurn();
            }
        }
        endPhase();
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

    private void endPhase() {
        getModel();
    }
}
