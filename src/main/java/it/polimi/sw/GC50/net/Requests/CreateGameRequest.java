package it.polimi.sw.GC50.net.Requests;

import java.io.Serializable;

public class CreateGameRequest implements Serializable {
    private final String gameId;
    private final int numPlayers;
    private final int endScore;

    public CreateGameRequest(String gameId, int numPlayers, int endScore) {
        this.gameId = gameId;
        this.numPlayers = numPlayers;
        this.endScore = endScore;
    }

    public String getGameId() {
        return gameId;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getEndScore() {
        return endScore;
    }
}
