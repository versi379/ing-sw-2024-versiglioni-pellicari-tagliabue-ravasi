package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.game.PlayerData;

public class ObjectiveCard {
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
}
