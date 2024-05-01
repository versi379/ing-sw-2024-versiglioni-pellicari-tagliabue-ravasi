package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.sw.GC50.model.card.PlayableCardTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoveredCornersBonusTest {

    @Test
    void checkBonusZero() {
        PlayerData playerData = new PlayerData(2);
        CoveredCornersBonus coveredCornersBonus = new CoveredCornersBonus();

        assertEquals(0, coveredCornersBonus.checkBonus(redPlayableCard, playerData, 2, 2));
    }

    @Test
    void checkBonusOne() {
        PlayerData playerData = new PlayerData(2);
        playerData.placeCard(whitePlayableCard, 2, 2);
        CoveredCornersBonus coveredCornersBonus = new CoveredCornersBonus();

        assertEquals(1, coveredCornersBonus.checkBonus(redPlayableCard, playerData, 3, 3));
    }
}
