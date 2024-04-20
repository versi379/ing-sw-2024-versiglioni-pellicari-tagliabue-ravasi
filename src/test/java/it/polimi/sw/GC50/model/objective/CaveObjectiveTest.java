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

    /**
     * Check matrix operations to be performed on CardsMatrix,
     * with respect to different cave orientation cases
     */
    @Test
    void testAdjustOrientation() {
        // Create a test CardsMatrix
        Corner emptyCorner = new Corner(CornerStatus.EMPTY, null);
        Corner[] corners = new Corner[]{emptyCorner, emptyCorner, emptyCorner, emptyCorner};
        PlayableCard greenPlayableCard = new PlayableCard(Color.GREEN, 1, corners);
        PlayableCard bluePlayableCard = new PlayableCard(Color.BLUE, 1, corners);
        PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
        PlayableCard[][] testPlayableCards = {
                {greenPlayableCard, greenPlayableCard, bluePlayableCard, bluePlayableCard},
                {redPlayableCard, redPlayableCard, greenPlayableCard, greenPlayableCard},
                {greenPlayableCard, greenPlayableCard, bluePlayableCard, bluePlayableCard},
                {redPlayableCard, redPlayableCard, greenPlayableCard, greenPlayableCard},
        };
        CardsMatrix testCardsMatrix = new CardsMatrix(testPlayableCards.length);
        testCardsMatrix.setCardsMatrix(testPlayableCards);

        // Test when orientation is INVERTEDL
        CaveObjective caveObjectiveInvertedL = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.INVERTEDL);
        CardsMatrix invertedLResult = caveObjectiveInvertedL.adjustOrientation(testCardsMatrix);
        // Assert that the result is transposed
        assertEquals(testCardsMatrix.transpose(), invertedLResult);

        // Test when orientation is INVERTEDJ
        CaveObjective caveObjectiveInvertedJ = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.INVERTEDJ);
        CardsMatrix invertedJResult = caveObjectiveInvertedJ.adjustOrientation(testCardsMatrix);
        // Assert that the result is inverted
        assertEquals(testCardsMatrix.invert(), invertedJResult);

        // Test when orientation is UPRIGHTJ
        CaveObjective caveObjectiveUprightJ = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTJ);
        CardsMatrix uprightJResult = caveObjectiveUprightJ.adjustOrientation(testCardsMatrix);
        // Assert that the result is transposed and then inverted
        assertEquals(testCardsMatrix.transpose().invert(), uprightJResult);

        // Test when orientation is UPRIGHTL
        CaveObjective caveObjectiveUprightL = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTL);
        CardsMatrix defaultResult = caveObjectiveUprightL.adjustOrientation(testCardsMatrix);
        // Assert that the result is the same as the input matrix
        assertEquals(testCardsMatrix, defaultResult);
    }

    /**
     * Test InvertedL cave condition, where the two bottom cards are blue,
     * and the top card is red
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
                {genericPlayableCard, genericPlayableCard, bluePlayableCard, redPlayableCard, genericPlayableCard},
                {genericPlayableCard, bluePlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard},
                {genericPlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard},
                {genericPlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard, genericPlayableCard}
        };
        CardsMatrix testCardsMatrix = new CardsMatrix(testPlayableCards.length);
        testCardsMatrix.setCardsMatrix(testPlayableCards);
        PlayerData testPlayerData = new PlayerData(testCardsMatrix);
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.INVERTEDL);
        assertEquals(3, caveObjective.checkCondition(testPlayerData));
    }

}