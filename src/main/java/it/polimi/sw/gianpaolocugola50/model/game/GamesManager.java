package it.polimi.sw.gianpaolocugola50.model.game;

import java.util.HashMap;
import java.util.Map;

public class GamesManager {
    private static GamesManager instance = null;
    private final Map<String, Game> gamesList;

    private GamesManager() {
        gamesList = new HashMap<>();
    }

    public static GamesManager getInstance() {
        if (instance == null) {
            instance = new GamesManager();
        }
        return instance;
    }

    public void setGame(String id, int numPlayers, Player creator) {
        gamesList.put(id, new Game(id, numPlayers, creator));
    }

    public boolean containsGame(String id) {
        return gamesList.containsKey(id);
    }

    public Game getGame(String id) {
        return gamesList.get(id);
    }
}
