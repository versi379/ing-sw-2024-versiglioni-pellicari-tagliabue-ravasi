package it.polimi.sw.GC50.model.lobby;

import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.game.PlayerStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Contains infos about player like nickname, status and current game
 */
public class Player {

    /**
     * Player's unique nickname
     */
    private final String nickname;
    private Game currentGame;

    //Status of the connection to the server of the player
    private PlayerStatus status;

    /**
     * Constructs a new instance of Player with default status as DISCONNECTED
     * @param nickname of the player instance
     */

    public Player(String nickname) {
        this.nickname = nickname;
        status = PlayerStatus.DISCONNECTED;
    }

    /**
     * Return player's nickname
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the game as current game
     * @param game
     */
    public void setCurrentGame(Game game) {
        currentGame = game;
    }

    /**
     * Returns the current game
     * @return
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * Sets player's status
     * @param status
     */
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }
    /**
     * Returns player's status
     */
    public PlayerStatus getStatus() {
        return status;
    }

    /**
     * Compare nicknames between players
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Player player)) {
            return false;
        }
        return getNickname().equals(player.getNickname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNickname());
    }

    @Override
    public String toString() {
        return getNickname();
    }
}
