package it.polimi.sw.GC50.net.observ;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;

import java.rmi.RemoteException;
import java.util.List;

public interface GameObserver {
    void update(GameObservable o, Request request, Object arg) throws RemoteException;
    void onUpdate(Message message) throws RemoteException;

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
