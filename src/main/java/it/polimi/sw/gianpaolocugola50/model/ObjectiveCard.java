package it.polimi.sw.gianpaolocugola50.model;

public class ObjectiveCard {
    private final int pointsPerCompletion;
    private final Objective objective;

    public int checkObjective(PlayerData playerData) {
        return pointsPerCompletion*objective.checkCondition(playerData);
    }
}
