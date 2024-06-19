package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.net.client.Client;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface View extends Serializable {
    void setClient(Client client);
    void listen();

    // SELECT ///////////////////////////////////////////////////////
    String selectName();
    int selectJoinOrCreate();
    String selectGameName();
    int selectNumberOfPlayers();
    int selectEndScore();

    // SHOW /////////////////////////////////////////////////////////
    void showConnected();
    void showFreeGames(Map<String, List<String>> freeGames);
    void showWaitPlayers();
    void showPlayerJoined(String nickname);
    void showPlayerLeft(String nickname);
    void showSetup();
    void showObjectives();
    void showPlayerReady(String nickname);
    void showStart();
    void showPlacingPhase();
    void showDrawingPhase();
    void showCurrentPlayer();
    void showCardsArea(String nickname);
    void showHand();
    void showDecks();
    void showScores();
    void showEnd();
    void showEndSession();
    void showHelp();
    void showError(String content);
    void showChatMessage(String sender, String content, String time);
}
