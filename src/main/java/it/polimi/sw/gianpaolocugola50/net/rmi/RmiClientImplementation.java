package it.polimi.sw.gianpaolocugola50.net.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClientImplementation extends UnicastRemoteObject implements RmiClientInterface {

    public RmiClientImplementation() throws RemoteException {
        super();
    }

    @Override
    public void inviaMessaggioAlServer(String message) throws RemoteException {
        System.out.println("Messaggio dal client al server: " + message);
    }
    public void riceviMessaggioDalServer(String message) throws RemoteException {
        System.out.println("Messaggio dal server al client: " + message);
    }

}

