package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.model.lobby.Lobby;
import it.polimi.sw.GC50.net.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class ServerRmi extends UnicastRemoteObject implements ServerRmiRemote {
    private final Lobby lobby;
    private final int port;

    /**
     * method that creates a new server RMI
     *
     * @param lobby is the lobby of the server
     * @param port  is the port of the server
     */
    public ServerRmi(Lobby lobby, int port) throws RemoteException {
        this.lobby = lobby;
        this.port = port;
    }

    /**
     * method that starts RMI server
     */
    public void run() {
        try {
            LocateRegistry.createRegistry(this.port).rebind("server", this);
            System.out.println("Server RMI ready");
        } catch (RemoteException e) {
            System.err.println("Error in binding to RMI registry");
        }
    }

    /**
     * method that adds a new client
     *
     * @param client client added
     * @throws RemoteException if there is an error in binding to RMI registry
     */
    @Override
    public void addClient(ClientInterface client) throws RemoteException {
        System.out.println("Client connected");
    }


    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that sets a new player
     *
     * @param clientInterface interface that represents client
     * @param nickname        of the player
     * @return lobby with the new player
     * @throws RemoteException if there is an error in binding to RMI registry
     */
    @Override
    public String setPlayer(ClientInterface clientInterface, String nickname) throws RemoteException {
        return lobby.addPlayer(clientInterface, nickname);
    }

    /**
     * method that resets a new player
     *
     * @param clientInterface   interface that represents client
     * @throws RemoteException if there is an error in binding to RMI registry
     */
    @Override
    public void resetPlayer(ClientInterface clientInterface) throws RemoteException {
        lobby.removePlayer(clientInterface);
    }

    /**
     * method that creates a new game
     *
     * @param clientInterface   interface that represents client
     * @param gameId          id of the game
     * @param numOfPlayers    number of players
     * @param endScore        final score
     * @return id of the game
     * @throws RemoteException if there is an error in binding to RMI registry
     */
    @Override
    public GameControllerRemote createGame(ClientInterface clientInterface, String gameId, int numOfPlayers, int endScore) throws RemoteException {
        return lobby.createGame(clientInterface, gameId, numOfPlayers, endScore);
    }

    /**
     * @param clientInterface   interface that represents client
     * @param gameId          id of the game
     * @return lobby with the game that we have joint
     * @throws RemoteException if there is an error in binding to RMI registry
     */
    @Override
    public GameControllerRemote joinGame(ClientInterface clientInterface, String gameId) throws RemoteException {
        return lobby.joinGame(clientInterface, gameId);
    }

    /**
     * @return a map of free games
     * @throws RemoteException if there is an error in binding to RMI registry
     */
    @Override
    public Map<String, List<String>> getFreeGames() throws RemoteException {
        return lobby.getFreeGames();
    }
}
