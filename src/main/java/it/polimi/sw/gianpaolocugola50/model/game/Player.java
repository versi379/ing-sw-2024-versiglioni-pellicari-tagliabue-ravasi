package it.polimi.sw.gianpaolocugola50.model.game;

import java.util.Objects;

public class Player {
    //name of the player and is also used like an id, no double name during all the game
    private final String nickName;

    //the personal gameArea of the player
    private Game currentGame;

    //Status of the connection to the server of the player
    private PlayerStatus status;


    public Player(String nickName) {
        this.nickName = nickName;
        this.currentGame = null;
        this.status = PlayerStatus.DISCONNECTED;
    }

    public String getNickName() {
        return nickName;
    }

    public void setCurrentGame(Game game) {
        currentGame = game;
    }

    public PlayerData getPlayerData() {
        return currentGame.getPlayerData(this);
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
        return nickName.equals(player.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }
}
