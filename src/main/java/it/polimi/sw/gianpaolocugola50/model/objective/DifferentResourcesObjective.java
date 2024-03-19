package it.polimi.sw.gianpaolocugola50.model.objective;
import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;
import it.polimi.sw.gianpaolocugola50.model.card.Resource;

import java.util.*;

public class DifferentResourcesObjective implements Objective {
    private final Set<Resource> targetResources;

    public DifferentResourcesObjective(Set<Resource> targetResources) {
        this.targetResources = targetResources;
    }

    @Override
    public int checkCondition(PlayerData playerData) {
        return targetResources.stream()
                .map(playerData::numOfResource)
                .min((a, b) -> a - b)
                .orElse(0);
    }
}
