package it.polimi.sw.gianpaolocugola50.net.rMi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RmiServerImplementation extends UnicastRemoteObject implements RmiServerInterface {
    private ExecutorService executorService;
    public RmiServerImplementation() throws RemoteException {
        super();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public String test(String a) throws RemoteException {
        System.out.println(a);
        return "Ciao Client";
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
