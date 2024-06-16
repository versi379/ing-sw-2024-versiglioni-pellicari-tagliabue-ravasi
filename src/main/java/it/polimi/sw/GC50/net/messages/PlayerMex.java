package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.lobby.Player;

public class PlayerMex implements Message {
    private final String nickname;

    public PlayerMex(Player player) {
        nickname = player.getNickname();
    }

    public String getNickname() {
        return nickname;
    }
}
