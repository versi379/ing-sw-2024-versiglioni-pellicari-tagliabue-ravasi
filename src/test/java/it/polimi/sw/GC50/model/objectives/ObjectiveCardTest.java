package it.polimi.sw.GC50.model.objectives;

import it.polimi.sw.GC50.model.cards.Color;
import it.polimi.sw.GC50.model.cards.Resource;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectiveCardTest {

    @Test
    void TestObjectiveCardFullConstructor() {
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTL);
        ObjectiveCard objectiveCard = new ObjectiveCard("1", 2, caveObjective);

        assertEquals("1", objectiveCard.getCode());
        assertEquals(objectiveCard.getPointsPerCompletion(), 2);
        assertEquals(objectiveCard.getObjective(), caveObjective);
    }

    @Test
    void TestObjectiveCardNoCodeConstructor() {
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTL);
        ObjectiveCard objectiveCard = new ObjectiveCard(2, caveObjective);

        assertEquals(objectiveCard.getPointsPerCompletion(), 2);
        assertEquals(objectiveCard.getObjective(), caveObjective);
    }

    @Test
    void testCheckObjective() {
        PlayerData playerData = new PlayerData(2);
        Map<Resource, Integer> testResourceCount = new EnumMap<>(Resource.class);
        testResourceCount.put(Resource.PLANT, 5);
        playerData.setNumOfResources(testResourceCount);
        IdenticalResourcesObjective identicalResourcesObjective = new IdenticalResourcesObjective(Resource.PLANT, 2);
        ObjectiveCard objectiveCard = new ObjectiveCard(2, identicalResourcesObjective);

        assertEquals(4, objectiveCard.checkObjective(playerData));
    }
}
