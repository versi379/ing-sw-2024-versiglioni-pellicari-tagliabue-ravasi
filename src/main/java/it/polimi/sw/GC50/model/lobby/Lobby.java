package it.polimi.sw.GC50.model.lobby;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.net.util.ClientInterface;

import java.rmi.RemoteException;
import java.util.*;

public class Lobby {
    private final Map<ClientInterface, String> clients;
    private final ArrayList<GameController> freeGameControllers;

    public Lobby() {
        clients = new HashMap<>();
        freeGameControllers = new ArrayList<>();

        System.out.println("Server Ready!");
    }

    public synchronized boolean addPlayer(ClientInterface clientInterface, String nickname) {
        if (nickname == null || nickname.isEmpty() || clients.containsValue(nickname)) {
            return false;
        }
        clients.put(clientInterface, nickname);
        return true;
    }

    /**
     * @param client
     * @param numOfPlayer
     * @param gameId
     * @param nickname
     * @return GameController
     * the return value is the GameController created by the client
     * if is it null the GameController is not created
     * this method is called when a client wants to create a GameController
     * @
     */
    public synchronized GameController createGame(ClientInterface client, String gameId, int numOfPlayer, int endScore, String nickname) throws RemoteException {
        if (isPlayerPresent(client) && !isGamePresent(gameId)) {
            System.out.println("Game " + gameId + " created");
            GameController newGameController = new GameController(client, gameId, numOfPlayer, endScore, nickname);
            freeGameControllers.add(newGameController);
            return newGameController;
        } else {
            return null;
        }
    }

    private synchronized boolean isPlayerPresent(ClientInterface client) {
        return clients.containsKey(client);
    }

    private synchronized boolean isGamePresent(String gameId) {
        return freeGameControllers.stream()
                .map(GameController::getGameId)
                .anyMatch(gameId::equals);
    }

    public synchronized GameController joinGame(ClientInterface client, String gameId, String nickname) {
        if (isPlayerPresent(client)) {
            for (GameController gameController : freeGameControllers) {
                if (gameController.getGameId().equals(gameId) && gameController.isFree()) {
                    gameController.addPlayer(client, nickname);
                    if (!gameController.isFree()) {
                        freeGameControllers.remove(gameController);
                    }
                    return gameController;
                }
            }
        }
        return null;
    }

    public synchronized Map<String, List<String>> getFreeGames() {
        Map<String, List<String>> freeGames = new HashMap<>();
        for (GameController gameController : freeGameControllers) {
            freeGames.put(gameController.getGameId(), gameController.getPlayerList());
        }
        return freeGames;
    }
}
