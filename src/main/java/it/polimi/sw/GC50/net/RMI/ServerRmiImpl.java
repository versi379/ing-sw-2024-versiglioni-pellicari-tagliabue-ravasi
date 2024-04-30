package it.polimi.sw.GC50.net.RMI;

import com.google.gson.Gson;
import it.polimi.sw.GC50.net.util.ClientInterface;
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

    public ServerRmiImpl(Server server, int port) throws RemoteException {
        this.server = server;
        this.port = port;
    }

    @Override
    public void start(ServerRmi serverRmi) throws RemoteException {
        try {
            registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", serverRmi);
        } catch (Exception e) {
            System.err.println("");
        }
    }

    @Override
    public int addClient(ClientInterface client) {
        System.out.println("client connected");
        int id = server.connect(client);
        return id;
    }

    @Override
    public ArrayList<String> getFreeMatch() throws RemoteException {
        return server.getFreeMatchesNames();
    }

    @Override
    public void createGame(int numOfPl, String gameName, ClientInterface clientInterface, View view) throws RemoteException {
        server.createMatch(clientInterface, numOfPl, gameName, view);

    }

    @Override
    public void enterGame(String gameName, ClientInterface clientInterface, View view) throws RemoteException {
        server.enterMatch(gameName, clientInterface, view);
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    @Override
    public Request message(String gameName, ClientInterface clientInterface, Request request, Gson gson) throws RemoteException {
        return null;
    }

    @Override
    public Gson getModel(String gameName, ClientInterface clientInterface, Request request, Gson gson) throws RemoteException {
        return null;
    }

}
