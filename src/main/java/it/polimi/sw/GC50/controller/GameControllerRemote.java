package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.net.util.ChatMessageRequest;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameControllerRemote extends Remote {
    void selectSecretObjective(ClientInterface clientInterface, int index) throws RemoteException;
    void selectStarterFace(ClientInterface clientInterface, int face) throws RemoteException;
    void placeCard(ClientInterface clientInterface, PlaceCardRequest placeCardRequest) throws RemoteException;
    void drawCard(ClientInterface clientInterface, int position) throws RemoteException;
    void sendChatMessage(ClientInterface clientInterface, ChatMessageRequest message) throws RemoteException;

    // INUTILE ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //void updateController(ClientInterface clientInterface, Object update, Request request) throws RemoteException;

    //Object getModel(ClientInterface clientInterface) throws RemoteException;
}
