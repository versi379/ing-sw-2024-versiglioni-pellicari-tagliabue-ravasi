package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.net.client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ServerRmiRemote extends Remote {
    void start() throws RemoteException;

    void addClient(ClientInterface client) throws RemoteException;

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    String setPlayer(ClientInterface clientInterface, String nickname) throws RemoteException;

    void resetPlayer(ClientInterface clientInterface) throws RemoteException;

    GameControllerRemote createGame(ClientInterface clientInterface, String gameId, int numOfPlayers, int endScore) throws RemoteException;

    GameControllerRemote joinGame(ClientInterface clientInterface, String gameId) throws RemoteException;

    Map<String, List<String>> getFreeGames() throws RemoteException;
}
