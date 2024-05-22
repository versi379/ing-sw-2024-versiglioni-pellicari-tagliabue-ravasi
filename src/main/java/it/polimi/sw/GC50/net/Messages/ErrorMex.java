package it.polimi.sw.GC50.net.Messages;

import it.polimi.sw.GC50.model.lobby.Player;

public class ErrorMex implements Message {
    private final String nickname;
    private final String content;
    public ErrorMex(Player player, String content) {
        nickname = player.getNickname();
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public String getContent() {
        return content;
    }
}
