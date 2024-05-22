package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.net.util.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServerRmiRemote extends Remote {
    void start() throws RemoteException;

    void addClient(ClientInterface client) throws RemoteException;

    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////

    Map<String, List<String>> getFreeGames() throws RemoteException;

    GameControllerRemote createGame(ClientInterface clientInterface, String gameId, int numOfPlayers, int endScore, String nickname) throws RemoteException;

    GameControllerRemote joinGame(ClientInterface clientInterface, String gameId, String nickname) throws RemoteException;

    boolean setPlayer(ClientInterface clientInterface, String nickname) throws RemoteException;

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////
}
