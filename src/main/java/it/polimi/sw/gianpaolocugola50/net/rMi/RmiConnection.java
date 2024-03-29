package it.polimi.sw.gianpaolocugola50.net.rMi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiConnection extends Remote {
    RmiServerInterface registerClient(RmiClientInterface rmiClientInterface) throws RemoteException;
}
