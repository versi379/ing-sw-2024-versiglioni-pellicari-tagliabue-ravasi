package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdenticalResourcesObjectiveTest {

    @Test
    void IdenticalResourcesObjective() {
        IdenticalResourcesObjective identicalResourcesObjective = new IdenticalResourcesObjective(Resource.PLANT, 1);
        assertEquals(identicalResourcesObjective.getTargetResource(), Resource.PLANT);
        assertEquals(identicalResourcesObjective.getCount(), 1);
    }

    @Test
    void checkCondition() {
        PlayerData playerData = new PlayerData(5);
        Map<Resource, Integer> map = new HashMap<Resource, Integer>();
        map.put(Resource.PLANT, 2);
        playerData.setNumOfResources(map);
        IdenticalResourcesObjective identicalResourcesObjective = new IdenticalResourcesObjective(Resource.PLANT, 2);
        assertEquals(identicalResourcesObjective.checkCondition(playerData), 1);
    }
}