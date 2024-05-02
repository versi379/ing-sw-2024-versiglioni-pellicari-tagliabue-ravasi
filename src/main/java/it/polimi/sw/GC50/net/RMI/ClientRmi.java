package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.observ.Observer;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface{
    private ServerRmi serverRmi;
    private String servername;
    private String nickName;
    private TypeOfView typeOfView;
    private View view;
    private String gameName;


    public ClientRmi(String name) throws RemoteException {

        this.servername = name;
        this.connection();
    }

    public void connection() throws RemoteException {
        try {
            this.serverRmi = (ServerRmi) Naming.lookup(servername);
            this.serverRmi.addClient(this);
            System.out.println("Connected to server");

        } catch (Exception e) {
            System.out.println("Error in connection");
        }

    }

    public void lobby() {
        try {
            //setNickName();
             serverRmi.createGame(2, "test1", this, "luca");

            for (String freeMatch : serverRmi.getFreeMatch()) {
                System.out.println(freeMatch);
            }


        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void setNickName() throws RemoteException {
        String name2;
        do {
            name2 = view.askName();
        } while (!serverRmi.setName(this,name2));

        nickName = name2;
    }

    public void joinGame(String gameName) {
        try {
            serverRmi.enterGame("gameName", this, nickName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void createGame() {
        String gameName;
        do {
            gameName = view.askGameName();
        } while (gameName == null);
        int numOfPlayer = view.askNumberOfPlayer();
        try {
            serverRmi.createGame(numOfPlayer, gameName, this,nickName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void addView(View view, TypeOfView typeOfView) {
        this.view = view;
        this.typeOfView = typeOfView;
    }

    public void myTurn() {


    }

    public void placeCard() {

    }




    @Override
    public void ping() throws RemoteException {

    }


    //////////////////////////////////////////
    //OBSERVER
    ///////////////////////////////////////////

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void onUpdate(Message message) {

    }
}
