package it.polimi.sw.GC50.net;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    public void ping() throws RemoteException;
}
