package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.net.client.GameException;
import it.polimi.sw.GC50.net.requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.requests.PlaceCardRequest;

import java.util.List;
import java.util.Map;

public interface ServerInterface {
    // CONNECTION //////////////////////////////////////////////////////////////////////////////////////////////////////
    boolean connect();

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

    void sendChatMessage(ChatMessageRequest message) throws GameException;

    void leaveGame() throws GameException;
}
