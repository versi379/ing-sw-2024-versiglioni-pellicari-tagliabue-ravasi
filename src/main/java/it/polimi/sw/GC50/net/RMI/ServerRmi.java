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

    void addClient(ClientInterface client) throws RemoteException;

    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////

    ArrayList<String> getFreeMatch() throws RemoteException;

    String createGame(int numOfPl, String gameName, ClientInterface clientInterface,String nickname) throws RemoteException;

    String enterGame(String gameName, ClientInterface clientInterface, String nickName) throws RemoteException;

    String setName(ClientInterface clientInterface,String name) throws RemoteException;

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    void message(Request request, Object object,String gameName, String nickName, ClientInterface clientInterface ) throws RemoteException;

    Object getModel(String gameName, String nickName, ClientInterface clientInterface, Request request, Object object) throws RemoteException;

}
