package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.game.PlayerData;

/**
 *
 */
public class IdenticalResourcesObjective implements Objective {
    private final Resource targetResource;
    private final int count;

    public IdenticalResourcesObjective(Resource targetResource, int count) {
        this.targetResource = targetResource;
        this.count = count;
    }

    public Resource getTargetResource() {
        return targetResource;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int checkCondition(PlayerData playerData) {

        return playerData.numOfResource(targetResource) / count;
    }

}
