package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.model.lobby.Lobby;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerRmi extends UnicastRemoteObject implements ServerRmiRemote {
    private final Lobby lobby;
    private final int port;
    private Registry registry;

    public ServerRmi(Lobby lobby, int port) throws RemoteException {
        this.lobby = lobby;
        this.port = port;
        registry = null;
    }

    @Override
    public void start() {
        bindToRegistry(this, "server");
    }

    private void bindToRegistry(Remote remote, String name) {
        try {
            if (registry == null) {
                registry = LocateRegistry.createRegistry(this.port);
            }
            System.out.println("Binding remote " + remote.getClass() + " : " + name);
            registry.rebind(name, remote);
            System.out.println("Remote " + remote.getClass() + " : " + name + " bound");
        } catch (RemoteException e) {
            System.err.println("Error in binding to RMI registry");
        }
    }

    @Override
    public void addClient(ClientInterface client) {
        System.out.println("Client connected");
    }

    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////
    @Override
    public boolean setPlayer(ClientInterface clientInterface, String nickname) {
        return lobby.addPlayer(clientInterface, nickname);
    }

    @Override
    public List<String> getFreeMatches() throws RemoteException {
        return lobby.getFreeMatches();
    }

    @Override
    public boolean createGame(ClientInterface clientInterface, int numOfPlayers, String gameId, String nickname) throws RemoteException {
        GameControllerRemote gameController = lobby.createMatch(clientInterface, gameId, numOfPlayers, nickname);
        if (gameController != null) {
            bindToRegistry(gameController, gameId);
            return true;
        }
        return false;
    }

    @Override
    public boolean joinGame(ClientInterface clientInterface, String gameId, String nickname) throws RemoteException {
        return lobby.joinMatch(clientInterface, gameId, nickname) != null;
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////
}
