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

    ArrayList<String> getFreeMatch() throws RemoteException;

    void createGame(int numOfPl, String gameName, ClientInterface clientInterface, View view) throws RemoteException;

    void enterGame(String gameName, ClientInterface clientInterface, View view) throws RemoteException;

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    Request message(String gameName, ClientInterface clientInterface, Request request, Gson gson) throws RemoteException;

    Gson getModel(String gameName, ClientInterface clientInterface, Request request, Gson gson) throws RemoteException;


}
