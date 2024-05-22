package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface View extends Serializable {
    void setModel(GameView gameView);

    // SELECT ///////////////////////////////////////////////////////
    String selectName();
    int selectJoinOrCreate();
    String selectGameName();
    int selectNumberOfPlayers();
    int selectEndScore();
    int selectObjectiveCard();
    boolean selectStarterFace();
    PlaceCardRequest selectPlaceCard();
    DrawingPosition selectDrawingPosition();

    // SHOW /////////////////////////////////////////////////////////
    void showFreeGames(Map<String, List<String>> freeGames);
    void showWaitPlayers();
    void showPlayerJoined(String nickname);
    void showPlayerLeft(String nickname);
    void showSetup();
    void showCommonObjectives();
    void showPlayerReady(String nickname);
    void showStart();
    void showPlayerArea(String nickname);
    void showDecks();
    void showScores();
    void showEnd();
    void showEndSession();
    void showError(String content);
    void showMessage(String message);

    // ????
    void updateChat(Chat chat);
}
