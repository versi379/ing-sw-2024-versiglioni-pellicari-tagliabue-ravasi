package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.CardType;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;

/**
 *
 */
public class ObjectiveCard implements Serializable {
    private final int pointsPerCompletion;
    private final Objective objective;

    public ObjectiveCard(int pointsPerCompletion, Objective objective) {
        this.pointsPerCompletion = pointsPerCompletion;
        this.objective = objective;
    }

    public Objective getObjective() {
        return objective;
    }

    public int getPointsPerCompletion() {
        return pointsPerCompletion;
    }

    public int checkObjective(PlayerData playerData) {
        return pointsPerCompletion * objective.checkCondition(playerData);
    }

    public CardType getCardType() {
        return CardType.OBJECTIVE;
    }
}
