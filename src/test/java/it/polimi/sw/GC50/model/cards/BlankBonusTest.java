package it.polimi.sw.GC50.model.cards;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import static it.polimi.sw.GC50.model.cards.PlayableCardTest.redPlayableCard;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
