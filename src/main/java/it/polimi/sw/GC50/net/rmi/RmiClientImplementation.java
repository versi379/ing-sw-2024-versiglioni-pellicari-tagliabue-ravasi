package it.polimi.sw.GC50.net.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClientImplementation extends UnicastRemoteObject implements RmiClientInterface {

    public RmiClientImplementation() throws RemoteException {
        super();
    }


    @Override
    public void sendMessage(String message) throws RemoteException {
    }

    @Override
    public void achievedSecretGoal() throws RemoteException {

    }

    @Override
    public void achievedTwenty() throws RemoteException {

    }

    @Override
    public void achievedCommonGoal() throws RemoteException {

    }
}

