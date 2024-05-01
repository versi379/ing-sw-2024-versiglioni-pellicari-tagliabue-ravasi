package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectiveCardTest {

    @Test
    void objectiveCardTest() {
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTL);
        ObjectiveCard objectiveCard = new ObjectiveCard(2, caveObjective);
        assertEquals(objectiveCard.getPointsPerCompletion(), 2);
        assertEquals(objectiveCard.getObjective(), caveObjective);
    }

    @Test
    void checkObjective() {
        //da riguardare
        PlayerData playerData = new PlayerData(5);
        CaveObjective caveObjective = new CaveObjective(Color.RED, Color.BLUE, CaveOrientation.UPRIGHTL);
        ObjectiveCard objectiveCard = new ObjectiveCard(2, caveObjective);
        assertEquals(objectiveCard.getPointsPerCompletion() * caveObjective.checkCondition(playerData), 0);

    }
}