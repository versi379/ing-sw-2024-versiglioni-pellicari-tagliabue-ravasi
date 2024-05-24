package it.polimi.sw.GC50.net.RMI;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.net.Messages.*;
import it.polimi.sw.GC50.net.util.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class ClientRmi extends UnicastRemoteObject implements ServerInterface, ClientInterface {
    private final Client client;
    private final String serverIp;
    private final String serverPort;
    private ServerRmiRemote serverRmi;
    private GameControllerRemote gameController;

    public ClientRmi(Client client, String serverIp, String serverPort) throws RemoteException {
        this.client = client;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        serverRmi = null;
        gameController = null;
    }

    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    public void connect() throws GameException {
        try {
            serverRmi = (ServerRmiRemote) Naming.lookup("rmi://" + serverIp + ":" + serverPort + "/server");
            serverRmi.addClient(this);
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new GameException("Error in connection to the server", e.getCause());
        }
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String setPlayer(String nickname) throws GameException {
        try {
            return serverRmi.setPlayer(this, nickname);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    public void resetPlayer() throws GameException {
        try {
            serverRmi.resetPlayer(this);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    public boolean createGame(String gameId, int numPlayers, int endScore) throws GameException {
        try {
            gameController = serverRmi.createGame(this, gameId, numPlayers, endScore);
            return gameController != null;
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    public boolean joinGame(String gameId) throws GameException {
        try {
            gameController = serverRmi.joinGame(this, gameId);
            return gameController != null;
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    public Map<String, List<String>> getFreeGames() throws GameException {
        try {
            return serverRmi.getFreeGames();
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void selectObjectiveCard(int index) throws GameException {
        try {
            gameController.selectObjectiveCard(this, index);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    public void selectStarterFace(int face) throws GameException {
        try {
            gameController.selectStarterFace(this, face);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void placeCard(PlaceCardRequest placeCardRequest) throws GameException {
        try {
            gameController.placeCard(this, placeCardRequest);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    public void drawCard(int position) throws GameException {
        try {
            gameController.drawCard(this, position);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    public void sendChatMessage(String message) throws GameException {
        try {
            gameController.sendChatMessage(this, message);
        } catch (RemoteException e) {
            throw new GameException("Connection error", e.getCause());
        }
    }

    // OBSERVER ////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void ping() throws RemoteException {
    }

    @Override
    public void update(Request request, Message message) {
        client.update(request, message);
    }

    /*
    @Override
    public String getNickname() {
        return nickname;
    }


    @Override
    public void playerJoined(String nickname) {

        view.playerJoined(String nickname);
    }

    @Override
    public void playerLeft(String nickname);

    @Override
    public void gameSetup();

    @Override
    public void playerReady(String nickname);

    @Override
    public void gameStarted();

    @Override
    public void cardAdded(String nickname, PhysicalCard card);

    @Override
    public void cardRemoved(String nickname, int index);

    @Override
    public void cardPlaced(String nickname, PlayableCard card, int x, int y);

    @Override
    public void cardDrawn(DrawingPosition drawingPosition);

    @Override
    public void gameEnd(List<String> winnerList, int totalScore, int objectivesScore);

    @Override
    public void chatMessage(String nickname, String message);
     */
}