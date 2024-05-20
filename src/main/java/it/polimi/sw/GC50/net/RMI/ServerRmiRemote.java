package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.net.util.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerRmiRemote extends Remote {
    void start() throws RemoteException;

    void addClient(ClientInterface client) throws RemoteException;

    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////

    List<String> getFreeMatches() throws RemoteException;

    boolean createGame(ClientInterface clientInterface, int numOfPlayers, String gameId, String nickname) throws RemoteException;

    boolean joinGame(ClientInterface clientInterface, String gameId, String nickname) throws RemoteException;

    boolean setPlayer(ClientInterface clientInterface, String nickname) throws RemoteException;

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////
}
