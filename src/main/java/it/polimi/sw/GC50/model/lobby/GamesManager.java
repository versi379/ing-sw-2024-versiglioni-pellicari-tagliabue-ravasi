package it.polimi.sw.GC50.model.lobby;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.game.Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class GamesManager {
    private final Set<Player> players;
    private final Map<Game, GameController> gameControllers;

    public GamesManager() {
        players = new HashSet<>();
        gameControllers = new HashMap<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean containsPlayer(Player player) {
        return players.contains(player);
    }

    public void addGame(Game game) {
        gameControllers.put(game, new GameController(game));
    }

    public void removeGame(Game game) {
        gameControllers.remove(game);
    }

    public boolean containsGame(String gameId) {
        return gameControllers.keySet().stream()
                .map(Game::getId)
                .anyMatch(gameId::equals);
    }

    public Game getGame(String gameId) {
        for (Game game : gameControllers.keySet()) {
            if (gameId.equals(game.getId())) {
                return game;
            }
        }
        return null;
    }

    public GameController getController(Game game) {
        return gameControllers.get(game);
    }
}
