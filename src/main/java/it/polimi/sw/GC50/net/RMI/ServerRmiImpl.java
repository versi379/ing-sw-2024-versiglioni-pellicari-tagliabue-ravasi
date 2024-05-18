package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Match;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.net.util.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerRmiImpl extends UnicastRemoteObject implements ServerRmi {
    private final Server server;
    private final int port;
    private Registry registry;
    private Match match;

    public ServerRmiImpl(Server server, int port) throws RemoteException {
        this.server = server;
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
        server.connect(client);
        System.out.println("Client connected");
    }

    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////
    @Override
    public String setNickname(ClientInterface clientInterface, String nickname) {
        if (server.addNickname(clientInterface, nickname)) {
            return nickname;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<String> getFreeMatches() throws RemoteException {
        return server.getFreeMatches();
    }

    @Override
    public String createGame(String gameId, int numOfPlayers, ClientInterface clientInterface, String nickname) throws RemoteException {
        this.match = server.createMatch(clientInterface, gameId, numOfPlayers, nickname);
        if (this.match == null) {
            return null;
        }
        return this.match.getGameId();
    }

    @Override
    public String enterGame(String gameId, ClientInterface clientInterface, String nickname) throws RemoteException {
        this.match = server.enterMatch(clientInterface, gameId, nickname);
        if (this.match == null) {
            return null;
        }
        return this.match.getGameId();
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
