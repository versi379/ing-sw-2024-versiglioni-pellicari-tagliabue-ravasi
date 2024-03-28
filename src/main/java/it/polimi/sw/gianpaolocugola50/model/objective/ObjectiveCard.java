package it.polimi.sw.gianpaolocugola50.model.objective;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

public class ObjectiveCard {
    private final int pointsPerCompletion;
    private final Objective objective;

    public ObjectiveCard(int pointsPerCompletion, Objective objective) {
        this.pointsPerCompletion = pointsPerCompletion;
        this.objective = objective;
    }

    public int getPointsPerCompletion() {
        return pointsPerCompletion;
    }

    public int checkObjective(PlayerData playerData) {
        return pointsPerCompletion * objective.checkCondition(playerData);
    }
}
