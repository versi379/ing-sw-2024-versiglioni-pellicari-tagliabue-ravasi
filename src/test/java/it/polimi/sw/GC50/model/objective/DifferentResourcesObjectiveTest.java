package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DifferentResourcesObjectiveTest {

    @Test
    void testDifferentResourcesObjectiveConstructor() {
        Set<Resource> resources = new HashSet<>(Arrays.asList(Resource.PLANT, Resource.ANIMAL, Resource.INK));
        DifferentResourcesObjective differentResourcesObjective = new DifferentResourcesObjective(resources);

        assertEquals(resources, differentResourcesObjective.getTargetResources());
    }

    @Test
    void testCheckCondition() {
        PlayerData playerData = new PlayerData(2);
        Map<Resource, Integer> testResourceCount = new EnumMap<>(Resource.class);
        testResourceCount.put(Resource.PLANT, 5);
        testResourceCount.put(Resource.ANIMAL, 2);
        testResourceCount.put(Resource.INK, 3);
        playerData.setNumOfResources(testResourceCount);
        Set<Resource> resources = new HashSet<>(Arrays.asList(Resource.PLANT, Resource.ANIMAL, Resource.INK));
        DifferentResourcesObjective differentResourcesObjective = new DifferentResourcesObjective(resources);

        assertEquals(2, differentResourcesObjective.checkCondition(playerData));
    }
}
