package it.polimi.sw.gianpaolocugola50.model;

public class Player {
    private final String nickname;
    private PlayerStatus status;
    private PlayerData playerData;

    public Player(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname(){
        return nickname;
    }
    public PlayerStatus getStatus() {
        return status;
    }
    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }
    public PlayerData getPlayerData() {
        return playerData;
    }
}
