package it.polimi.sw.GC50.model.lobby;

import it.polimi.sw.GC50.model.game.PlayerStatus;

import java.util.Objects;

/**
 contains player data like nickname and status
 */
public class Player {

    /**
     * Player's unique nickname
     */
    private final String nickname;

    /**
     * Status of the connection to the server of the player
     */
    private PlayerStatus status;

    /**
     * Constructs an instance of player with nickname
     * @param nickname  player's nickname
     */
    public Player(String nickname) {
        this.nickname = nickname;
        status = PlayerStatus.DISCONNECTED;
    }

    /**
     * @return nickname of a player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * set player status
     * @param status of the player
     */
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    /**

     * @return player status
     */
    public PlayerStatus getStatus() {
        return status;
    }

    /**
     * compare nicknames between players
     * @param obj object
     * @return a boolean
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
