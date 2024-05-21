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
import it.polimi.sw.GC50.view.GameView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GameController extends UnicastRemoteObject implements GameControllerRemote {
    private final Game game;
    private final Map<ClientInterface, Player> playerMap;

    public GameController(String gameId, int numPlayers, int endScore, ClientInterface clientInterface, String nickname) throws RemoteException {
        playerMap = new HashMap<>();
        Player creator = new Player(nickname);
        game = new Game(gameId, numPlayers, endScore, creator);
        game.addObserver(clientInterface, creator);
        playerMap.put(clientInterface, creator);
    }

    // GENERAL INFO ////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getGameId() {
        return game.getId();
    }

    public boolean isFree() {
        return isWaiting();
    }

    // PLAYERS MANAGEMENT //////////////////////////////////////////////////////////////////////////////////////////////
    public boolean addPlayer(ClientInterface clientInterface, String nickname) {
        if (isFree()) {
            Player player = new Player(nickname);
            game.addObserver(clientInterface, player);
            game.addPlayer(player);
            playerMap.put(clientInterface, player);
            return true;
        }
        return false;
    }

    public void removePlayer(ClientInterface clientInterface) {
        if (playerMap.containsKey(clientInterface)) {
            Player player = playerMap.get(clientInterface);
            game.removePlayer(player);
            game.removeObserver(clientInterface);
        }
    }

    // RECEIVE REQUESTS  ///////////////////////////////////////////////////////////////////////////////////////////////
    synchronized public void updateController(Request request, Object update, ClientInterface clientInterface) throws RemoteException {
        if (!playerMap.containsKey(clientInterface)) {
            return;
        }
        System.out.println(request + " from player " + playerMap.get(clientInterface));
        Player player = playerMap.get(clientInterface);
        switch (request) {
            case SELECT_STARTER_FACE -> {
                chooseStarterFace(player, (Boolean) update);
            }
            case SELECT_OBJECTIVE_CARD -> {
                chooseObjective(player, (Integer) update);
            }
            case PLACE_CARD -> {
                placeCard(player, (PlaceCardMex) update);
            }
            case DRAW_CARD -> {
                drawCard(player, (DrawingPosition) update);
            }
            default -> {
            }
        }
    }

    synchronized public void updateChat(String message, ClientInterface clientInterface) throws RemoteException {
        if (!playerMap.containsKey(clientInterface)) {
            return;
        }
        Player player = playerMap.get(clientInterface);
        updateChat(player, message);
        System.out.println("chat updated");
    }

    // UPDATE MODEL ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param player
     * @param face
     */
    public void chooseStarterFace(Player player, boolean face) {
        if (isStarting()) {
            PhysicalCard starterCard = game.getStarterCard(player);
            game.setStarterCard(player, face ? starterCard.getFront() : starterCard.getBack());
        } else {
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
                game.error(Request.NOTIFY_INVALID_INDEX, player.getNickname());
            }
        } else {
            game.error(Request.NOTIFY_OPERATION_NOT_AVAILABLE, player.getNickname());
        }
    }

    public void placeCard(Player player, PlaceCardMex placeCardMex) {
        placeCard(player, placeCardMex.getIndex(), placeCardMex.isFace(), placeCardMex.getX(), placeCardMex.getY());
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
                    game.error(Request.NOTIFY_CARD_NOT_PLACEABLE, player.getNickname());
                }
            } else {
                game.error(Request.NOTIFY_CARD_NOT_FOUND, player.getNickname());
            }
        } else {
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
                game.error(Request.NOTIFY_DRAWING_POSITION_NOT_AVAILABLE, player.getNickname());
            }
        } else {
            game.error(Request.NOTIFY_OPERATION_NOT_AVAILABLE, player.getNickname());
        }
    }

    public void updateChat(Player player, String message) {
        game.sendMessageInChat(player, message);
    }

    // MODEL MEX ///////////////////////////////////////////////////////////////////////////////////////////////////////
    synchronized public Object getModel(ClientInterface clientInterface) throws RemoteException {
        return new GameView(game, playerMap.get(clientInterface));
    }

    /*
    synchronized public Object getModel(ClientInterface clientInterface) throws RemoteException {
        if (!playerMap.containsKey(clientInterface)) {
            return null;
        }
        Player player = playerMap.get(clientInterface);
        ArrayList<CardsMatrix> board = new ArrayList<>();
        ArrayList<Color> color = new ArrayList<>();
        ArrayList<Integer> point = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();

        for (Player p : game.getPlayerList()) {
            if (!p.getNickname().equals(player.getNickname())) {
                board.add(game.getPlayerData(p).getCardsArea());
                color.add(Color.BLUE);
                point.add(game.getPlayerData(p).getTotalScore());
                name.add(p.getNickname());
            }
        }

        ModelMex modelMex = new ModelMex(game.getPlayerData(player), player, game.getChat(),
                game.getCurrentPlayer().getNickname(), name,
                board, color, point,
                game.getDecksTop(), game.getCurrentPhase(), game.getStatus());
        return modelMex;
    }
     */

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

    // TEST METHODS ////////////////////////////////////////////////////////////////////////////////////////////////////
    public GameController(Game game) throws RemoteException {
        this.game = game;
        playerMap = new HashMap<>();
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer(String nickname) {

        for (Player p : game.getPlayerList()) {
            if (p.getNickname().equals(nickname)) {
                return p;
            }
        }
        return null;
    }
}
