package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.Set;

/**
 *
 */
public class DifferentResourcesObjective implements Objective , Serializable {
    private final Set<Resource> targetResources;

    public DifferentResourcesObjective(Set<Resource> targetResources) {
        this.targetResources = targetResources;
    }

    public Set<Resource> getTargetResources() {
        return targetResources;
    }

    @Override
    public int checkCondition(PlayerData playerData) {
        return targetResources.stream()
                .map(playerData::numOfResource)
                .min(Integer::compareTo)
                .orElse(0);
    }

    @Override
    public String toStringTUI() {
        String result = "Different Resources Objective => target resources:";
        for (Resource resource : getTargetResources()) {
            result += (" " + resource);
        }
        return result;
    }
}
