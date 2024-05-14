package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientSCK implements Runnable {
    private View view;
    private TypeOfView typeOfView;
    private final int port;
    private final String address;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private ArrayList<String> freeMatch;
    private GameStatus gameStatus;
    //match
    private String matchName;
    private String nickName;
    private ModelMex modelMex;
    ///////////////////////////////////////////
    private boolean notyfyUpdateModel;
    private boolean notifyUpdateChat;
    private boolean notify;
    private boolean alive;
    private boolean allPlayerReady;
    private boolean myTurn;
    private boolean error;
    private Boolean statusGame;
    ///////////////////////////////////////////
    //Thread
    Thread thread2;
    Thread thread1;
    private final ExecutorService executorService;


    public ClientSCK(int port, String address) throws IOException {
        this.port = port;
        this.address = address;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), 1000);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        view = null;
        this.myTurn = false;
        this.alive = true;
        this.notify = false;
        this.matchName = null;
        this.nickName = null;
        this.freeMatch = new ArrayList<>();
        this.notifyUpdateChat = false;
        this.notyfyUpdateModel = false;
        this.allPlayerReady = false;
        this.gameStatus = GameStatus.SETUP;
        this.statusGame = false;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.error = false;
    }

    //////////////////////////////////////////
    //COMUNICATION WITH SERVER
    ///////////////////////////////////////////

    private void inputThread() {

        executorService.execute(() -> {
            while (!executorService.isShutdown()) {
                try {
                    Object object = input.readObject();
                    Message message = (Message) object;
                    switchmex(message);

                } catch (IOException | ClassNotFoundException e) {

                }

            }
        });

    }

    private synchronized void setMessageout(Message.MessageClientToServer messageout) {
        try {
            output.writeObject(messageout);
            output.flush();
            output.reset();

        } catch (IOException e) {

        }
    }

    private synchronized void switchmex(Message mex) {

        switch (mex.getRequest()) {
            case SET_NAME_RESPONSE: {
                boolean response = (boolean) mex.getObject();
                if (!response) {
                    nickName = null;
                }
                notify = true;
                break;
            }
            case CREATE_GAME_RESPONSE, ENTER_GAME_RESPONSE: {
                boolean response = (boolean) mex.getObject();
                if (!response) {
                    this.matchName = null;
                }
                notify = true;
                break;
            }
            case GET_FREE_MATCH_RESPONSE: {
                freeMatch = (ArrayList<String>) mex.getObject();
                notify = true;
                break;
            }
            case NOTIFY_GAME_SETUP: {
                allPlayerReady = true;
                break;
            }

            case NOTIFY_CARD_PLACED, NOTIFY_NEXT_TURN, NOTIFY_CARD_DRAW: {
                notify = true;
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
                    error = true;
                    notify = true;
                }
                break;
            }
            case GET_MODEL_RESPONSE: {
                modelMex = (ModelMex) mex.getObject();
                notyfyUpdateModel = true;
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


    }


    private void waitNoifyfromServer() {
        notify = false;
        while (!notify) {
            try {
                Thread.sleep(12);
            } catch (InterruptedException e) {

            }
        }

    }


//////////////////////////////////////////
//LOBBY
///////////////////////////////////////////


    public void addView(View view, TypeOfView typeOfView) {
        this.view = view;
        this.typeOfView = typeOfView;
    }

    private String createGame(String matchName, int numberOfPlayer) {
        this.matchName = matchName;
        setMessageout(new Message.MessageClientToServer(Request.CREATE_GAME, numberOfPlayer, matchName, nickName));
        waitNoifyfromServer();
        return this.matchName;

    }

    private String enterGame(String matchName) {
        this.matchName = matchName;
        setMessageout(new Message.MessageClientToServer(Request.ENTER_GAME, null, matchName, nickName));
        waitNoifyfromServer();
        return this.matchName;
    }

    private String setName(String name) {
        this.nickName = name;
        setMessageout(new Message.MessageClientToServer(Request.SET_NAME, null, null, name));
        waitNoifyfromServer();
        return this.nickName;
    }

    private ArrayList<String> getFreeMatch() {
        setMessageout(new Message.MessageClientToServer(Request.GET_FREE_MATCH, null, null, null));
        waitNoifyfromServer();
        return this.freeMatch;
    }


//////////////////////////////////////////
//ACTIVE GAME
///////////////////////////////////////////


    private void placeCard(PlaceCardMex placeCardMex) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message.MessageClientToServer(Request.PLACE_CARD, placeCardMex, this.matchName, this.nickName));
    }


    private void sendMessage(String message) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message.MessageClientToServer(Request.MEX_CHAT, message, this.matchName, this.nickName));
    }


    private void selectStarterFace(Boolean index) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message.MessageClientToServer(Request.SELECT_STARTER_FACE, index, this.matchName, this.nickName));
    }


    private void selectObjectiveCard(int index) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message.MessageClientToServer(Request.SELECT_OBJECTIVE_CARD, index, this.matchName, this.nickName));

    }


    private void drawCard(DrawingPosition position) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message.MessageClientToServer(Request.DRAW_CARD, position, this.matchName, this.nickName));
    }


    private Object getModel() {

        setMessageout(new Message.MessageClientToServer(Request.GET_MODEL, null, this.matchName, this.nickName));
        waitNotifyModelChangedFromServer();
        view.addModel(this.modelMex);

        if (this.modelMex.getCurrentPlayer().equals(this.nickName)) {
            this.myTurn = true;
        } else {
            this.myTurn = false;
        }
        return this.modelMex;
    }


    private void waitNotifyModelChangedFromServer() {
        this.notyfyUpdateModel = false;
        while (!this.notyfyUpdateModel) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {

            }

        }

    }

    private void updateView() {

    }

    //////////////////////////////////////////
    //LOBBY_CONTROLLER
    ///////////////////////////////////////////
    public void lobby() {
        while (this.setName(view.askName()) == null) {
            System.out.println("name not valid");
        }
        do {
            switch (view.joinorcreate()) {
                case 1: {
                    while (this.createGame(view.askGameName(), view.askNumberOfPlayer()) == null) {
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
                    while (this.enterGame(view.askGameName()) == null) {
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


    //////////////////////////////////////////
    //ACTIVE_GAME_CONTROLLER
    ///////////////////////////////////////////
    private void firstPhase() {

        this.getModel();
        view.addModel(this.modelMex);
        this.selectObjectiveCard(view.SelectObjectiveCard());
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
            if (myTurn) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                }

                if (modelMex.getPlayingPhase().equals(PlayingPhase.DRAWING)) {
                    x++;
                    y++;
                    // this.drawCard(view.choseWhereToDraw());

                    view.updateBoard();
                    setMessageout(new Message.MessageClientToServer(Request.DRAW_CARD, DrawingPosition.GOLD1, this.matchName, this.nickName));
                    waitNoifyfromServer();
                    if (!error) {
                        getModel();
                        view.updateBoard();
                    }
                    error = false;


                } else if (modelMex.getPlayingPhase().equals(PlayingPhase.PLACING)) {

                    //this.placeCard(view.askPlaceCard());
                    view.updateBoard();
                    setMessageout(new Message.MessageClientToServer(Request.PLACE_CARD, new PlaceCardMex(false, 1, x, y), this.matchName, this.nickName));
                    waitNoifyfromServer();
                    if (!error) {
                        getModel();
                    }
                    error = false;

                }
            } else {

                waitMyTurn();
            }
        }
        endPhase();
    }


    private void waitMyTurn() {
        while (!myTurn) {
            waitNoifyfromServer();
            if (!error) {
                getModel();
                view.updateBoard();
            }
            error = false;

        }
    }

    private void endPhase() {
        this.getModel();
    }

    //////////////////////////////////////////
    //PASSIVE_GAME_CONTROLLER
    ///////////////////////////////////////////


    @Override
    public void run() {
        thread1 = new Thread(() -> {
            inputThread();
        });
        thread1.start();

        /*thread2 = new Thread(() -> {
            outputThread();
        });
        thread2.start();*/
    }


}

