package it.polimi.sw.gianpaolocugola50.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RmiServerInterface extends Remote {
     void inviaMessaggioAlClient(String a) throws RemoteException;
}
