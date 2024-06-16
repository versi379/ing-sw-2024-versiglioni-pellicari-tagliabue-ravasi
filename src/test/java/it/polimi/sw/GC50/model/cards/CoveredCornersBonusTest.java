package it.polimi.sw.GC50.model.cards;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import static it.polimi.sw.GC50.model.cards.PlayableCardTest.redPlayableCard;
import static it.polimi.sw.GC50.model.cards.PlayableCardTest.whitePlayableCard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoveredCornersBonusTest {

    @Test
    void testCheckBonusMinCoveredCorners() {
        PlayerData playerData = new PlayerData(2);
        playerData.placeCard(whitePlayableCard, 1, 1);
        CoveredCornersBonus coveredCornersBonus = new CoveredCornersBonus();

        assertEquals(1, coveredCornersBonus.checkBonus(redPlayableCard, playerData, 2, 2));
    }

    @Test
    void testCheckBonusMaxCoveredCorners() {
        PlayerData playerData = new PlayerData(2);
        playerData.placeCard(whitePlayableCard, 1, 3);
        playerData.placeCard(whitePlayableCard, 3, 3);
        playerData.placeCard(whitePlayableCard, 3, 1);
        playerData.placeCard(whitePlayableCard, 1, 1);
        CoveredCornersBonus coveredCornersBonus = new CoveredCornersBonus();

        assertEquals(4, coveredCornersBonus.checkBonus(redPlayableCard, playerData, 2, 2));
    }

    @Test
    void testEquals() {
        CoveredCornersBonus coveredCornersBonus1 = new CoveredCornersBonus();
        Bonus coveredCornersBonus2 = new CoveredCornersBonus();

        assertTrue(coveredCornersBonus1.equals(coveredCornersBonus2));
        assertEquals(coveredCornersBonus1.hashCode(), coveredCornersBonus2.hashCode());
    }
}
