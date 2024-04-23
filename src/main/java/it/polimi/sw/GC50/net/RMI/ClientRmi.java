package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.ClientInterface;
import it.polimi.sw.GC50.view.TypeOfView;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface {
    private ServerRmi serverRmi;
    private String name;
    private TypeOfView typeOfView;

    private int codePlayer;
    private int codeMatch;


    public  ClientRmi(String name) throws RemoteException {

        this.name=name;
    }
    public void connect() throws RemoteException {
        try {
            serverRmi=(ServerRmi) Naming.lookup(name);
            Thread ckConnection = new Thread(ckConnection(),"ckConnection");
            ckConnection.start();
        } catch (Exception e) {}
    }
    public void lobby(){

    }
    public void myTurn(){

    }
    public void placeCard(){

    }

    public void waiting()throws InterruptedException{}
    public Runnable ckConnection(){
        return null;
    }

    @Override
    public void ping() throws RemoteException {

    }
}
