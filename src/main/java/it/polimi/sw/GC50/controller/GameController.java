package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.game.PlayingPhase;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GameController extends UnicastRemoteObject implements GameControllerRemote {
    private final Game game;
    private final Map<ClientInterface, Player> playerMap;

    public GameController(ClientInterface clientInterface, String gameId, int numPlayers, int endScore, String nickname) throws RemoteException {
        playerMap = new HashMap<>();
        game = new Game(gameId, numPlayers, endScore);
        addPlayer(clientInterface, nickname);
    }

    // GENERAL INFO ////////////////////////////////////////////////////////////////////////////////////////////////////
    public synchronized String getGameId() {
        return game.getId();
    }

    public synchronized List<String> getPlayerList() {
        return game.getPlayerList().stream().map(Player::getNickname).toList();
    }

    public synchronized boolean isFree() {
        return isWaiting();
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
        }
    }

    private Player getPlayer(ClientInterface clientInterface) throws RemoteException {
        if (playerMap.containsKey(clientInterface)) {
            return playerMap.get(clientInterface);
        } else {
            throw new RemoteException();
        }
    }

    // RECEIVE REQUESTS  ///////////////////////////////////////////////////////////////////////////////////////////////

    // UPDATE MODEL ////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public synchronized void selectObjectiveCard(ClientInterface clientInterface, int index) throws RemoteException {
        Player player = getPlayer(clientInterface);

        if (isStarting()) {
            List<ObjectiveCard> secretObjectivesList = game.getSecretObjectivesList(player);
            if (index >= 0 && index < secretObjectivesList.size()) {
                game.setSecretObjective(player, secretObjectivesList.get(index));
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
                        game.placeCard(player, card, x, y);
                        game.removeCard(player, index);
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
    public synchronized void sendChatMessage(ClientInterface clientInterface, String message) throws RemoteException {
        Player player = getPlayer(clientInterface);

        game.addChatMessage(player, message);
    }

    // FUFFA ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    synchronized public void updateController(ClientInterface clientInterface, Object update, Request request) throws RemoteException {
        if (!playerMap.containsKey(clientInterface)) {
            return;
        }
        System.out.println(request + " from player " + playerMap.get(clientInterface));
        Player player = playerMap.get(clientInterface);
        switch (request) {
            case SELECT_OBJECTIVE_CARD -> {
                selectObjectiveCard(player, (Integer) update);
            }
            case SELECT_STARTER_FACE -> {
                selectStarterFace(player, (Integer) update);
            }
            case PLACE_CARD -> {
                placeCard(player, (PlaceCardRequest) update);
            }
            case DRAW_CARD -> {
                drawCard(player, (Integer) update);
            }
            default -> {
            }
        }
    }

    synchronized public Object getModel(ClientInterface clientInterface) throws RemoteException {
        return new GameView(game, playerMap.get(clientInterface));
    }

     */

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
}
