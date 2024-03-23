package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.objective.ObjectiveCard;

public class Player {
    //name of the player and is also used like an id, no double name during all the game
    private final String nickName;

    //the personal gameArea of the player
    private PlayerData playerArea;

    //Status of the connection to the server of the player
    private PlayerStatus status;

    //personalGoal
    private ObjectiveCard secreteObjctive;


    public Player(String nickName) {
        this.nickName = nickName;
        this.playerArea = null;
        this.status = PlayerStatus.DISCONNECTED;
        this.secreteObjctive = null;
    }

    public String getNickName() {
        return nickName;
    }

    public PlayerData getPlayerData() {
        return playerArea;
    }

    public void setPlayerData(PlayableCard starterCard, int deckSize) {
        this.playerArea = new PlayerData(starterCard, deckSize);
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public void setSecreteObjctive(ObjectiveCard secreteObjctive) {
        this.secreteObjctive = secreteObjctive;
    }

    public ObjectiveCard getSecreteObjctive() {
        return secreteObjctive;
    }
}
