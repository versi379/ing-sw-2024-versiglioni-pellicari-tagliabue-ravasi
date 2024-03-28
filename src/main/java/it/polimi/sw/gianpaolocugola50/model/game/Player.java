package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import it.polimi.sw.gianpaolocugola50.model.objective.ObjectiveCard;

import java.util.List;

public class Player {
    //name of the player and is also used like an id, no double name during all the game
    private final String nickName;

    //the personal gameArea of the player
    private PlayerData playerArea;

    //Status of the connection to the server of the player
    private PlayerStatus status;


    public Player(String nickName) {
        this.nickName = nickName;
        this.playerArea = null;
        this.status = PlayerStatus.DISCONNECTED;
    }

    public String getNickName() {
        return nickName;
    }

    public PlayerData getPlayerData() {
        return playerArea;
    }

    public void setPlayerData(PhysicalCard starterCard, int deckSize, List<ObjectiveCard> commonObjectives, ObjectiveCard[] toChoseObjective) {
        this.playerArea = new PlayerData(starterCard, deckSize);
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }
}
