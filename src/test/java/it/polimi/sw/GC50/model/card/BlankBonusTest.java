package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlankBonusTest {

    @Test
    void checkBonus() {
        BlankBonus bonus = new BlankBonus();
        PlayerData playerData = new PlayerData(40);
        Corner[] corners = new Corner[]
                {CornerTest.emptyCorner, CornerTest.emptyCorner, CornerTest.emptyCorner, CornerTest.emptyCorner};
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);
        assertEquals(1,bonus.checkBonus(card,playerData,1,2));

    }
}
