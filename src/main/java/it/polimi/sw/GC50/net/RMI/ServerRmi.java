package it.polimi.sw.GC50.net.RMI;

import com.google.gson.Gson;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.View;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRmi extends Remote {
    void start(ServerRmi serverRmi) throws RemoteException;

    int addClient(ClientInterface client) throws RemoteException;

    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////

    ArrayList<String> getFreeMatch() throws RemoteException;

    void createGame(int numOfPl, String gameName, ClientInterface clientInterface, View view) throws RemoteException;

    void enterGame(String gameName, ClientInterface clientInterface, String nickName) throws RemoteException;

    boolean setName(String name) throws RemoteException;

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    Request message(String gameName, String nickName, ClientInterface clientInterface, Request request, Object object) throws RemoteException;

    Object getModel(String gameName, String nickName, ClientInterface clientInterface, Request request, Object object) throws RemoteException;


}
