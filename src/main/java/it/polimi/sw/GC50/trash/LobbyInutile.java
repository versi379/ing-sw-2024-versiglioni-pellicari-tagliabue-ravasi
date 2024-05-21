package it.polimi.sw.GC50.trash;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
public class LobbyInutile {
    private final Set<Player> players;
    private final Map<Game, GameController> gameControllers;

    public LobbyInutile() {
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
        return gameControllers.keySet().stream()
                .filter(x -> gameId.equals(x.getId()))
                .findAny()
                .orElse(null);
    }

    public GameController getController(Game game) {
        return gameControllers.get(game);
    }
}

 */
