package it.polimi.sw.GC50.model.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class GamesManager {

    private static GamesManager instance = null;
    private final Set<Player> playersList;
    private final Map<String, Game> gamesList;

    private GamesManager() {
        playersList = new HashSet<>();
        gamesList = new HashMap<>();
    }

    public static GamesManager getInstance() {
        if (instance == null) {
            instance = new GamesManager();
        }
        return instance;
    }

    public void addPlayer(Player player) {
        playersList.add(player);
    }

    public void removePlayer(Player player) {
        playersList.remove(player);
    }

    public boolean containsPlayer(Player player) {
        return playersList.contains(player);
    }

    public void setGame(String id, int numPlayers, int endScore, Player creator) {
        gamesList.put(id, new Game(id, numPlayers, endScore, creator));
    }

    public void deleteGame(String id) {
        gamesList.remove(id);
    }

    public boolean containsGame(String id) {
        return gamesList.containsKey(id);
    }

    public Game getGame(String id) {
        return gamesList.get(id);
    }

}
