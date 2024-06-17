package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

public class PlayerJoinedMex implements Message {
    private final String nickname;
    private final int playersLeft;

    public PlayerJoinedMex(Game game, Player player) {
        playersLeft = game.getNumPlayers() - game.getPlayerList().size();
        nickname = player.getNickname();
    }

    public String getNickname() {
        return nickname;
    }

    public int getPlayersLeft() {
        return playersLeft;
    }
}
