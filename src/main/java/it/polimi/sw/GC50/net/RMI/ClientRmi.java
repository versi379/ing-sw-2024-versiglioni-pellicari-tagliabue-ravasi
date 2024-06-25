package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.net.Client;
import it.polimi.sw.GC50.net.ClientInterface;
import it.polimi.sw.GC50.net.GameException;
import it.polimi.sw.GC50.net.messages.*;
import it.polimi.sw.GC50.net.requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.requests.PlaceCardRequest;
import it.polimi.sw.GC50.net.ServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * Class for client RMI
 */
public class ClientRmi extends UnicastRemoteObject implements ServerInterface, ClientInterface {
    private final Client client;
    private final String serverIp;
    private final String serverPort;
    private ServerRmiRemote serverRmi;
    private GameControllerRemote gameController;

    /**
     * Constructs an interface of RMI client
     *
     * @param client     name of the client
     * @param serverIp   IP of the server
     * @param serverPort port of the server
     * @throws RemoteException if there is an error
     */
    public ClientRmi(Client client, String serverIp, String serverPort) throws RemoteException {
        this.client = client;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        serverRmi = null;
        gameController = null;
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that connect RMI client to RMI server
     */
    @Override
    public boolean connect() {
        try {
            serverRmi = (ServerRmiRemote) Naming.lookup("rmi://" + serverIp + ":" + serverPort + "/server");
            serverRmi.addClient(this);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            return false;
        }
        return true;
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that set a new player
     *
     * @param nickname of the player
     * @return nickname of the player
     * @throws GameException if there is a connection error
     */
    @Override
    public String setPlayer(String nickname) throws GameException {
        try {
            return serverRmi.setPlayer(this, nickname);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * method that resets a player
     *
     * @throws GameException if there is a connection error
     */
    @Override
    public void resetPlayer() throws GameException {
        try {
            serverRmi.resetPlayer(this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * method that creates a new game
     *
     * @param gameId     id of the game
     * @param numPlayers number of players
     * @param endScore   final score
     * @return gameController when is != null
     * @throws GameException if there is a connection error
     */
    @Override
    public boolean createGame(String gameId, int numPlayers, int endScore) throws GameException {
        try {
            gameController = serverRmi.createGame(this, gameId, numPlayers, endScore);
            return gameController != null;
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * method that join a client to a new game
     *
     * @param gameId id of the game
     * @return gameController != null
     * @throws GameException if there is a connection error
     */
    @Override
    public boolean joinGame(String gameId) throws GameException {
        try {
            gameController = serverRmi.joinGame(this, gameId);
            return gameController != null;
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * @return a map of free games
     * @throws GameException if there is a connection error
     */
    @Override
    public Map<String, List<String>> getFreeGames() throws GameException {
        try {
            return serverRmi.getFreeGames();
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that select secret objective
     *
     * @param index of the card
     * @throws GameException if there is a connection error
     */
    @Override
    public void selectSecretObjective(int index) throws GameException {
        try {
            gameController.selectSecretObjective(this, index);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * method that select starter face
     *
     * @param face of the cars
     * @throws GameException if there is a connection error
     */
    @Override
    public void selectStarterFace(int face) throws GameException {
        try {
            gameController.selectStarterFace(this, face);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method used to place a card
     *
     * @param placeCardRequest request of place a card
     * @throws GameException if there is a connection error
     */
    @Override
    public void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        try {
            gameController.placeCard(this, placeCardRequest);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * method used to draw a card
     *
     * @param position of the card
     * @throws GameException if there is a connection error
     */
    @Override
    public void drawCard(int position) throws GameException {
        try {
            gameController.drawCard(this, position);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * method used to send a chat message
     *
     * @param message that is sent
     * @throws GameException if there is a connection error
     */
    @Override
    public void sendChatMessage(ChatMessageRequest message) throws GameException {
        try {
            gameController.sendChatMessage(this, message);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    /**
     * method for leave a game
     *
     * @throws GameException if there is a connection error
     */
    @Override
    public void leaveGame() throws GameException {
        try {
            gameController.leaveGame(this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // OBSERVER ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void ping() throws RemoteException {
    }

    @Override
    public void update(Notify notify, Message message) {
        client.update(notify, message);
    }
}
