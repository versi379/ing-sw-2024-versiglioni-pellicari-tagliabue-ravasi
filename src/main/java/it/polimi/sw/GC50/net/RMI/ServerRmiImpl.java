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
    public void start(ServerRmi serverRmi) throws RemoteException {
        try {
            registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", serverRmi);
            System.out.println("Server RMI ready");
        } catch (Exception e) {
            System.err.println("error in binding to RMI registry");
        }

    }

    @Override
    public void addClient(ClientInterface client) {
        System.out.println("client connected");
        server.connect(client);

    }
    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////

    @Override
    public String setName(ClientInterface clientInterface, String name) {
        if (server.addName(clientInterface, name)) {
            return name;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<String> getFreeMatch() throws RemoteException {
        return server.getFreeMatchesNames();
    }

    @Override
    public String createGame(int numOfPl, String gameName, ClientInterface clientInterface, String nickName) throws RemoteException {
        this.match = server.createMatch(clientInterface, gameName, numOfPl, nickName);
        if (this.match == null) {
            return null;
        }
        return this.match.getGameId();
    }

    @Override
    public String enterGame(String gameName, ClientInterface clientInterface, String nickName) throws RemoteException {
        this.match = server.enterMatch(gameName, clientInterface, nickName);
        if (this.match == null) {
            return null;
        }
        return this.match.getGameId();
    }


    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    @Override
    public void message(Request request, Object object, String gameName, String nickName, ClientInterface clientInterface) throws RemoteException {
        if (match.getGameId().equals(gameName)) {
            if (request.equals(Request.MEXCHAT)) {
                match.updateChat(clientInterface, nickName, (String) object);
                System.out.println("chat updated");
            }
            switch (request) {
                case PLACE_CARD:
                    match.updateController(request, clientInterface, object, nickName);
                    break;
                case SELECT_STARTER_FACE:
                    match.updateController(request, clientInterface, object, nickName);
                    break;
                case SELECT_OBJECTIVE_CARD:
                    match.updateController(request, clientInterface, object, nickName);
                    break;
                case DRAW_CARD:
                    match.updateController(request, clientInterface, object, nickName);
                    break;
                default:
                    break;

            }
        }
    }

    @Override
    public Object getModel(String gameName, String nickName, ClientInterface clientInterface, Request request, Object object) throws RemoteException {
        if (!match.getGameId().equals(gameName)) {
            return null;
        }
        return match.getModel(nickName, clientInterface);
    }
}
