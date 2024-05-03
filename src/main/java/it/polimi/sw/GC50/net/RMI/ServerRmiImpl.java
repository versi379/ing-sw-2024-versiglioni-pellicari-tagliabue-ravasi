package it.polimi.sw.GC50.net.RMI;

import com.google.gson.Gson;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Match;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.net.util.Server;
import it.polimi.sw.GC50.view.View;

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
        this.match = server.createMatch(clientInterface, numOfPl, gameName, nickName);
        if (this.match == null) {
            return null;
        }
        return this.match.getName();

    }

    @Override
    public String enterGame(String gameName, ClientInterface clientInterface, String nickName) throws RemoteException {
        this.match = server.enterMatch(gameName, clientInterface, nickName);
        if (this.match == null) {
            return null;
        }
        return this.match.getName();
    }


    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    @Override
    public Request message(String gameName, String nickName, ClientInterface clientInterface, Request request, Object object) throws RemoteException {
        if (match.getName().equals(gameName)) {
            match.update(nickName, clientInterface, request, object);
        }
        return null;
    }

    @Override
    public Object getModel(String gameName, String nickName, ClientInterface clientInterface, Request request, Object object) throws RemoteException {
        return match.getModel(clientInterface, request, object);
    }


}
