package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;

import java.io.Serializable;

public interface View extends Serializable {

    String selectName();
    String selectGameName();
    int selectNumberOfPlayers();
    void waitPlayers();
    int selectJoinOrCreate();
    void allPlayerReady();
    void addModel(ModelMex modelmex);
    void updateChat(Chat chat);
    int selectObjectiveCard();
    boolean selectStarterFace();
    int game();
    void updateBoard();
    PlaceCardMex selectPlaceCard();
    DrawingPosition selectDrawingPosition();
    void printMessage(String message);
}
