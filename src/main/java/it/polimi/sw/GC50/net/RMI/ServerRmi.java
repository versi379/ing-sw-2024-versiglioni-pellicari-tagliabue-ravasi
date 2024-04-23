package it.polimi.sw.GC50.net.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRmi extends Remote {
    public void start( ServerRmi serverRmi) throws RemoteException;
}
