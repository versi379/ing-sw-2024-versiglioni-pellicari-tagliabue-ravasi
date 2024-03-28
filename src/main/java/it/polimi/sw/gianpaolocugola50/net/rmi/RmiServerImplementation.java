package it.polimi.sw.gianpaolocugola50.net.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiServerImplementation extends UnicastRemoteObject implements RmiServerInterface {
    public RmiServerImplementation() throws RemoteException {
        super();
    }

    public void inviaMessaggioAlClient(String a) throws RemoteException {
        System.out.println("Messaggio dal server al client: ");
    }




}
