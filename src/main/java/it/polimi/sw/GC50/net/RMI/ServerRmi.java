package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Request;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRmi extends Remote {
    void start() throws RemoteException;

    void addClient(ClientInterface client) throws RemoteException;

    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////

    ArrayList<String> getFreeMatches() throws RemoteException;

    String createGame(String gameId, int numOfPlayers, ClientInterface clientInterface, String nickname) throws RemoteException;

    String enterGame(String gameId, ClientInterface clientInterface, String nickname) throws RemoteException;

    String setNickname(ClientInterface clientInterface, String name) throws RemoteException;

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    void message(Request request, Object object, ClientInterface clientInterface) throws RemoteException;

    Object getModel(Request request, Object object, ClientInterface clientInterface) throws RemoteException;
}
