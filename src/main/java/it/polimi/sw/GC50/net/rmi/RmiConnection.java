package it.polimi.sw.GC50.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiConnection extends Remote {
    RmiServerInterface registerClient(RmiClientInterface rmiClientInterface) throws RemoteException;
}
