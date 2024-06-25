package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.messages.FreeGamesMex;
import it.polimi.sw.GC50.net.messages.StringMex;
import it.polimi.sw.GC50.net.requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.requests.CreateGameRequest;
import it.polimi.sw.GC50.net.requests.PlaceCardRequest;
import it.polimi.sw.GC50.net.client.Client;
import it.polimi.sw.GC50.net.client.GameException;
import it.polimi.sw.GC50.net.ServerInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

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

    /////////////////////////////////////////////////////////////////
    private String nickname;
    private String gameId;
    private Map<String, List<String>> freeGames;

    /////////////////////////////////////////////////////////////////
    private final Object lobbyLock = new Object(); // Object for synchronization
    private boolean lobbyWaiting;

    /**
     * constructor of the class
     *
     * @param client     is the client
     * @param serverIp   is the address of the server
     * @param serverPort is the port number
     */
    public ClientSCK(Client client, String serverIp, String serverPort) {
        this.client = client;
        this.serverIp = serverIp;
        this.serverPort = Integer.parseInt(serverPort);

        gameId = null;
        nickname = null;
        freeGames = new HashMap<>();
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    @Override
    public boolean connect() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.serverIp, this.serverPort), 1000);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> {
                while (true) {
                    try {
                        switchMex((NotifyMessage) input.readObject());
                    } catch (IOException | ClassNotFoundException ignored) {
                    }
                }
            }).start();
        } catch (IOException e) {
            return false;
        }
        return true;
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
    private synchronized void setMessageOut(RequestMessage messageOut) throws GameException {
        try {
            output.writeObject(messageOut);
            output.flush();
            output.reset();
        } catch (IOException e) {
            throw new GameException("Connection error", e.getCause());
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
        setMessageOut(new RequestMessage(Request.SET_PLAYER, nickname));
        lobbyWait();
        return this.nickname;
    }

    /**
     * method that sends a message to the server to reset the player
     */
    @Override
    public void resetPlayer() throws GameException {
        setMessageOut(new RequestMessage(Request.RESET_PLAYER, null));
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
        setMessageOut(new RequestMessage(Request.CREATE_GAME, new CreateGameRequest(gameId, numPlayers, endScore)));
        lobbyWait();
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
        setMessageOut(new RequestMessage(Request.JOIN_GAME, gameId));
        lobbyWait();
        return this.gameId != null;
    }

    /**
     * method that sends a message to the server get the list of free games
     * it waits for notify from the server and returns the list of free games
     *
     * @return a map
     * @throws GameException if an error occurs
     */
    @Override
    public Map<String, List<String>> getFreeGames() throws GameException {
        setMessageOut(new RequestMessage(Request.LIST_FREE_GAMES, null));
        lobbyWait();
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
        setMessageOut(new RequestMessage(Request.CHOOSE_OBJECTIVE, index));
    }

    /**
     * method that sends a message to the server to select the starter face of the player
     *
     * @param face is the face of the player
     * @throws GameException if an error occurs
     */
    @Override
    public void selectStarterFace(int face) throws GameException {
        setMessageOut(new RequestMessage(Request.CHOOSE_STARTER_FACE, face));
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
        setMessageOut(new RequestMessage(Request.PLACE_CARD, placeCardRequest));
    }

    /**
     * method that sends a message to the server to draw a card
     *
     * @param position is the position of the card to draw
     * @throws GameException if an error occurs
     */
    @Override
    public void drawCard(int position) throws GameException {
        setMessageOut(new RequestMessage(Request.DRAW_CARD, position));
    }

    /**
     * method that sends a message to the server to send a chat message
     *
     * @param chatMessage is the message to send
     * @throws GameException if an error occurs
     */
    @Override
    public void sendChatMessage(ChatMessageRequest chatMessage) throws GameException {
        setMessageOut(new RequestMessage(Request.CHAT, chatMessage));
    }

    @Override
    public void leaveGame() throws GameException {
        setMessageOut(new RequestMessage(Request.LEAVE, null));
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
                lobbyNotify();
            }
            case NOTIFY_GAME_CREATED, NOTIFY_GAME_JOINED -> {
                gameId = ((StringMex) message.getContent()).getString();
                lobbyNotify();
            }
            case NOTIFY_FREE_GAMES -> {
                freeGames = ((FreeGamesMex) message.getContent()).getFreeGames();
                lobbyNotify();
            }
            default -> {
                client.update(message.getNotify(), message.getContent());
            }
        }
    }

    /**
     * method that wait for notify from the method lobbyNotify
     */
    private void lobbyWait() {
        lobbyWaiting = true;

        synchronized (lobbyLock) {
            while (lobbyWaiting) {
                try {
                    lobbyLock.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * method that notify the method lobbyWait
     */
    private void lobbyNotify() {
        synchronized (lobbyLock) {
            lobbyWaiting = false;
            lobbyLock.notifyAll();
        }
    }
}
