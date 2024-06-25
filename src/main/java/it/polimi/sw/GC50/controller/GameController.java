package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;
import it.polimi.sw.GC50.net.requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.ClientInterface;
import it.polimi.sw.GC50.net.requests.PlaceCardRequest;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GameController extends UnicastRemoteObject implements GameControllerRemote {
    private final String gameId;
    private final Game game;
    private final Map<ClientInterface, Player> playerMap;

    public GameController(ClientInterface clientInterface, String gameId, int numPlayers, int endScore, String nickname) throws RemoteException {
        this.gameId = gameId;
        game = new Game(numPlayers, endScore);
        playerMap = new HashMap<>();
        addPlayer(clientInterface, nickname);
    }

    // GENERAL INFO ////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized String getGameId() {
        return gameId;
    }

    public synchronized List<String> getPlayerList() {
        return game.getPlayerList().stream().map(Player::getNickname).toList();
    }

    public synchronized boolean isFree() {
        return isWaiting();
    }

    public synchronized boolean isEmpty() {
        return playerMap.isEmpty();
    }

    // PLAYERS MANAGEMENT //////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized boolean addPlayer(ClientInterface clientInterface, String nickname) {
        if (isFree()) {
            Player player = new Player(nickname);
            game.addObserver(clientInterface, player);
            game.addPlayer(player);
            playerMap.put(clientInterface, player);
            return true;
        }
        return false;
    }

    public synchronized void removePlayer(ClientInterface clientInterface) {
        if (playerMap.containsKey(clientInterface)) {
            Player player = playerMap.get(clientInterface);
            game.removePlayer(player);
            game.removeObserver(clientInterface);
            playerMap.remove(clientInterface);
        }
    }

    private Player getPlayer(ClientInterface clientInterface) throws RemoteException {
        if (playerMap.containsKey(clientInterface)) {
            return playerMap.get(clientInterface);
        } else {
            throw new RemoteException();
        }
    }

    // UPDATE MODEL ////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public synchronized void selectSecretObjective(ClientInterface clientInterface, int index) throws RemoteException {
        Player player = getPlayer(clientInterface);

        if (isStarting()) {
            List<ObjectiveCard> secretObjectivesSelection = game.getSecretObjectivesSelection(player);
            if (index >= 0 && index < secretObjectivesSelection.size()) {
                game.setSecretObjective(player, secretObjectivesSelection.get(index));
            } else {
                game.error(player, "Invalid index");
            }
        } else {
            game.error(player, "Operation currently not available");
        }
    }

    @Override
    public synchronized void selectStarterFace(ClientInterface clientInterface, int face) throws RemoteException {
        Player player = getPlayer(clientInterface);

        if (isStarting()) {
            if (face >= 0 && face < 2) {
                PhysicalCard starterCard = game.getStarterCard(player);
                game.setStarterCard(player, (face == 0) ? starterCard.getFront() : starterCard.getBack());
            } else {
                game.error(player, "Invalid index");
            }
        } else {
            game.error(player, "Operation currently not available");
        }
    }

    @Override
    public synchronized void placeCard(ClientInterface clientInterface, PlaceCardRequest placeCardRequest) throws RemoteException {
        Player player = getPlayer(clientInterface);

        placeCard(player, placeCardRequest.getIndex(), placeCardRequest.getFace(),
                placeCardRequest.getX(), placeCardRequest.getY());
    }

    private void placeCard(Player player, int index, int face, int x, int y) {
        if (isPlacingPhase(player)) {
            List<PhysicalCard> playerHand = game.getHand(player);
            if (index >= 0 && index < playerHand.size()) {
                if (face >= 0 && face < 2) {
                    PlayableCard card = (face == 0) ? playerHand.get(index).getFront() : playerHand.get(index).getBack();
                    if (card.isPlaceable(game.getPlayerData(player), x, y)) {
                        game.removeCard(player, index);
                        game.placeCard(player, card, x, y);
                    } else {
                        game.error(player, "Card not placeable");
                    }
                } else {
                    game.error(player, "Invalid face index");
                }
            } else {
                game.error(player, "Invalid card index");
            }
        } else {
            game.error(player, "Operation currently not available");
        }
    }

    @Override
    public synchronized void drawCard(ClientInterface clientInterface, int position) throws RemoteException {
        Player player = getPlayer(clientInterface);

        if (isDrawingPhase(player)) {
            if (position >= 0 && position < DrawingPosition.values().length) {
                PhysicalCard card = game.pickCard(DrawingPosition.values()[position]);
                if (card != null) {
                    game.addCard(player, card);
                } else {
                    game.error(player, "Empty deck");
                }
            } else {
                game.error(player, "Invalid index");
            }
        } else {
            game.error(player, "Operation currently not available");
        }
    }

    @Override
    public synchronized void sendChatMessage(ClientInterface clientInterface, ChatMessageRequest message) throws RemoteException {
        Player player = getPlayer(clientInterface);

        if (message.getReceiver() == null) {
            game.addChatMessage(player, message.getContent());
        } else {
            if (!player.getNickname().equals(message.getReceiver()) &&
                    playerMap.values().stream()
                            .anyMatch(x -> x.getNickname().equals(message.getReceiver()))) {
                game.addChatMessage(player,
                        playerMap.values().stream()
                                .filter(x -> x.getNickname().equals(message.getReceiver()))
                                .findFirst()
                                .orElse(null),
                        message.getContent());
            } else {
                game.error(player, "Invalid receiver");
            }
        }
    }

    @Override
    public synchronized void leaveGame(ClientInterface clientInterface) throws RemoteException {
        removePlayer(clientInterface);
    }

    // GAME STATUS /////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean isWaiting() {
        return game.getStatus().equals(GameStatus.WAITING);
    }

    private boolean isStarting() {
        return game.getStatus().equals(GameStatus.SETUP);
    }

    private boolean isPlayerTurn(Player player) {
        return game.getStatus().equals(GameStatus.PLAYING) &&
                game.getCurrentPlayer().equals(player);
    }

    private boolean isPlacingPhase(Player player) {
        return isPlayerTurn(player) &&
                game.getCurrentPhase().equals(PlayingPhase.PLACING);
    }

    private boolean isDrawingPhase(Player player) {
        return isPlayerTurn(player) &&
                game.getCurrentPhase().equals(PlayingPhase.DRAWING);
    }
}
