package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.sw.GC50.model.card.CornerTest.plantCorner;
import static it.polimi.sw.GC50.model.card.PlayableCardTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class ResourcesBonusTest {
    @Test
    void testResourceBonusConstructor(){
        ResourcesBonus resourcesBonus = new ResourcesBonus(Resource.PLANT);

        assertEquals(resourcesBonus.getTargetResource(),Resource.PLANT);
    }
    @Test
    void testCheckBonusZeroResources() {
        PlayerData playerData = new PlayerData(2);
        ResourcesBonus coveredCornersBonus = new ResourcesBonus(Resource.PLANT);

        assertEquals(0, coveredCornersBonus.checkBonus(redPlayableCard, playerData, 2, 2));
    }

    @Test
    void testCheckBonusPositiveResources() {
        PlayerData playerData = new PlayerData(2);
        Corner[] testCorners = corners.clone();
        testCorners[1] = plantCorner;
        testCorners[2] = plantCorner;
        PlayableCard card = new PlayableCard(Color.GREEN, 1,
                new ArrayList<>(List.of(Resource.PLANT)), testCorners);
        playerData.placeCard(card, 2, 2);
        ResourcesBonus coveredCornersBonus = new ResourcesBonus(Resource.PLANT);

        assertEquals(5, coveredCornersBonus.checkBonus(card, playerData, 3, 3));
    }

    @Test
    void testEquals() {
        ResourcesBonus resourcesBonus1 = new ResourcesBonus(Resource.PLANT);
        Bonus resourcesBonus2 = new ResourcesBonus(Resource.PLANT);
        Bonus resourcesBonus3 = new ResourcesBonus(Resource.ANIMAL);

        assertTrue(resourcesBonus1.equals(resourcesBonus2));
        assertEquals(resourcesBonus1.hashCode(), resourcesBonus2.hashCode());
        assertFalse(resourcesBonus1.equals(resourcesBonus3));
        assertNotEquals(resourcesBonus1.hashCode(), resourcesBonus3.hashCode());
    }
}
