package it.polimi.sw.GC50.net.messages;

import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;

public class PlayerReadyMex implements Message {
    private final String nickname;
    private final ObjectiveCard secretObjective;
    private final CardsMatrix cardsMatrix;
    private final int totalScore;

    public PlayerReadyMex(Game game, Player player) {
        nickname = player.getNickname();
        secretObjective = game.getSecretObjective(player);
        cardsMatrix = game.getCardsArea(player);
        totalScore = game.getTotalScore(player);
    }

    public String getNickname() {
        return nickname;
    }

    public ObjectiveCard getSecretObjective() {
        return secretObjective;
    }

    public CardsMatrix getCardsMatrix() {
        return cardsMatrix.copy();
    }

    public int getTotalScore() {
        return totalScore;
    }
}
