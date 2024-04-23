package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.ClientInterface;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientRmi extends UnicastRemoteObject implements ClientInterface {
    private ServerRmi serverRmi;
    private String name;

    private int codePlayer;
    private int codeMatch;



    public void ClientRmi(String name) throws RemoteException {
        this.name=name;
    }
    public void connect() throws RemoteException {
        try {
            serverRmi=(ServerRmi) Naming.lookup(name);

           // ArrayList<Integer> indexFromServer;
            //indexFromServer = server.initClient(this);
            //this.myIndex = indexFromServer.get(1);
           // this.matchIndex = indexFromServer.get(0);
            Thread ckConnection = new Thread(ckConnection(),"ckConnection");
            ckConnection.start();
        } catch (Exception e) {}
    }
    public void waiting()throws InterruptedException{}
    public Runnable ckConnection(){

        return null;
    }

    @Override
    public void ping() throws RemoteException {

    }
}
