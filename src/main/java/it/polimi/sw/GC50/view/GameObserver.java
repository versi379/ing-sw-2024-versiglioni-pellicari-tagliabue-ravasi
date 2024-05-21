package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.GameView;

import java.rmi.RemoteException;

public interface GameObserver {
    void update(Request request, Object arg, GameView gameView) throws RemoteException;

    //void onUpdate(Message message) throws RemoteException;

    /*
    String getNickname();

    void setModel();

    void playerJoined(String nickname);

    void playerLeft(String nickname);

    void gameSetup();

    void playerReady(String nickname);

    void gameStarted();

    void cardAdded(String nickname, PhysicalCard card);

    void cardRemoved(String nickname, int index);

    void cardPlaced(String nickname, PlayableCard card, int x, int y);

    void cardDrawn(DrawingPosition drawingPosition);

    void gameEnd(List<String> winnerList, int totalScore, int objectivesScore);

    void chatMessage(String nickname, String message);
    */
}
