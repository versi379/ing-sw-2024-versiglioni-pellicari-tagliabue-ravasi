package it.polimi.sw.GC50.model.objectives;

import it.polimi.sw.GC50.model.cards.CardType;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;


public class ObjectiveCard implements Serializable {
    private final String code;
    private final int pointsPerCompletion;
    private final Objective objective;

    /**
     * Constructs an instance of Objective Card
     *
     * @param code                specify the associated .jpg
     * @param pointsPerCompletion points necessaries for complete the objective
     * @param objective           objective to reach
     */
    public ObjectiveCard(String code, int pointsPerCompletion, Objective objective) {
        this.code = code;
        this.pointsPerCompletion = pointsPerCompletion;
        this.objective = objective;
    }

    public String getCode() {
        return code;
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
     *
     * @param playerData player's information
     * @return if objective is reached
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

    // TEST ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ObjectiveCard(int pointsPerCompletion, Objective objective) {
        code = "-1";
        this.pointsPerCompletion = pointsPerCompletion;
        this.objective = objective;
    }
}
