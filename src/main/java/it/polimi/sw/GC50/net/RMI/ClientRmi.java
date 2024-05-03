package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.observ.Observer;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.RequestFromClietToServer;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface, RequestFromClietToServer {
    private ServerRmi serverRmi;
    private String servername;
    //////////////////////////////////////////
    private String nickName;
    private TypeOfView typeOfView;
    private View view;
    private String gameName;
    private ArrayList<String> freeMatch;
    ///////////////////////////////////////////


    public ClientRmi(String name) throws RemoteException {
        this.servername = name;
        this.connection();
        this.freeMatch = new ArrayList<>();

    }
    //////////////////////////////////////////
    //COMUNICATION WITH SERVER
    ///////////////////////////////////////////

    public void connection() throws RemoteException {
        try {
            this.serverRmi = (ServerRmi) Naming.lookup(servername);
            this.serverRmi.addClient(this);
            System.out.println("Connected to server");

        } catch (Exception e) {

            System.out.println("Error in connection");
        }

    }


    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////
    public void addView(View view, TypeOfView typeOfView) {
        this.view = view;
        this.typeOfView = typeOfView;
    }

    @Override
    public String createGame(String matchName, int numberOfPlayer) {
        try {
            this.gameName = this.serverRmi.createGame(numberOfPlayer, matchName, this, this.nickName);
        } catch (RemoteException e) {
            return null;
        }
        return this.gameName;
    }

    @Override
    public String enterGame(String matchName) {
        try {
            this.gameName = this.serverRmi.enterGame(matchName, this, this.nickName);
        } catch (RemoteException e) {
            return null;
        }
        return this.gameName;
    }

    @Override
    public String setName(String name) {
        try {
            this.nickName = this.serverRmi.setName(this, name);
        } catch (RemoteException e) {
            return null;
        }
        return this.nickName;
    }

    @Override
    public ArrayList<String> getFreeMatch() {
        try {
            this.freeMatch = this.serverRmi.getFreeMatch();
        } catch (RemoteException e) {
            return null;
        }
        return this.freeMatch;
    }
    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    public void placeCard() {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void selectStarterFace() {

    }

    @Override
    public void selectObjectiveCard() {

    }

    @Override
    public void drawCard() {

    }

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public void waitNotifyModelChangedFromServer() {

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
