package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Request;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameControllerRemote extends Remote {

    // RECEIVE REQUESTS  ///////////////////////////////////////////////////////////////////////////////////////////////
    void updateController(Request request, Object update, ClientInterface clientInterface) throws RemoteException;

    void updateChat(String message, ClientInterface clientInterface) throws RemoteException;


    // MODEL MEX ///////////////////////////////////////////////////////////////////////////////////////////////////////
    Object getModel(ClientInterface clientInterface) throws RemoteException;
}
