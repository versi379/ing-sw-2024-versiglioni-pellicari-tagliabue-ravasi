package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
        try{
            registry = LocateRegistry.createRegistry(this.port);
            registry.rebind("server", serverRmi);
        } catch (Exception e){
            System.err.println("bind to RMI server is failed");
        }
    }
}
