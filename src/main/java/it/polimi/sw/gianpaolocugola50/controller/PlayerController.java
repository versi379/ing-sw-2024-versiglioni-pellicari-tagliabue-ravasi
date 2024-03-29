package it.polimi.sw.gianpaolocugola50.controller;

import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.game.DrawingPosition;
import it.polimi.sw.gianpaolocugola50.model.game.Game;
import it.polimi.sw.gianpaolocugola50.model.game.GamesManager;
import it.polimi.sw.gianpaolocugola50.model.game.Player;

public class PlayerController implements ViewObserver {
    private final Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    public void createGame(String id, int numPlayers) {
        GamesManager gamesManager = GamesManager.getInstance();
        if (!gamesManager.containsGame(id)) {
            gamesManager.setGame(id, numPlayers, player);
        } //else {
        // roba per notificare player che esiste già un gioco con quell'id
        //}
    }

    public void joinGame(String id) {
        GamesManager gamesManager = GamesManager.getInstance();
        if (gamesManager.containsGame(id)) {
            gamesManager.getGame(id).addPlayer(player);
        } //else {
        // roba per notificare player che non esiste un gioco con quell'id
        //}
    }

    public void abandonGame() {
        if (player.getCurrentGame() != null) {
            player.getCurrentGame().removePlayer(player);
            player.setCurrentGame(null);
        }
    }

    public void placeCard(PhysicalCard physicalCard, boolean face, int x, int y) {
        PlayableCard card = face ? physicalCard.getFront() : physicalCard.getBack();

        if (player.getCurrentGame() != null && card.isPlaceable(player.getPlayerData(), x, y)) {
            player.getPlayerData().placeCard(card, x, y);
        } //else {
        // roba per notificare player che la carta non è piazzabile
        //}
    }

    public void drawCard(DrawingPosition position) {
        if (player.getCurrentGame() != null) {
            player.getPlayerData().addCard(player.getCurrentGame().drawCard(position));
        }
    }
}
