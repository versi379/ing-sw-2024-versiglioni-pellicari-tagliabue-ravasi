package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoveredCornersBonusTest {

    @Test
    void checkBonus() {
        CoveredCornersBonus bonus = new CoveredCornersBonus();
        PlayerData playerData = new PlayerData(40);
        Corner[] corners = new Corner[]
                {CornerTest.emptyCorner, CornerTest.emptyCorner, CornerTest.emptyCorner, CornerTest.emptyCorner};
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);
        CoveredCornersBonus coveredCornersBonus = new CoveredCornersBonus();
        assertEquals(0,coveredCornersBonus.checkBonus(card,playerData, 1,2));
    }
}