package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.Messages.FreeGamesMex;
import it.polimi.sw.GC50.net.Messages.StringMex;
import it.polimi.sw.GC50.net.Requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.Requests.CreateGameRequest;
import it.polimi.sw.GC50.net.Requests.PlaceCardRequest;
import it.polimi.sw.GC50.net.util.*;
import it.polimi.sw.GC50.net.util.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * class that represents the client for the socket connection
 * it implements the ServerInterface and Runnable
 * it implements the run method that starts the client
 * it implements the methods to connect to the server and to communicate with the server
 */
public class ClientSCK implements ServerInterface {

    private final Client client;
    private final String serverIp;
    private final int serverPort;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final ExecutorService executorService;

    /////////////////////////////////////////////////////////////////
    private String nickname;
    private Map<String, List<String>> freeGames;
    private String gameId;

    /////////////////////////////////////////////////////////////////
    private final Object[] lock;
    private final Queue<NotifyMessage> queue;
    private final boolean[] condition;

    /**
     * constructor of the class
     *
     * @param client     is the client
     * @param serverIp   is the address of the server
     * @param serverPort is the port number
     * @throws IOException if an error occurs
     */
    public ClientSCK(Client client, String serverIp, String serverPort) throws IOException {
        this.client = client;
        this.serverIp = serverIp;
        this.serverPort = Integer.parseInt(serverPort);

        gameId = null;
        nickname = null;
        freeGames = new HashMap<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
        queue = new LinkedList<>();
        condition = new boolean[6];
        lock = new Object[6];
        for (int i = 0; i < 6; i++) {
            condition[i] = true;
            lock[i] = new Object();
        }
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    @Override
    public void connect() throws GameException {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.serverIp, this.serverPort), 1000);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            executorService.execute(() -> {
                while (!executorService.isShutdown()) {
                    try {
                        Object object = input.readObject();
                        NotifyMessage message = (NotifyMessage) object;
                        queue.add(message);
                        notifyMessageFromServer();

                    } catch (IOException | ClassNotFoundException e) {

                    }
                }
            });

            new Thread(() -> {
                while (true) {
                    waitMessageFromServer();
                    if (!queue.isEmpty()) {
                        switchMex(queue.poll());
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * method that sends a message to the server
     * the method writes the message to the output stream
     * the method flushes the output stream
     * the method resets the output stream
     * if an error occurs the method returns
     *
     * @param messageOut is the message to send to the server
     */
    private synchronized void setMessageOut(CommandMessage messageOut) throws GameException {
        try {
            output.writeObject(messageOut);
            output.flush();
            output.reset();
        } catch (IOException e) {
            throw new GameException("Connection error zio pera", e.getCause());
        }
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that send a message to the server to set the player name
     * it waits for notify from the server and returns the nickname of the player
     * if the name is not available the method returns null
     *
     * @param nickname is the nickname of the player
     */
    @Override
    public String setPlayer(String nickname) throws GameException {
        setMessageOut(new CommandMessage(Command.SET_PLAYER, nickname));
        waitSetupPhase();
        return this.nickname;
    }

    /**
     * method that sends a message to the server to reset the player
     */
    @Override
    public void resetPlayer() throws GameException {
        setMessageOut(new CommandMessage(Command.RESET_PLAYER, null));
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
        setMessageOut(new CommandMessage(Command.CREATE_GAME, new CreateGameRequest(gameId, numPlayers, endScore)));
        waitSetupPhase();
        return this.gameId != null;
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
        setMessageOut(new CommandMessage(Command.JOIN_GAME, gameId));
        waitSetupPhase();
        return this.gameId != null;
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
        setMessageOut(new CommandMessage(Command.LIST_FREE_GAMES, null));
        waitSetupPhase();
        return freeGames;
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that sends a message to the server to select the objective card of the player
     *
     * @param index is the index of the objective card
     * @throws GameException if an error occurs
     */
    @Override
    public void selectSecretObjective(int index) throws GameException {
        setMessageOut(new CommandMessage(Command.CHOOSE_OBJECTIVE, index));
    }

    /**
     * method that sends a message to the server to select the starter face of the player
     *
     * @param face is the face of the player
     * @throws GameException if an error occurs
     */
    @Override
    public void selectStarterFace(int face) throws GameException {
        setMessageOut(new CommandMessage(Command.CHOOSE_STARTER_FACE, face));
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that sends a message to the server to place a card
     *
     * @param placeCardRequest is the request to place a card
     * @throws GameException if an error occurs
     */
    @Override
    public void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        setMessageOut(new CommandMessage(Command.PLACE_CARD, placeCardRequest));
    }

    /**
     * method that sends a message to the server to draw a card
     *
     * @param position is the position of the card to draw
     * @throws GameException if an error occurs
     */
    @Override
    public void drawCard(int position) throws GameException {
        setMessageOut(new CommandMessage(Command.DRAW_CARD, position));
    }

    /**
     * method that sends a message to the server to send a chat message
     *
     * @param chatMessage is the message to send
     * @throws GameException if an error occurs
     */
    @Override
    public void sendChatMessage(ChatMessageRequest chatMessage) throws GameException {
        setMessageOut(new CommandMessage(Command.CHAT, chatMessage));
    }

    @Override
    public void leaveGame() throws GameException {
        setMessageOut(new CommandMessage(Command.LEAVE, null));
    }

    // UPDATES /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that switches the message received from the server
     * the method switches the message based on the notify
     * the method calls the update method of the client
     *
     * @param message is the message received from the server
     */
    private void switchMex(NotifyMessage message) {
        switch (message.getNotify()) {
            case NOTIFY_NAME_SET -> {
                nickname = ((StringMex) message.getContent()).getString();
                notifySetupPhase();
            }
            case NOTIFY_GAME_CREATED, NOTIFY_GAME_JOINED -> {
                gameId = ((StringMex) message.getContent()).getString();
                notifySetupPhase();
            }
            case NOTIFY_FREE_GAMES -> {
                freeGames = ((FreeGamesMex) message.getContent()).getFreeGames();
                notifySetupPhase();
            }
            default -> {
                client.update(message.getNotify(), message.getContent());
            }
        }
    }

    /**
     * method that wait for notify from the unlock method
     *
     * @param index is the index of the condition
     */
    private void lock(int index) {
        if (index > condition.length) {
            return;
        }
        synchronized (lock[index]) {
            while (condition[index]) {
                try {
                    lock[index].wait();
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
        if (index > condition.length) {
            return;
        }
        synchronized (lock[index]) {
            condition[index] = false;
            lock[index].notifyAll();
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
     * method that notify the method waitSetupPhase
     */
    private void notifySetupPhase() {
        unlock(1);
    }

    /**
     * method that wait for notify from the method notifySetupPhase
     */
    private void waitSetupPhase() {
        lock(1);
    }
}
