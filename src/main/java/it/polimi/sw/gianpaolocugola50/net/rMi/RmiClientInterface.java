package it.polimi.sw.gianpaolocugola50.net.rMi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {

    void inviaMessaggioAlServer(String message) throws RemoteException;

}
