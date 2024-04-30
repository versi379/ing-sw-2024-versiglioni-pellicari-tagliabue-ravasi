package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface {
    private ServerRmi serverRmi;
    private String name;
    private String nickName;
    private TypeOfView typeOfView;
    private View view;
    private int id;
    private int codeMatch;



    public ClientRmi(String name) throws RemoteException {
        this.name = name;
        nickName="Luca";
        connect();
    }

    public void connect() throws RemoteException {
        try {
            this.serverRmi = (ServerRmi) Naming.lookup(name);
            this.id = this.serverRmi.addClient(this);
            Thread ckConnection = new Thread(ckConnection(), "ckConnection");
            ckConnection.start();

        } catch (Exception e) {
        }
    }

    public void lobby() {
        try {
            serverRmi.createGame(2, "dio", this, view);


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void joinGame(String gameName) {
        // this.serverRmi.

    }

    public void createGame() {

    }

    public void addView(View view, TypeOfView typeOfView) {
        this.view = view;
        this.typeOfView = typeOfView;

    }

    public void myTurn() {


    }

    public void placeCard() {

    }

    public void waiting() throws InterruptedException {
    }

    public Runnable ckConnection() {
        return null;
    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void message(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public String getNickName() throws RemoteException {
        return nickName;
    }

    @Override
    public void message(Object o) throws RemoteException {
        System.out.println(o.toString());
    }
}
