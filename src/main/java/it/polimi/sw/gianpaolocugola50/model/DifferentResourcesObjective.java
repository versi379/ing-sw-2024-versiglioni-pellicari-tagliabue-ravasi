package it.polimi.sw.gianpaolocugola50.model;
import java.util.*;

public class DifferentResourcesObjective implements Objective {
    private final Set<Resource> targetResources;

    @Override
    public int checkCondition(PlayerData playerData) {
        return targetResources.stream()
                .map(playerData::numOfResource)
                .min((a, b) -> a - b)
                .orElse(0);
    }
}
