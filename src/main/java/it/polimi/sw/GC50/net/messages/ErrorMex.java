package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.lobby.Player;

/**
 * class that sends a message when there is an error
 */
public class ErrorMex implements Message {
    private final String nickname;
    private final String content;

    /**
     * Constructs an instance of ErrorMex
     * @param player    who gets an error messages
     * @param content   of the message
     */
    public ErrorMex(Player player, String content) {
        nickname = player.getNickname();
        this.content = content;
    }

    /**
     * Returns player's nickname
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns content of the message
     * @return
     */
    public String getContent() {
        return content;
    }
}
