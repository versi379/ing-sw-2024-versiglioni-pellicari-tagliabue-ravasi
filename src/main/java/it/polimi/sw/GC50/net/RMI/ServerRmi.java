package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRmi extends Remote {
    public void start( ServerRmi serverRmi) throws RemoteException;
    public void addClient(ClientInterface client)throws RemoteException;
}
