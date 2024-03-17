package it.polimi.sw.gianpaolocugola50.model;

public class IdenticalResourcesObjective implements Objective {
    private Resource targetResource;
    private int count;

    @Override
    public int checkCondition(PlayerData playerData) {
        return playerData.numOfResource(targetResource) / count;
    }
}
