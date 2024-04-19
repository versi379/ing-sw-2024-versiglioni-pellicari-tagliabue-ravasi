package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.*;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaveObjectiveTest {

    @Test
    void testCaveObjectiveConstructor() {
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.INVERTEDL);
        assertEquals(Color.BLUE, caveObjective.getTargetColor1());
        assertEquals(Color.RED, caveObjective.getTargetColor2());
        assertEquals(CaveOrientation.INVERTEDL, caveObjective.getOrientation());
    }

    @Test
    void testAdjustOrientation() {

    }

    /**
     *
     */
    @Test
    void testInvertedLCondition() {
        Corner emptyCorner = new Corner(CornerStatus.EMPTY, null);
        Corner[] corners = new Corner[]{emptyCorner, emptyCorner, emptyCorner, emptyCorner};
        PlayableCard genericPlayableCard = new PlayableCard(Color.GREEN, 1, corners);
        PlayableCard bluePlayableCard = new PlayableCard(Color.BLUE, 1, corners);
        PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
        PlayableCard[][] testPlayableCards = {
                {genericPlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard},
                {genericPlayableCard, genericPlayableCard, redPlayableCard, genericPlayableCard, genericPlayableCard},
                {genericPlayableCard, bluePlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard},
                {genericPlayableCard, bluePlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard},
                {genericPlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard}
        };
        CardsMatrix testCardsMatrix = new CardsMatrix(testPlayableCards.length);
        testCardsMatrix.setCardsMatrix(testPlayableCards);
        PlayerData testPlayerData = new PlayerData(testCardsMatrix);
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.INVERTEDL);
        assertEquals(3, caveObjective.checkCondition(testPlayerData));
    }

    @Test
    void getTargetColor1() {
    }

    @Test
    void getTargetColor2() {
    }

    @Test
    void getOrientation() {
    }

    @Test
    void checkCondition() {
    }
}