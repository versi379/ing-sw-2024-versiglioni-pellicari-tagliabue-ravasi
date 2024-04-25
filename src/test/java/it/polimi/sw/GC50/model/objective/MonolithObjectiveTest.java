package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonolithObjectiveTest {
    @Test
    void monolithObjectiveTest(){
        MonolithObjective monolithObjective = new MonolithObjective(Color.PURPLE, MonolithOrientation.LEFTDIAGONAL);
        assertEquals(monolithObjective.getTargetColor(),Color.PURPLE);
        assertEquals(monolithObjective.getOrientation(),MonolithOrientation.LEFTDIAGONAL);
    }


    @Test
    void checkCondition() {
        PlayerData playerData = new PlayerData(5);
        MonolithObjective monolithObjective = new MonolithObjective(Color.PURPLE,MonolithOrientation.LEFTDIAGONAL);
        assertEquals(monolithObjective.checkCondition(playerData),0);
    }
}