package it.polimi.sw.gianpaolocugola50.model.objective;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;
import it.polimi.sw.gianpaolocugola50.model.card.Resource;

public class IdenticalResourcesObjective implements Objective {
    private final Resource targetResource;
    private final int count;

    public IdenticalResourcesObjective(Resource targetResource, int count) {
        this.targetResource = targetResource;
        this.count = count;
    }

    @Override
    public int checkCondition(PlayerData playerData) {
        return playerData.numOfResource(targetResource) / count;
    }
}