package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.util.Request;

import java.io.Serializable;

public interface View extends Serializable {

    String askName();
    String askGameName();
    int askNumberOfPlayer();
    void waitPlayer();
    int joinorcreate();
    void allPlayerReady();
    void addModel(ModelMex modelmex);
    void updateChat(Chat chat);
    int SelectObjectiveCard();
    Boolean selectStarterFace();
    int game();
    void updateBoard();
    PlaceCardMex askPlaceCard();
    DrawingPosition choseWhereToDraw();
}
