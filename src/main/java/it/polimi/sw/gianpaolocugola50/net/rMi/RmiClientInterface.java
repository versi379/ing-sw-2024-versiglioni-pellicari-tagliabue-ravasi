package it.polimi.sw.gianpaolocugola50.net.rMi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {

    void sendMessage(String message) throws RemoteException;
    void achievedSecretGoal() throws RemoteException;
    void achievedTwenty()throws RemoteException;
    void achievedCommonGoal()throws RemoteException;
}
