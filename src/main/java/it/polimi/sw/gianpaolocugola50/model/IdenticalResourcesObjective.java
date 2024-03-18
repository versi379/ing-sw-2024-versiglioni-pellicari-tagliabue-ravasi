package it.polimi.sw.gianpaolocugola50.model;

public class IdenticalResourcesObjective implements Objective {
    private final Resource targetResource;
    private final int count;

    @Override
    public int checkCondition(PlayerData playerData) {
        return playerData.numOfResource(targetResource) / count;
    }
}
