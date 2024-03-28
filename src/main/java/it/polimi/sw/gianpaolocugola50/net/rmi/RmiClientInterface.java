package it.polimi.sw.gianpaolocugola50.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {

    void inviaMessaggioAlServer(String message) throws RemoteException;

}
