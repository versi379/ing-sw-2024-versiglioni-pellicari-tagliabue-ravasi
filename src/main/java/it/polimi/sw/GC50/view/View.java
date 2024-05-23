package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.net.util.Client;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface View extends Serializable {
    void setClient(Client client);
    void listen();

    // SELECT ///////////////////////////////////////////////////////
    String selectName() throws InterruptedException;
    int selectJoinOrCreate();
    String selectGameName();
    int selectNumberOfPlayers();
    int selectEndScore();

    // SHOW /////////////////////////////////////////////////////////
    void showFreeGames(Map<String, List<String>> freeGames);
    void showWaitPlayers();
    void showPlayerJoined(String nickname);
    void showPlayerLeft(String nickname);
    void showSetup();
    void showCommonObjectives();
    void showPlayerReady(String nickname);
    void showStart();
    void showCurrentPlayer();
    void showPlayerArea(String nickname);
    void showDecks();
    void showScores();
    void showEnd();
    void showEndSession();
    void showHelp();
    void showError(String content);
    void showMessage(String message);
    // ????
    void updateChat(Chat chat);
}
