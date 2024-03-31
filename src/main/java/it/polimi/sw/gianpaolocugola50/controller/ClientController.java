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
            if (numPlayers >= 1 && numPlayers < 5) {
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
                if (game.getPlayerList().size() >= game.getNumPlayers()) {
                    game.setup();
                }
            } else {
                System.err.println("Partita già iniziata");
            }
        } else {
            System.err.println("Partita non esistente");
        }
    }

    public void abandonCurrentGame() {
        Game game = player.getCurrentGame();
        if (game != null) {
            game.removePlayer(player);
            if (game.getPlayerList().isEmpty()) {
                GamesManager.getInstance().deleteGame(game.getId());
            }
            player.setCurrentGame(null);
        }
    }

    public void chooseStarterFace(boolean face) {
        if (isStarting()) {
            player.getPlayerData().setStarterCard(face ? player.getPlayerData().getStarterCard().getFront()
                    : player.getPlayerData().getStarterCard().getBack());
            checkSetupStatus();
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    public void chooseObjective(int index) {
        if (isStarting()) {
            if (index >= 0 && index < player.getPlayerData().getSecretObjectivesList().size()) {
                player.getPlayerData().setSecretObjective(player.getPlayerData().getSecretObjectivesList().get(index));
                checkSetupStatus();
            } else {
                System.err.println("Indice non valido");
            }
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    private void checkSetupStatus() {
        if (player.getCurrentGame().getPlayerList().stream()
                .map(Player::getPlayerData)
                .allMatch(PlayerData::isReady)) {
            player.getCurrentGame().start();
        }
    }

    public void placeCard(int index, boolean face, int x, int y) {
        if (isPlaying() && isPlayerTurn()) {
            List<PhysicalCard> playerHand = player.getPlayerData().getHand();
            if (index >= 0 && index < playerHand.size()) {
                PlayableCard card = face ? playerHand.get(index).getFront() : playerHand.get(index).getBack();
                if (card.isPlaceable(player.getPlayerData(), x, y)) {
                    player.getPlayerData().placeCard(card, x, y);
                    player.getPlayerData().removeCard(index);
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
        if (isPlaying() && isPlayerTurn()) {
            PhysicalCard card = player.getCurrentGame().drawCard(position);
            if (card != null) {
                player.getPlayerData().addCard(card);
                player.getCurrentGame().nextPlayer();
            } else {
                System.err.println("Posizione non disponibile");
            }
        } else {
            System.err.println("Operazione non disponibile");
        }
    }

    private boolean isStarting() {
        return player.getCurrentGame() != null && player.getCurrentGame().getStatus().equals(GameStatus.SETUP);
    }

    private boolean isPlaying() {
        return player.getCurrentGame() != null &&
                (player.getCurrentGame().getStatus().equals(GameStatus.PLAYING) ||
                        player.getCurrentGame().getStatus().equals(GameStatus.ENDING));
    }

    private boolean isPlayerTurn() {
        return player.getCurrentGame().getCurrentPlayer().equals(player);
    }
}
