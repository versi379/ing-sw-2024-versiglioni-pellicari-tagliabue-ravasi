package it.polimi.sw.gianpaolocugola50.net.rmi;

import it.polimi.sw.gianpaolocugola50.net.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {

    public void onCommonGoalAchieved() throws RemoteException;
}
