package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.*;
import it.polimi.sw.GC50.view.View;

import java.util.List;

public class Controller implements ViewObserver {

    private final View view;
    private Player player;

    /**
     * @param view
     */
    public Controller(View view) {
        this.view = view;
        player = null;
    }

    /**
     *
     * @param nickName
     */
    public void setPlayer(String nickName) {
        GamesManager gamesManager = GamesManager.getInstance();
        if (!gamesManager.containsPlayer(new Player(nickName))) {
            resetPlayer();
            player = new Player(nickName);
            gamesManager.addPlayer(player);
        } else {
            System.err.println("Giocatore già esistente");
        }
    }

    /**
     *
     */
    public void resetPlayer() {
        GamesManager gamesManager = GamesManager.getInstance();
        if (player != null) {
            abandonCurrentGame();
            gamesManager.removePlayer(player);
            player = null;
        }
    }

    /**
     *
     * @param id
     * @param numPlayers
     * @param endScore
     */
    public void createGame(String id, int numPlayers, int endScore) {
        GamesManager gamesManager = GamesManager.getInstance();
        if (!gamesManager.containsGame(id)) {
            if (numPlayers >= 1 && numPlayers <= 4) {
                gamesManager.setGame(id, numPlayers, endScore, player);
            } else {
                System.err.println("Numero giocatori non valido");
            }
        } else {
            System.err.println("Partita già esistente");
        }
    }

    /**
     *
     * @param id
     */
    public void joinGame(String id) {
        GamesManager gamesManager = GamesManager.getInstance();
        if (gamesManager.containsGame(id)) {
            Game game = gamesManager.getGame(id);
            if (game.getStatus().equals(GameStatus.WAITING)) {
                game.addPlayer(player);
            } else {
                System.err.println("Partita già iniziata");
            }
        } else {
            System.err.println("Partita non esistente");
        }
    }

    /**
     *
     */
    public void abandonCurrentGame() {
        Game game = player.getCurrentGame();
        if (isInGame(game)) {
            game.removePlayer(player);
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    /**
     *
     * @param face
     */
    public void chooseStarterFace(boolean face) {
        Game game = player.getCurrentGame();
        if (isInGame(game) && isStarting(game)) {
            game.setStarterCard(player, face ? game.getStarterCard(player).getFront() : game.getStarterCard(player).getBack());
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    /**
     *
     * @param index
     */
    public void chooseObjective(int index) {
        Game game = player.getCurrentGame();
        if (isInGame(game) && isStarting(game)) {
            if (index >= 0 && index < game.getSecretObjectivesList(player).size()) {
                game.setSecretObjective(player, game.getSecretObjectivesList(player).get(index));
            } else {
                System.err.println("Indice non valido");
            }
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    /**
     *
     * @param index
     * @param face
     * @param x
     * @param y
     */
    public void placeCard(int index, boolean face, int x, int y) {
        Game game = player.getCurrentGame();
        if (isInGame(game) && isPlacingPhase(game)) {
            List<PhysicalCard> playerHand = game.getHand(player);
            if (index >= 0 && index < playerHand.size()) {
                PlayableCard card = face ? playerHand.get(index).getFront() : playerHand.get(index).getBack();
                if (card.isPlaceable(game.getPlayerData(player), x, y)) {
                    game.placeCard(player, card, x, y);
                    game.removeCard(player, index);
                } else {
                    System.err.println("Carta non piazzabile");
                }
            } else {
                System.err.println("Indice non valido");
            }
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    /**
     *
     * @param position
     */
    public void drawCard(DrawingPosition position) {
        Game game = player.getCurrentGame();
        if (isInGame(game) && isDrawingPhase(game)) {
            PhysicalCard card = game.pickCard(position);
            if (card != null) {
                game.addCard(player, card);
            } else {
                System.err.println("Posizione non disponibile");
            }
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    /**
     *
     * @param game
     * @return
     */
    private boolean isInGame(Game game) {
        return game != null;
    }

    /**
     *
     * @param game
     * @return
     */
    private boolean isStarting(Game game) {
        return game.getStatus().equals(GameStatus.SETUP);
    }

    /**
     *
     * @param game
     * @return
     */
    private boolean isPlacingPhase(Game game) {
        return game.getStatus().equals(GameStatus.PLAYING) &&
                game.getCurrentPhase().equals(PlayingPhase.PLACING) &&
                isPlayerTurn(game);
    }

    /**
     *
     * @param game
     * @return
     */
    private boolean isDrawingPhase(Game game) {
        return game.getStatus().equals(GameStatus.PLAYING) &&
                game.getCurrentPhase().equals(PlayingPhase.DRAWING) &&
                isPlayerTurn(game);
    }

    /**
     *
     * @param game
     * @return
     */
    private boolean isPlayerTurn(Game game) {
        return game.getCurrentPlayer().equals(player);
    }

}
