package it.polimi.sw.GC50.net.util;

import java.util.List;
import java.util.Map;

public interface ServerInterface {
    void connect() throws GameException;

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    String setPlayer(String nickname) throws GameException;

    void resetPlayer() throws GameException;

    boolean createGame(String gameId, int numPlayers, int endScore) throws GameException;

    boolean joinGame(String gameId) throws GameException;

    Map<String, List<String>> getFreeGames() throws GameException;

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    void selectSecretObjective(int index) throws GameException;

    void selectStarterFace(int face) throws GameException;

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    void placeCard(PlaceCardRequest placeCardRequest) throws GameException;

    void drawCard(int position) throws GameException;

    void sendChatMessage(String message) throws GameException;
}
