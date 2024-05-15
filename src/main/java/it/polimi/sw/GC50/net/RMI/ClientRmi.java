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
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static it.polimi.sw.GC50.net.util.Request.DRAW_CARD;

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface {
    private ServerRmi serverRmi;
    private String servername;
    //////////////////////////////////////////
    private String nickName;
    private TypeOfView typeOfView;
    private View view;
    private String matchName;
    private ArrayList<String> freeMatch;
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
        this.connection();
        this.freeMatch = new ArrayList<>();
        this.gameStatus = GameStatus.SETUP;
        myTurn = false;
        notify = false;
        error = false;
        allPlayerReady = false;
        statusGame = false;
        notifyUpdateChat = false;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService2 = Executors.newSingleThreadScheduledExecutor();
        this.lock = new Object();
        lock2 = new ReentrantLock();
    }
    //////////////////////////////////////////
    //COMUNICATION WITH SERVER
    ///////////////////////////////////////////

    public void connection() throws RemoteException {
        try {
            this.serverRmi = (ServerRmi) Naming.lookup(servername);
            this.serverRmi.addClient(this);
            System.out.println("Connected to server");

        } catch (Exception e) {

            System.out.println("Error in connection");
        }

    }


    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////
    public void addView(View view, TypeOfView typeOfView) {
        this.view = view;
        this.typeOfView = typeOfView;
    }


    public String createGame(String matchName, int numberOfPlayer) {
        try {
            this.matchName = this.serverRmi.createGame(numberOfPlayer, matchName, this, this.nickName);
        } catch (RemoteException e) {
            return null;
        }
        return this.matchName;
    }


    public String enterGame(String matchName) {
        try {
            this.matchName = this.serverRmi.enterGame(matchName, this, this.nickName);
        } catch (RemoteException e) {
            return null;
        }
        return this.matchName;
    }


    public String setName(String name) {
        try {
            this.nickName = this.serverRmi.setName(this, name);
        } catch (RemoteException e) {
            return null;
        }
        return this.nickName;
    }


    public ArrayList<String> getFreeMatch() {
        try {
            this.freeMatch = this.serverRmi.getFreeMatch();
        } catch (RemoteException e) {
            return null;
        }
        return this.freeMatch;
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    public void placeCard(PlaceCardMex placeCardMex) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        try {
            this.serverRmi.message(Request.PLACE_CARD, placeCardMex, this.matchName, this.nickName, this);
        } catch (RemoteException e) {
            return;
        }

    }

    public void sendMessage(String message) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        try {
            this.serverRmi.message(Request.MEX_CHAT, message, this.matchName, this.nickName, this);
        } catch (RemoteException e) {
            return;
        }

    }


    public void selectStarterFace(Boolean index) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        try {
            this.serverRmi.message(Request.SELECT_STARTER_FACE, index, this.matchName, this.nickName, this);
        } catch (RemoteException e) {

        }
    }


    public void selectObjectiveCard(int index) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        try {
            this.serverRmi.message(Request.SELECT_OBJECTIVE_CARD, index, this.matchName, this.nickName, this);
        } catch (RemoteException e) {

        }
    }


    public void drawCard(DrawingPosition position) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        try {
            this.serverRmi.message(DRAW_CARD, position, this.matchName, this.nickName, this);
        } catch (RemoteException e) {

        }
    }


    public Object getModel() {

        try {
            modelMex = (ModelMex) serverRmi.getModel(this.matchName, this.nickName, this, Request.GET_MODEL, null);
        } catch (RemoteException e) {

        }
        view.addModel(this.modelMex);
        if (this.modelMex.getCurrentPlayer().equals(this.nickName)) {
            this.myTurn = true;
        } else {
            this.myTurn = false;
        }
        return this.modelMex;
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
                    notify= true;
                    error=false;
                    this.lock.notifyAll();
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
                this.gameStatus = GameStatus.PLAYING;
                this.statusGame = true;
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
                if (mex.getObject().equals(nickName)) {
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
                        this.lock.notifyAll();
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
    private void firstPhase() {
        //this.lock = new Object();
        this.getModel();
        view.addModel(this.modelMex);
        this.selectObjectiveCard(view.selectObjectiveCard());
        this.selectStarterFace(view.selectStarterFace());
        midPhase();

    }

    private void midPhase() {
        while (!this.statusGame) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {

            }
        }
        getModel();
        int y = 41;
        int x = 41;

        while (statusGame) {
            if (this.myTurn) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }

                if (modelMex.getPlayingPhase().equals(PlayingPhase.DRAWING)) {

                    x++;
                    y++;
                    //this.drawCard(view.choseWhereToDraw());
                    //setMessageout(new Message.MessageClientToServer(Request.DRAW_CARD, view.choseWhereToDraw(), this.matchName, this.nickName));
                    view.updateBoard();
                    try {
                        serverRmi.message(DRAW_CARD, DrawingPosition.GOLD1, this.matchName, this.nickName, this);
                    } catch (RemoteException e) {

                    }
                    waitNoifyfromServer();


                } else if (modelMex.getPlayingPhase().equals(PlayingPhase.PLACING)) {
                    //view.askPlaceCard();
                    view.updateBoard();
                    //this.placeCard(view.askPlaceCard());
                    try {
                        serverRmi.message(Request.PLACE_CARD, new PlaceCardMex(1, false, x, y), this.matchName, this.nickName, this);
                    } catch (RemoteException e) {
                        System.out.println("error");
                    }
                    waitNoifyfromServer();


                }
            } else {
                waitMyTurn();
            }
        }
        endPhase();
    }

    private void waitNoifyfromServer() {


        synchronized (lock2) {
            thread2pause = false;
            this.lock2.notifyAll();
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
        thread2pause= true;

    }


    private void waitMyTurn() {
        while (!myTurn) {
            waitNoifyfromServer();
            if (!error) {
                getModel();
            }
            error = false;
        }
    }

    private void endPhase() {
        this.getModel();
    }

    public void lobby() {
        thread1 = new Thread(() -> {
            lobby0();
        });
        thread1.setPriority(10);
        thread1.start();
    }


    private void lobby0() {
        while (this.setName(view.selectName()) == null) {
            System.out.println("name not valid");
        }
        do {
            switch (view.selectJoinOrCreate()) {
                case 1: {
                    while (this.createGame(view.selectGameName(), view.selectNumberOfPlayers()) == null) {
                        System.out.println("game name not valid");
                    }
                    break;
                }
                case 2: {
                    if (this.getFreeMatch() == null || this.freeMatch.isEmpty()) {
                        System.out.println("no free match");
                        break;
                    }
                    System.out.println("free match");
                    for (String s : this.freeMatch) {
                        System.out.println(s);
                    }
                    while (this.enterGame(view.selectGameName()) == null) {
                        System.out.println("game name not valid");
                    }
                    break;
                }
            }
        } while (this.matchName == null);

        waitingPlayer();

    }

    private void waitingPlayer() {

        view.waitPlayer();
        long startTime = System.currentTimeMillis();

        while (!this.allPlayerReady) {
            try {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                if (elapsedTime >= 4 * 60 * 1000) {
                    System.out.println("timeout");
                    break;
                }
                Thread.sleep(11);
            } catch (InterruptedException e) {
                this.lobby();
            }
        }
        if (this.allPlayerReady) {
            view.allPlayerReady();
            firstPhase();
        } else {
            this.lobby();
        }

    }

}
