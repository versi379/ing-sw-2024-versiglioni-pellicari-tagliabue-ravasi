package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdenticalResourcesObjectiveTest {

    @Test
    void testIdenticalResourcesObjectiveConstructor() {
        IdenticalResourcesObjective identicalResourcesObjective = new IdenticalResourcesObjective(Resource.PLANT, 1);

        assertEquals(identicalResourcesObjective.getTargetResource(), Resource.PLANT);
        assertEquals(identicalResourcesObjective.getCount(), 1);
    }

    @Test
    void testCheckCondition() {
        PlayerData playerData = new PlayerData(2);
        Map<Resource, Integer> testResourceCount = new EnumMap<>(Resource.class);
        testResourceCount.put(Resource.PLANT, 5);
        playerData.setNumOfResources(testResourceCount);
        IdenticalResourcesObjective identicalResourcesObjective = new IdenticalResourcesObjective(Resource.PLANT, 2);

        assertEquals(2, identicalResourcesObjective.checkCondition(playerData));
    }
}
