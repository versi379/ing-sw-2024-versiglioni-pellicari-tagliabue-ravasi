package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface View extends Serializable {
    void addModel(GameView gameView);

    // SELECT ///////////////////////////////////////////////////////
    String selectName();
    int selectJoinOrCreate();
    String selectGameName();
    int selectNumberOfPlayers();
    int selectObjectiveCard();
    boolean selectStarterFace();
    PlaceCardMex selectPlaceCard();
    DrawingPosition selectDrawingPosition();

    // SHOW /////////////////////////////////////////////////////////
    void showFreeGames(Map<String, List<String>> freeGames);
    void showWaitPlayers();
    void showPlayerJoined(String nickname);
    void showPlayerLeft(String nickname);
    void showSetup();
    void showCommonObjectives();
    void showPlayerReady(String nickname);
    void showPlayerArea(String nickname);
    void showDecks();
    void showScores();
    void showEndSession();
    void showError();
    void showMessage(String message);

    // ????
    void updateChat(Chat chat);
    void updateBoard();
    int game();
}
