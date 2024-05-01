package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourcesBonusTest {
    @Test
    void ResourceBonusTest(){
        ResourcesBonus resourcesBonus = new ResourcesBonus(Resource.PLANT);
        assertEquals(resourcesBonus.getTargetResource(),Resource.PLANT);
    }
    @Test

    void checkBonus() {

    }
}
