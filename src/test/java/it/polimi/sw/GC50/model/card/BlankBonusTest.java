package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.sw.GC50.model.card.PlayableCardTest.redPlayableCard;
import static it.polimi.sw.GC50.model.card.PlayableCardTest.whitePlayableCard;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlankBonusTest {

    @Test
    void testCheckBonus() {
        PlayerData playerData = new PlayerData(2);
        BlankBonus blankBonus = new BlankBonus();

        assertEquals(1, blankBonus.checkBonus(redPlayableCard, playerData, 2, 2));
    }

    @Test
    void testEquals() {
        BlankBonus blankBonus1 = new BlankBonus();
        Bonus blankBonus2 = new BlankBonus();

        assertTrue(blankBonus1.equals(blankBonus2));
        assertEquals(blankBonus1.hashCode(), blankBonus2.hashCode());
    }
}
