package it.polimi.sw.GC50.model.objectives;

import it.polimi.sw.GC50.model.cards.Resource;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;


public class IdenticalResourcesObjective implements Objective , Serializable {
    private final Resource targetResource;
    private final int count;

    /**
     * Constructs an instance of IdenticalResourcesObjective
     * @param targetResource    resource that we need
     * @param count             number of resource requested
     */
    public IdenticalResourcesObjective(Resource targetResource, int count) {
        this.targetResource = targetResource;
        this.count = count;
    }

    /**
     * @return target resource
     */
    public Resource getTargetResource() {
        return targetResource;
    }

    /**
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param playerData    player data selected
     * @return number of target resource required / number of resource that we have
     */
    @Override
    public int checkCondition(PlayerData playerData) {

        return playerData.numOfResource(targetResource) / count;
    }

    @Override
    public String toStringTUI() {
        return "Identical Resources Objective => target resource: " + getTargetResource().toStringTUI() +
                ", count: " + getCount();
    }
}
