package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Lobby;
import it.polimi.sw.GC50.net.util.Match;
import it.polimi.sw.GC50.net.util.Request;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerRmiImpl extends UnicastRemoteObject implements ServerRmi {
    private final Lobby lobby;
    private final int port;
    private Registry registry;
    private Match match;

    public ServerRmiImpl(Lobby lobby, int port) throws RemoteException {
        this.lobby = lobby;
        this.port = port;
    }

    @Override
    public void start() throws RemoteException {
        try {
            registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", this);
            System.out.println("Server RMI ready");
        } catch (Exception e) {
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
        match = lobby.createMatch(clientInterface, gameId, numOfPlayers, nickname);
        return match != null;
    }

    @Override
    public boolean joinGame(ClientInterface clientInterface, String gameId, String nickname) throws RemoteException {
        match = lobby.joinMatch(clientInterface, gameId, nickname);
        return match != null;
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////
    @Override
    public void message(Request request, Object object, ClientInterface clientInterface) throws RemoteException {
        switch (request) {
            case SELECT_STARTER_FACE, SELECT_OBJECTIVE_CARD, PLACE_CARD, DRAW_CARD:
                match.updateController(request, object, clientInterface);
                break;
            case MEX_CHAT:
                match.updateChat((String) object, clientInterface);
                break;
            default:
                break;
        }
    }

    @Override
    public Object getModel(Request request, Object object, ClientInterface clientInterface) throws RemoteException {
        System.out.println("getModel");
        return match.getModel(clientInterface);
    }
}
