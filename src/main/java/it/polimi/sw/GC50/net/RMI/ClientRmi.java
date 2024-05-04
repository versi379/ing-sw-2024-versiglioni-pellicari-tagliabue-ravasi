package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientRmi extends UnicastRemoteObject implements Serializable, ClientInterface {
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


    public String createGame(String matchName, int numberOfPlayer) {
        try {
            this.gameName = this.serverRmi.createGame(numberOfPlayer, matchName, this, this.nickName);
        } catch (RemoteException e) {
            return null;
        }
        return this.gameName;
    }


    public String enterGame(String matchName) {
        try {
            this.gameName = this.serverRmi.enterGame(matchName, this, this.nickName);
        } catch (RemoteException e) {
            return null;
        }
        return this.gameName;
    }


    public String setName(String name) {
        try {
            this.nickName = this.serverRmi.setName(this, name);
        } catch (RemoteException e) {
            return null;
        }
        return this.nickName;
    }


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

    public void placeCard(boolean face, int index, int x, int y) {
        if (this.gameName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        try {
            this.serverRmi.message(Request.PLACE_CARD, new PlaceCardMex(face, index, x, y), this.gameName, this.nickName, this);
        } catch (RemoteException e) {
            return;
        }

    }


    public void sendMessage(String message) {
        if (this.gameName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        try {
            this.serverRmi.message(Request.MEX_CHAT, message, this.gameName, this.nickName, this);
        } catch (RemoteException e) {
            return;
        }

    }


    public void selectStarterFace() {

    }


    public void selectObjectiveCard() {

    }


    public void drawCard() {

    }


    public Object getModel() {
        return null;
    }


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
        System.out.println("Update from server");
    }

    @Override
    public void onUpdate(Message message) throws RemoteException {
        switch (message.getRequest()) {
            case NOTIFY_GAME_SETUP: {
                System.out.println("all players joined");
                break;
            }
            case NOTIFY_CARD_PLACED: {
                break;

            }
            case NOTIFY_PLAYER_JOINED_GAME: {
                break;
            }
            case NOTIFY_PLAYER_LEFT_GAME: {
                break;
            }
            case NOTIFY_PLAYER_READY: {
                break;
            }
            case NOTIFY_GAME_STARTED: {
                break;
            }
            case NOTIFY_CHAT_MESSAGE: {
                break;
            }
            case NOTIFY_ALL_PLAYER_JOINED_THE_GAME: {
                break;
            }
            case NOTIFY_CARD_NOT_FOUND: {
                break;
            }
            case NOTIFY_CARD_NOT_PLACEABLE: {
                break;
            }
            case NOTIFY_NOT_YOUR_PLACING_PHASE: {
                break;
            }
            case NOTIFY_OPERATION_NOT_AVAILABLE: {
                break;
            }
            case NOTIFY_INVALID_INDEX: {
                break;
            }
            case NOTIFY_POSITION_DRAWING_NOT_AVAILABLE: {
                break;
            }
            case GET_MODEL_RESPONSE: {
                break;
            }
            case GET_CHAT_MODEL_RESPONSE: {
                System.out.println("chat");
                break;
            }

        }
        System.out.println("Update from server");
    }

    public void lobby() {
    }
}
