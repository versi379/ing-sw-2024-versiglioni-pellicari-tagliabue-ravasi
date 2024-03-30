package it.polimi.sw.gianpaolocugola50.controller;

import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.game.DrawingPosition;
import it.polimi.sw.gianpaolocugola50.model.game.GameStatus;
import it.polimi.sw.gianpaolocugola50.model.game.GamesManager;
import it.polimi.sw.gianpaolocugola50.model.game.Player;
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
        } //else {
        // roba per notificare player che esiste già un giocatore con quel nickName
        //}
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

    public void abandonCurrentGame() {
        if (player.getCurrentGame() != null) {
            player.getCurrentGame().removePlayer(player);
            player.setCurrentGame(null);
        }
    }

    public void placeCard(int index, boolean face, int x, int y) {
        if (isPlaying()) {
            List<PhysicalCard> playerHand = player.getPlayerData().getHand();
            if (index >= 0 && index < playerHand.size()) {
                PlayableCard card = face ? playerHand.get(index).getFront() : playerHand.get(index).getBack();
                if (isPlayerTurn() && card.isPlaceable(player.getPlayerData(), x, y)) {
                    player.getPlayerData().placeCard(card, x, y);
                    player.getPlayerData().removeCard(index);
                } //else {
                // roba per notificare player che la carta non è piazzabile
                //}
            } //else {
            // indice non valido
            //}
        } //else {
        // operazione non valida
        //}
    }

    public void drawCard(DrawingPosition position) {
        if (isPlaying()) {
            if (isPlayerTurn()) {
                player.getCurrentGame().playerDraw(player, position);
            } //else {
            // non posso pescare adesso
            //}
        } //else {
        // operazione non valida
        //}
    }

    private boolean isPlaying() {
        return player.getCurrentGame() != null;
    }

    private boolean isPlayerTurn() {
        return player.getCurrentGame().getStatus().equals(GameStatus.PLAYING)
                && player.getCurrentGame().getCurrentPlayer() == player;
    }
}
