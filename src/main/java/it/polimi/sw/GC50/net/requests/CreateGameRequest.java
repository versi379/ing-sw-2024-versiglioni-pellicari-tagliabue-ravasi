package it.polimi.sw.GC50.net.requests;

import java.io.Serializable;

public class CreateGameRequest implements Serializable {
    private final String gameId;
    private final int numPlayers;
    private final int endScore;

    /**
     *Constructs an instance of CreateGameRequest
     * @param gameId        Id of the game
     * @param numPlayers    number of players in the game
     * @param endScore      Final score
     */
    public CreateGameRequest(String gameId, int numPlayers, int endScore) {
        this.gameId = gameId;
        this.numPlayers = numPlayers;
        this.endScore = endScore;
    }

    /**
     * @return game ID
     */
    public String getGameId() {
        return gameId;
    }

    /**
     *
     * @return number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     *
     * @return final score
     */
    public int getEndScore() {
        return endScore;
    }
}
