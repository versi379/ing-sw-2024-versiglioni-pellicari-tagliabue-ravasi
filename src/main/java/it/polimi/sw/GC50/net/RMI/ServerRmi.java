package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.model.lobby.Lobby;
import it.polimi.sw.GC50.net.util.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class ServerRmi extends UnicastRemoteObject implements ServerRmiRemote {
    private final Lobby lobby;
    private final int port;

    public ServerRmi(Lobby lobby, int port) throws RemoteException {
        this.lobby = lobby;
        this.port = port;
    }

    @Override
    public void start() throws RemoteException {

        try {
            LocateRegistry.createRegistry(this.port).rebind("server", this);
            System.out.println("Server RMI ready");
        } catch (RemoteException e) {
            System.err.println("Error in binding to RMI registry");
        }
    }

    @Override
    public void addClient(ClientInterface client) throws RemoteException {
        System.out.println("Client connected");
    }


    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String setPlayer(ClientInterface clientInterface, String nickname) throws RemoteException {
        return lobby.addPlayer(clientInterface, nickname);
    }

    @Override
    public void resetPlayer(ClientInterface clientInterface) throws RemoteException {
        lobby.removePlayer(clientInterface);
    }

    @Override
    public GameControllerRemote createGame(ClientInterface clientInterface, String gameId, int numOfPlayers, int endScore) throws RemoteException {
        return lobby.createGame(clientInterface, gameId, numOfPlayers, endScore);
    }

    @Override
    public GameControllerRemote joinGame(ClientInterface clientInterface, String gameId) throws RemoteException {
        return lobby.joinGame(clientInterface, gameId);
    }

    @Override
    public Map<String, List<String>> getFreeGames() throws RemoteException {
        return lobby.getFreeGames();
    }
}
