package it.polimi.sw.GC50.model.lobby;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.net.ClientInterface;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Take a list of waiting games and clients that are connected at the moment
 */
public class Lobby {
    private final Map<ClientInterface, String> clients;
    private final ArrayList<GameController> freeGameControllers;

    /**
     * constructs a new Lobby instance
     */
    public Lobby() {
        clients = new HashMap<>();
        freeGameControllers = new ArrayList<>();

        System.out.println("Server Ready!");
    }

    /**
     * Adds a player to the game
     *
     * @param clientInterface   interface that represents client
     * @param nickname        player's nickname
     * @return nickname of the player added
     */
    public synchronized String addPlayer(ClientInterface clientInterface, String nickname) {
        if (nickname == null || nickname.isEmpty() ||
                !nickname.equals(nickname.trim()) || clients.containsValue(nickname)) {
            return null;
        }
        clients.put(clientInterface, nickname);
        return nickname;
    }

    /**
     * Removes a player from the game
     *
     * @param clientInterface   interface that represents client
     */
    public synchronized void removePlayer(ClientInterface clientInterface) {
        clients.remove(clientInterface);
    }

    /**
     * Verify if a player is present in the game
     *
     * @param client a specific client
     * @return true if the player is present
     */
    private synchronized boolean isPlayerPresent(ClientInterface client) {
        return clients.containsKey(client);
    }

    /**
     * @param client      selected client
     * @param numOfPlayer number of players
     * @param gameId      game identifier
     * @return GameController
     * the return value is the GameController created by the client
     * if is it null the GameController is not created
     * this method is called when a client wants to create a GameController
     */
    public synchronized GameController createGame(ClientInterface client, String gameId, int numOfPlayer, int endScore) {
        if (isPlayerPresent(client)) {
            if (gameId == null || gameId.isEmpty() ||
                    !gameId.equals(gameId.trim()) || isGamePresent(gameId)) {
                return null;
            }
            try {
                GameController newGameController = new GameController(client, gameId, numOfPlayer, endScore, clients.get(client));
                freeGameControllers.add(newGameController);
                System.out.println("Game " + gameId + " created");
                return newGameController;
            } catch (RemoteException ignored) {
            }
        }
        return null;
    }

    /**
     * @param client client selected
     * @param gameId game identifier
     * @return GameController
     * the return value is the GameController
     * this method is called when a client wants to join a GameController
     */
    public synchronized GameController joinGame(ClientInterface client, String gameId) {
        if (isPlayerPresent(client)) {
            for (GameController gameController : freeGameControllers) {
                if (gameController.getGameId().equals(gameId) && gameController.isFree()) {
                    gameController.addPlayer(client, clients.get(client));
                    if (!gameController.isFree()) {
                        freeGameControllers.remove(gameController);
                    }
                    return gameController;
                }
            }
        }
        return null;
    }

    /**
     * Verify if the game is present, given GameID
     *
     * @param gameId game identifier
     * @return true if the game is present
     */
    private synchronized boolean isGamePresent(String gameId) {
        return getFreeGames().keySet().stream()
                .anyMatch(gameId::equals);
    }

    /**
     * @return a map of name of free games (all in the lobby)
     * and a list of players that are in each free games
     */
    public synchronized Map<String, List<String>> getFreeGames() {
        clearEmptyGames();
        Map<String, List<String>> freeGames = new HashMap<>();
        for (GameController gameController : freeGameControllers) {
            freeGames.put(gameController.getGameId(), gameController.getPlayerList());
        }
        return freeGames;
    }

    /**
     * deletes empty games
     */
    private void clearEmptyGames() {
        freeGameControllers.removeIf(GameController::isEmpty);
    }
}
