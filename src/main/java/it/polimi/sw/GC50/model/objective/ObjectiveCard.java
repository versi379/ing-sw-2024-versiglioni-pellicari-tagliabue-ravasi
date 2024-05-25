package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.CardType;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;


public class ObjectiveCard implements Serializable {
    private final int pointsPerCompletion;
    private final Objective objective;

    /**
     * Constructs an instance of Objective Card
     * @param pointsPerCompletion    points necessaries for complete the objective
     * @param objective              objective to reach
     */
    public ObjectiveCard(int pointsPerCompletion, Objective objective) {
        this.pointsPerCompletion = pointsPerCompletion;
        this.objective = objective;
    }

    /**
     * @return objective
     */
    public Objective getObjective() {
        return objective;
    }

    /**
     * @return points for complete the objective
     */
    public int getPointsPerCompletion() {
        return pointsPerCompletion;
    }

    /**
     * Returns if objective is reached
     * @param playerData
     * @return
     */
    public int checkObjective(PlayerData playerData) {
        return getPointsPerCompletion() * getObjective().checkCondition(playerData);
    }

    public String toStringTUI() {
        return getObjective().toStringTUI() + "\npoints per completion: " + getPointsPerCompletion();
    }

    /**
     * @return card type
     */
    public CardType getCardType() {
        return CardType.OBJECTIVE;
    }
}
