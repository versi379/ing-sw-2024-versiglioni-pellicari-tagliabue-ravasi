package it.polimi.sw.gianpaolocugola50.model;
import java.util.*;

public class DifferentResourcesObjective implements Objective {
    private final Set<Resource> targetResources;

    @Override
    public int checkCondition(PlayerData playerData) {
        // fa schifo ma è giusto per far capire il concetto
        int result = 100;

        for(Resource resource : targetResources) {
            if(playerData.numOfResource(resource) < result) {
                result = playerData.numOfResource(resource);
            }
        }
        return result;
    }
}
