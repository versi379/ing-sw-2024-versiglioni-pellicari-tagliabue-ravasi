package it.polimi.sw.gianpaolocugola50.net.rMi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServerImplementation extends UnicastRemoteObject implements RmiServerInterface {
    public RmiServerImplementation() throws RemoteException {
        super();
    }

    @Override
    public void sendMessage(String a) throws RemoteException {
        System.out.println("Messaggio dal server al client: ");

    }

    @Override
    public void joinGame(String nickName) throws RemoteException {

    }

    @Override
    public void quitGame(String nickName) throws RemoteException {

    }

    @Override
    public void createGame(String nickName, int numOfPlayer) throws RemoteException {

    }

    @Override
    public void placeCard() throws RemoteException {

    }

    @Override
    public void drawCard() throws RemoteException {

    }
}
