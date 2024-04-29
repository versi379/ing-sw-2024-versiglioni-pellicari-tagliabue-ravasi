package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.ClientInterface;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface {
    private ServerRmi serverRmi;
    private String name;
    private TypeOfView typeOfView;
    private View view;
    private int id;

    private int codePlayer;
    private int codeMatch;


    public ClientRmi(String name) throws RemoteException {
        this.name = name;
        connect();
    }

    public void connect() throws RemoteException {
        try {
            this.serverRmi = (ServerRmi) Naming.lookup(name);
            this.id=this.serverRmi.addClient(this);
            Thread ckConnection = new Thread(ckConnection(), "ckConnection");
            ckConnection.start();

        } catch (Exception e) {
        }
    }

    public void lobby() {

    }
    public void addView(View view, TypeOfView typeOfView){
        this.view=view;
        this.typeOfView=typeOfView;

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
}
