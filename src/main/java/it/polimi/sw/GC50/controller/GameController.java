package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.*;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Request;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GameController {
    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    public GameController(String gameId, int numPlayers, int endScore, Player creator) {
        game = new Game(gameId, numPlayers, endScore, creator);
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
            game.error(Request.NOTIFY_OPERATION_NOT_AVAILABLE, player.getNickname());
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
                game.error(Request.NOTIFY_INVALID_INDEX, player.getNickname());
            }
        } else {
            sendError(player, "Operazione non disponibile");
            game.error(Request.NOTIFY_OPERATION_NOT_AVAILABLE, player.getNickname());
        }
    }


    public void placeCard(Player player, PlaceCardMex placeCardMex) {
        int index = placeCardMex.getIndexinhand();
        int x = placeCardMex.getX();
        int y = placeCardMex.getY();
        boolean face = placeCardMex.isFace();

        if (isPlacingPhase(player)) {
            List<PhysicalCard> playerHand = game.getHand(player);
            if (index >= 0 && index < playerHand.size()) {
                PlayableCard card = face ? playerHand.get(index).getFront() : playerHand.get(index).getBack();
                if (card.isPlaceable(game.getPlayerData(player), x, y)) {
                    game.placeCard(player, card, x, y);
                    game.removeCard(player, index);
                } else {
                    sendError(player, "Carta non piazzabile");
                    game.error(Request.NOTIFY_CARD_NOT_PLACEABLE, player.getNickname());
                }
            } else {
                sendError(player, "Indice non valido");
                game.error(Request.NOTIFY_CARD_NOT_FOUND, player.getNickname());
            }
        } else {
            sendError(player, "Operazione non disponibile");
            game.error(Request.NOTIFY_OPERATION_NOT_AVAILABLE, player.getNickname());
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
                    game.error(Request.NOTIFY_CARD_NOT_PLACEABLE, player.getNickname());
                }
            } else {
                sendError(player, "Indice non valido");
                game.error(Request.NOTIFY_CARD_NOT_FOUND, player.getNickname());
            }
        } else {
            sendError(player, "Operazione non disponibile");
            game.error(Request.NOTIFY_OPERATION_NOT_AVAILABLE, player.getNickname());
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
                game.error(Request.NOTIFY_POSITION_DRAWING_NOT_AVAILABLE, player.getNickname());
            }
        } else {
            sendError(player, "Operazione non disponibile");
            game.error(Request.NOTIFY_OPERATION_NOT_AVAILABLE, player.getNickname());
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

    //////////////////////////////////////////
    //
    ///////////////////////////////////////////
    public Player getPlayer(String nickname) {

        for (Player p : game.getPlayerList()) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    synchronized public Object getGameModel(Player player) {
        ArrayList<CardsMatrix> board = new ArrayList<>();
        ArrayList<Color> color = new ArrayList<>();
        ArrayList<Integer> point = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        for (Player p : game.getPlayerList()) {
            //if (!p.getNickname().equals(player.getNickname())) {
                board.add(game.getPlayerData(p).getCardsArea());
                color.add(Color.BLUE);
                point.add(game.getPlayerData(p).getTotalScore());
                name.add(p.getNickname());
            //}
        }
        return new ModelMex(game.getPlayerData(player), game.getChat(), game.getCurrentPlayer().getNickname(),name, board, color, point,game.getDrawingCard());
    }

    public void updateChat(Player player, String message) {
        game.sendMessageInChat(player, message);
    }

    // TEST METHODS ////////////////////////////////////////////////////////////////////////////////////////////////////
    public Game getGame() {
        return game;
    }

    public void addObserver(ClientInterface clientInterface) {
        game.addObserver(clientInterface);
    }
}
