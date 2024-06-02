package it.polimi.sw.GC50.net.Messages;

import java.io.Serializable;
import java.util.Map;

public class CreateGameMessage implements Message, Serializable{
    private String gameId;
    private int numPlayers;
    private int endScore;

    public CreateGameMessage(String gameId, int numPlayers, int endScore) {
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
