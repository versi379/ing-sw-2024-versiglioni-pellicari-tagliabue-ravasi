package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.*;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;

import java.util.List;

/**
 *
 */
public class GameController implements ViewObserver {
    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    public void addPlayer(Player player) {
        if (isWaiting()) {
            game.addPlayer(player);
        } else {
            sendError(player, "Partita già iniziata");
        }
    }

    public void removePlayer(Player player) {
        game.removePlayer(player);
    }

    /**
     * @param player
     * @param face
     */
    public void chooseStarterFace(Player player, boolean face) {
        if (isStarting()) {
            PhysicalCard starterCard = game.getStarterCard(player);
            game.setStarterCard(player, face ? starterCard.getFront() : starterCard.getBack());
        } else {
            sendError(player, "Operazione non disponibile");
        }
    }

    /**
     * @param player
     * @param index
     */
    public void chooseObjective(Player player, int index) {
        if (isStarting()) {
            List<ObjectiveCard> secretObjectivesList = game.getSecretObjectivesList(player);
            if (index >= 0 && index < secretObjectivesList.size()) {
                game.setSecretObjective(player, secretObjectivesList.get(index));
            } else {
                sendError(player, "Indice non valido");
            }
        } else {
            sendError(player, "Operazione non disponibile");
        }
    }

    /**
     * @param player
     * @param index
     * @param face
     * @param x
     * @param y
     */
    public void placeCard(Player player, int index, boolean face, int x, int y) {
        if (isPlacingPhase(player)) {
            List<PhysicalCard> playerHand = game.getHand(player);
            if (index >= 0 && index < playerHand.size()) {
                PlayableCard card = face ? playerHand.get(index).getFront() : playerHand.get(index).getBack();
                if (card.isPlaceable(game.getPlayerData(player), x, y)) {
                    game.placeCard(player, card, x, y);
                    game.removeCard(player, index);
                } else {
                    sendError(player, "Carta non piazzabile");
                }
            } else {
                sendError(player, "Indice non valido");
            }
        } else {
            sendError(player, "Operazione non disponibile");
        }
    }

    /**
     * @param player
     * @param position
     */
    public void drawCard(Player player, DrawingPosition position) {
        if (isDrawingPhase(player)) {
            PhysicalCard card = game.pickCard(position);
            if (card != null) {
                game.addCard(player, card);
            } else {
                sendError(player, "Posizione non disponibile");
            }
        } else {
            sendError(player, "Operazione non disponibile");
        }
    }

    private void sendError(Player player, String message) {
        player.addError(message);
    }

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
