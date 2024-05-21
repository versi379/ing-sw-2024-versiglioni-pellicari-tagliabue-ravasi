package trash;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.HashMap;
import java.util.Map;

public class LobbyController {
    /*
    private final LobbyInutile lobbyInutile;
    private final Map<Integer, Player> playerMap;

    public LobbyController(LobbyInutile lobbyInutile) {
        this.lobbyInutile = lobbyInutile;
        playerMap = new HashMap<>();
    }

    public void setPlayer(Integer clientId, String nickname) {
        resetPlayer(clientId);
        Player player = new Player(nickname);
        addPlayer(clientId, player);
    }
    public void resetPlayer(Integer clientId) {
        if (isPresent(clientId)) {
            removePlayer(clientId);
        }
    }

    public void createGame(Integer clientId, String gameId, int numPlayers, int endScore) {
        if (isPresent(clientId)) {
            Player player = getPlayer(clientId);
            if (!lobbyInutile.containsGame(gameId)) {
                if (numPlayers >= 1 && numPlayers <= 4) {
                    Game game = new Game(gameId, numPlayers, endScore, player);
                    lobbyInutile.addGame(game);
                } else {
                    sendError(player, "Numero giocatori non valido");
                }
            } else {
                sendError(player, "Partita già esistente");
            }
        } else {
            // Notifica client giocatore non assegnato?
            System.err.println("Giocatore non assegnato");
        }
    }

    public void joinGame(Integer clientId, String gameId) {
        if (isPresent(clientId)) {
            Player player = getPlayer(clientId);
            if (lobbyInutile.containsGame(gameId)) {
                Game game = lobbyInutile.getGame(gameId);
                if (game.getStatus().equals(GameStatus.WAITING)) {
                    if (!game.containsPlayer(player)) {
                        game.addPlayer(getPlayer(clientId));
                    } else {
                        sendError(player, "Nome giocatore già presente");
                    }
                } else {
                    sendError(player, "Partita già iniziata");
                }
            } else {
                sendError(player, "Partita non esistente");
            }
        } else {
            // Notifica client giocatore non assegnato?
            System.err.println("Giocatore non assegnato");
        }
    }

    private boolean isPresent(Integer clientId) {
        return playerMap.containsKey(clientId);
    }

    private void addPlayer(Integer clientId, Player player) {
        playerMap.put(clientId, player);
    }

    private void removePlayer(Integer clientId) {
        playerMap.remove(clientId);
    }

    private Player getPlayer(Integer clientId) {
        return playerMap.get(clientId);
    }

    private void sendError(Player player, String message) {
        player.addError(message);
    }

    private GameController getGameController(Game game) {
        return lobbyInutile.getController(game);
    }

     */
}
