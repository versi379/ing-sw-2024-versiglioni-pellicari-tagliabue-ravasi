package it.polimi.sw.GC50.model.lobby;

import java.util.Objects;

/**
 * contains player data like nickname and status
 */
public class Player {

    /**
     * Player's unique nickname
     */
    private final String nickname;

    /**
     * Constructs an instance of player with nickname
     *
     * @param nickname player's nickname
     */
    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return nickname of a player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * compare nicknames between players
     *
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
