package it.polimi.sw.GC50.model.lobby;

import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.game.PlayerStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class Player {

    /**
     * Player's unique nickname
     */
    private final String nickname;

    //Status of the connection to the server of the player
    private PlayerStatus status;


    public Player(String nickname) {
        this.nickname = nickname;
        status = PlayerStatus.DISCONNECTED;
    }

    public String getNickname() {
        return nickname;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public PlayerStatus getStatus() {
        return status;
    }

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
