package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.Messages.*;
import it.polimi.sw.GC50.net.util.*;
import it.polimi.sw.GC50.view.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * class that represents the client for the socket connection
 * it implements the ServerInterface and Runnable
 * it implements the run method that starts the client
 * it implements the methods to connect to the server and to communicate with the server
 */
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

    /**
     * constructor of the class
     *
     * @param client  is the client
     * @param port    is the port number
     * @param address is the address of the server
     * @throws IOException if an error occurs
     */
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

    /**
     * method that listens for messages from the server
     * the method reads an object from the input stream
     * the object is cast to a SocketMessage
     * the message is added to the queue
     * the method notifies the client that a message has been received
     * if an error occurs the method returns
     * the method is an infinite loop
     * the method is executed by a thread
     */
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

    /**
     * method that switches the message received from the server
     * the method switches the message based on the notify
     * the method calls the update method of the client
     *
     * @param message is the message received from the server
     */
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

    /**
     * method that sends a message to the server
     * the method writes the message to the output stream
     * the method flushes the output stream
     * the method resets the output stream
     * if an error occurs the method returns
     *
     * @param messageout is the message to send to the server
     */
    private synchronized void setMessageout(SocketMessage messageout) {
        try {
            output.writeObject(messageout);
            output.flush();
            output.reset();
        } catch (IOException e) {

        }
    }

    /**
     * method that wait for notify from the unlock method
     *
     * @param index is the index of the condition
     */
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

    /**
     * method that notify the unlock method
     *
     * @param index is the index of the condition
     */
    private void unlock(int index) {
        if (index > this.condition.length) {
            return;
        }
        synchronized (this.lock[index]) {
            this.condition[index] = false;
            this.lock[index].notifyAll();
        }
    }

    /**
     * method that notify the method wait
     */
    private void notifyMessageFromServer() {
        unlock(0);
    }

    /**
     * method that wait for notify from the method notifyMessageFromServer
     */
    private void waitMessageFromServer() {
        lock(0);
    }

    /**
     * method that notify the method waitSetupFase
     */
    private void notifySetupFase() {
        unlock(1);
    }

    /**
     * method that wait for notify from the method notifySetupFase
     */
    private void waitSetupFase() {
        lock(1);
    }

    //////////////////////////////////////////
    //PASSIVE_GAME_CONTROLLER
    ///////////////////////////////////////////

    /**
     * method that starts the client
     * the method starts two threads
     * the first thread is the inputThread
     * the second thread is the waitMessageFromServer
     */
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

    /**
     *
     */
    @Override
    public void connect() {

    }

    /**
     * method that send a message to the server to set the player name
     * it waits for notify from the server and returns the nickname of the player
     * if the name is not available the method returns null
     *
     * @param nickname is the nickname of the player
     */
    @Override
    public String setPlayer(String nickname) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(nickname), LobbyCommand.SET_PLAYER_NAME));
        waitSetupFase();
        return this.nickName;
    }

    /**
     * method that sends a message to the server to reset the player
     */
    @Override
    public void resetPlayer() throws GameException {
        setMessageout(new SocketMessage(null, LobbyCommand.RESET_PLAYER));
    }

    /**
     * method that sends a message to the server to create a game
     * it waits for notify from the server and returns true if the game is created
     * if the game is not created the method returns false
     *
     * @param gameId     is the id of the game
     * @param numPlayers is the number of players
     * @param endScore   is the score to reach to end the game
     * @return a boolean
     * @throws GameException if an error occurs
     */
    @Override
    public boolean createGame(String gameId, int numPlayers, int endScore) throws GameException {
        if (matchName == null) {
            setMessageout(new SocketMessage(new CreateGameMessage(gameId, numPlayers, endScore), LobbyCommand.CREATE_GAME));
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

    /**
     * method that sends a message to the server to join a game
     * it waits for notify from the server and returns true if the player joins the game
     * if the player does not join the game the method returns false
     *
     * @param gameId is the id of the game
     * @return a boolean
     * @throws GameException if an error occurs
     */
    @Override
    public boolean joinGame(String gameId) throws GameException {
        if (matchName == null) {
            setMessageout(new SocketMessage(new ObjectMessage(gameId), LobbyCommand.JOIN_GAME));
            waitSetupFase();
        } else {
            return false;
        }
        return matchName != null;
    }

    /**
     * method that sends a message to the server get the list of free games
     * it waits for the notify from the server and returns the list of free games
     *
     * @return a map
     * @throws GameException if an error occurs
     */
    @Override
    public Map<String, List<String>> getFreeGames() throws GameException {
        setMessageout(new SocketMessage(null, LobbyCommand.LIST_FREE_GAMES));
        waitSetupFase();
        return freeMatch;
    }

    /**
     * method that sends a message to the server to select the objective card of the player
     *
     * @param index is the index of the objective card
     * @throws GameException if an error occurs
     */
    @Override
    public void selectSecretObjective(int index) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(index), Command.CHOOSE_OBJECTIVE));
    }

    /**
     * method that sends a message to the server to select the starter face of the player
     *
     * @param face is the face of the player
     * @throws GameException if an error occurs
     */
    @Override
    public void selectStarterFace(int face) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(face), Command.CHOOSE_STARTER_FACE));
    }

    /**
     * method that sends a message to the server to place a card
     *
     * @param placeCardRequest is the request to place a card
     * @throws GameException if an error occurs
     */
    @Override
    public void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(placeCardRequest), Command.PLACE_CARD));
    }

    /**
     * method that sends a message to the server to draw a card
     *
     * @param position is the position of the card to draw
     * @throws GameException if an error occurs
     */
    @Override
    public void drawCard(int position) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(position), Command.DRAW_CARD));
    }

    /**
     * method that sends a message to the server to send a chat message
     *
     * @param message is the message to send
     * @throws GameException if an error occurs
     */
    @Override
    public void sendChatMessage(ChatMessageRequest message) throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(message), Command.CHAT));
    }

    @Override
    public void leaveGame() throws GameException {
        setMessageout(new SocketMessage(new ObjectMessage(null), Command.LEAVE));
    }
}
