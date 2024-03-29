package it.polimi.sw.gianpaolocugola50.net.rMi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClientImplementation extends UnicastRemoteObject implements RmiClientInterface {

    public RmiClientImplementation() throws RemoteException {
        super();
    }


    @Override
    public void sendMessage(String message) throws RemoteException {
        System.out.println("Messaggio dal client al server: " + message);
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

