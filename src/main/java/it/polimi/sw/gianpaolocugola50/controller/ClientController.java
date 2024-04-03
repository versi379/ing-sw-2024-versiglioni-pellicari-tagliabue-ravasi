package it.polimi.sw.gianpaolocugola50.controller;

import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.game.*;
import it.polimi.sw.gianpaolocugola50.view.View;

import java.util.List;

public class ClientController implements ViewObserver {
    private final View view;
    private Player player;

    public ClientController(View view) {
        this.view = view;
        player = null;
    }

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

    public void resetPlayer() {
        GamesManager gamesManager = GamesManager.getInstance();
        if (player != null) {
            abandonCurrentGame();
            gamesManager.removePlayer(player);
            player = null;
        }
    }

    public void createGame(String id, int numPlayers) {
        GamesManager gamesManager = GamesManager.getInstance();
        if (!gamesManager.containsGame(id)) {
            if (numPlayers >= 1 && numPlayers <= 4) {
                gamesManager.setGame(id, numPlayers, player);
            } else {
                System.err.println("Numero giocatori non valido");
            }
        } else {
            System.err.println("Partita già esistente");
        }
    }

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

    public void abandonCurrentGame() {
        if (isInGame()) {
            Game game = player.getCurrentGame();
            game.removePlayer(player);
            player.setCurrentGame(null);
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    public void chooseStarterFace(boolean face) {
        if (isInGame() && isStarting()) {
            Game game = player.getCurrentGame();
            game.setStarterCard(player, face ? game.getStarterCard(player).getFront() : game.getStarterCard(player).getBack());
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    public void chooseObjective(int index) {
        if (isInGame() && isStarting()) {
            Game game = player.getCurrentGame();
            if (index >= 0 && index < game.getSecretObjectivesList(player).size()) {
                game.setSecretObjective(player, game.getSecretObjectivesList(player).get(index));
            } else {
                System.err.println("Indice non valido");
            }
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    public void placeCard(int index, boolean face, int x, int y) {
        if (isInGame() && isPlaying() && isPlayerTurn()) {
            Game game = player.getCurrentGame();
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

    public void drawCard(DrawingPosition position) {
        if (isInGame() && isPlaying() && isPlayerTurn()) {
            Game game = player.getCurrentGame();
            PhysicalCard card = game.drawCard(position);
            if (card != null) {
                game.addCard(player, card);
                game.nextPlayer();
            } else {
                System.err.println("Posizione non disponibile");
            }
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    private boolean isInGame() {
        return player.getCurrentGame() != null;
    }

    private boolean isStarting() {
        return player.getCurrentGame().getStatus().equals(GameStatus.SETUP);
    }

    private boolean isPlaying() {
        return player.getCurrentGame().getStatus().equals(GameStatus.PLAYING) ||
                player.getCurrentGame().getStatus().equals(GameStatus.ENDING);
    }

    private boolean isPlayerTurn() {
        return player.getCurrentGame().getCurrentPlayer().equals(player);
    }
}
