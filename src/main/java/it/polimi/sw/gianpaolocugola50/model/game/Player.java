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
        currentGame = null;
        status = PlayerStatus.DISCONNECTED;
    }

    public String getNickName() {
        return nickName;
    }

    public void setCurrentGame(Game game) {
        currentGame = game;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public PlayerData getPlayerData() {
        return getCurrentGame().getPlayerData(this);
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
        return getNickName().equals(player.getNickName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNickName());
    }

    @Override
    public String toString() {
        return getNickName();
    }
}
