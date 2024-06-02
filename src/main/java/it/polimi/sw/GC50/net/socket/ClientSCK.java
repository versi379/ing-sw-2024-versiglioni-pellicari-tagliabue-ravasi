package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;

import it.polimi.sw.GC50.net.Messages.*;
import it.polimi.sw.GC50.net.util.*;
import it.polimi.sw.GC50.view.Command;
import it.polimi.sw.GC50.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientSCK implements Runnable, ServerInterface {

    private final int port;
    private final String address;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Map<String, List<String>> freeMatch;
    //match
    private String matchName;
    private String nickName;
    ///////////////////////////////////////////
    ///////////////////////////////////////////
    //Thread
    Thread thread2;
    Thread thread1;
    private final ExecutorService executorService;
    /////////////////////////////////////////////////////
    private Object[] lock;
    private Queue<SocketMessage> queue;
    private boolean[] condition;
    private Client client;

    public ClientSCK(Client client, String port, String address) throws IOException {
        this.port = Integer.parseInt(port);
        this.address = address;
        this.client = client;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(this.address, this.port), 1000);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.matchName = null;
        this.nickName = null;
        this.freeMatch = new HashMap<>();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.queue = new LinkedList<>();
        this.condition = new boolean[6];
        this.lock = new Object[6];
        for (int i = 0; i < 6; i++) {
            this.condition[i] = true;
            this.lock[i] = new Object();
        }
    }

    //////////////////////////////////////////
    //COMUNICATION WITH SERVER
    ///////////////////////////////////////////

    private void inputThread() {
        executorService.execute(() -> {
            while (!executorService.isShutdown()) {
                try {
                    Object object = input.readObject();
                    SocketMessage message = (SocketMessage) object;
                    queue.add(message);
                    notifyMessageFromServer();

                } catch (IOException | ClassNotFoundException e) {

                }
            }
        });
    }

    private void switchMex(SocketMessage message) {
        switch (message.getNotify()) {
            case NOTIFY_PLAYER_JOINED_GAME, GET_MODEL_RESPONSE, REQUEST_NOT_AVAILABLE, NOTIFY_GAME_ENDED,
                 GET_CHAT_MODEL_RESPONSE, NOTIFY_NEXT_TURN, NOTIFY_CHAT_MESSAGE, NOTIFY_CARD_DRAWN,
                 NOTIFY_GAME_STARTED, NOTIFY_PLAYER_READY, NOTIFY_CARD_PLACED, NOTIFY_GAME_SETUP,
                 NOTIFY_PLAYER_LEFT_GAME, NOTIFY_ERROR -> {
                client.update(message.getNotify(), message.getMessage());
            }
            case NOTIFY_NAME_SET -> {
                ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                if (objectMessage.getObject() != null) {
                    this.nickName = objectMessage.getObject().toString();

                } else {
                    this.nickName = null;
                }
                notifySetupFase();
            }
            case NOTIFY_FREE_GAMES -> {
                ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                this.freeMatch = (Map<String, List<String>>) objectMessage.getObject();
                notifySetupFase();
            }
            case NOTIFY_GAME_CREATED, NOTIFY_GAME_JOINED -> {
                ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                if (objectMessage.getObject() != null) {
                    this.matchName = objectMessage.getObject().toString();
                } else {
                    this.matchName = null;
                }
                notifySetupFase();
            }
        }

    }

    private synchronized void setMessageout(SocketMessage messageout) {
        try {
            output.writeObject(messageout);
            output.flush();
            output.reset();
        } catch (IOException e) {

        }
    }

    private void lock(int index) {
        if (index > this.condition.length) {
            return;
        }
        synchronized (this.lock[index]) {
            while (this.condition[index]) {
                try {
                    this.lock[index].wait();
                } catch (InterruptedException e) {
                    System.out.println("error");
                }
            }
        }
        condition[index] = true;
    }

    private void unlock(int index) {
        if (index > this.condition.length) {
            return;
        }
        synchronized (this.lock[index]) {
            this.condition[index] = false;
            this.lock[index].notifyAll();
        }
    }

    private void notifyMessageFromServer() {
        unlock(0);
    }

    private void waitMessageFromServer() {
        lock(0);
    }

    private void notifySetupFase() {
        unlock(1);
    }

    private void waitSetupFase() {
        lock(1);
    }


    //////////////////////////////////////////
    //PASSIVE_GAME_CONTROLLER
    ///////////////////////////////////////////
    @Override
    public void run() {
        thread1 = new Thread(() -> {
            inputThread();
        });

        thread2 = new Thread(() -> {
            while (true) {
                waitMessageFromServer();
                if (!queue.isEmpty()) {
                    switchMex(queue.poll());
                }
            }
        });
        thread2.start();
        thread1.start();

    }

    @Override
    public void connect() {

    }

    @Override
    public String setPlayer(String nickname) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(nickname), LobbyCommand.SET_PLAYER_NAME));
        waitSetupFase();
        return this.nickName;
    }

    @Override
    public void resetPlayer() throws GameException {

    }

    @Override
    public boolean createGame(String gameId, int numPlayers, int endScore) throws GameException {
        if (matchName == null) {
            setMessageout(new SocketMessage(new CreateGameMessage(gameId,numPlayers,endScore), LobbyCommand.CREATE_GAME));
            waitSetupFase();
        } else {
            return false;
        }
        if (matchName != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean joinGame(String gameId) throws GameException {
        if (matchName == null) {
            setMessageout(new SocketMessage(new ObjectMessage(gameId), LobbyCommand.JOIN_GAME));
            waitSetupFase();
        } else {
            return false;
        }
        if (matchName != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, List<String>> getFreeGames() throws GameException {
        setMessageout(new SocketMessage(null, LobbyCommand.LIST_FREE_GAMES));
        waitSetupFase();
        return freeMatch;
    }

    @Override
    public void selectSecretObjective(int index) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(index), Command.CHOOSE_OBJECTIVE));
    }

    @Override
    public void selectStarterFace(int face) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(face), Command.CHOOSE_STARTER_FACE));
    }

    @Override
    public void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(placeCardRequest), Command.PLACE_CARD));
    }

    @Override
    public void drawCard(int position) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(position), Command.DRAW_CARD));
    }

    @Override
    public void sendChatMessage(ChatMessageRequest message) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(message), Command.CHAT));
    }
}