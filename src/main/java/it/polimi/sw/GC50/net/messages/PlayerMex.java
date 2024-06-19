package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.lobby.Player;

public class PlayerMex implements Message {
    private final String nickname;

    /**
     * Constructs an instance of PlayerMex
     * @param player
     */
    public PlayerMex(Player player) {
        nickname = player.getNickname();
    }

    /**
     * Returns player's nickname
     * @return
     */
    public String getNickname() {
        return nickname;
    }
}
