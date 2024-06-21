package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

/**
 * class that informs when a player joins a game
 */
public class PlayerJoinedMex implements Message {
    private final String nickname;
    private final int playersLeft;

    /**
     * Constructs an instance of PlayerJoinedMex
     * @param game      where player joins
     * @param player    nickname of the player
     */
    public PlayerJoinedMex(Game game, Player player) {
        playersLeft = game.getNumPlayers() - game.getPlayerList().size();
        nickname = player.getNickname();
    }

    /**
     * @return  player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return number of player that have left the game
     */
    public int getPlayersLeft() {
        return playersLeft;
    }
}
