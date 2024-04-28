package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.*;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import static it.polimi.sw.GC50.model.card.PlayableCardTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaveObjectiveTest {

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

    @Test
    void testUprightLCondition() {
        PlayerData testPlayerData = new PlayerData(uprightLMatrix());
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTL);

        assertEquals(1, caveObjective.checkCondition(testPlayerData));
    }

    @Test
    void testUprightJCondition() {
        PlayerData testPlayerData = new PlayerData(uprightJMatrix());
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.UPRIGHTJ);

        assertEquals(1, caveObjective.checkCondition(testPlayerData));
    }

    @Test
    void testInvertedLCondition() {
        PlayerData testPlayerData = new PlayerData(invertedLMatrix());
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.INVERTEDL);

        assertEquals(1, caveObjective.checkCondition(testPlayerData));
    }

    @Test
    void testInvertedJCondition() {
        PlayerData testPlayerData = new PlayerData(invertedJMatrix());
        CaveObjective caveObjective = new CaveObjective(Color.BLUE, Color.RED, CaveOrientation.INVERTEDJ);

        assertEquals(1, caveObjective.checkCondition(testPlayerData));
    }

    public static CardsMatrix uprightLMatrix() {
        PlayableCard[][] playableCardsMatrix = new PlayableCard[][]{
                {null, null, null, null, null},
                {null, redPlayableCard, bluePlayableCard, null, null},
                {null, null, null, bluePlayableCard, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
        };
        CardsMatrix testMatrix = new CardsMatrix(playableCardsMatrix.length);
        for (int i = 0; i < playableCardsMatrix.length; i++) {
            for (int j = 0; j < playableCardsMatrix[i].length; j++) {
                testMatrix.insert(playableCardsMatrix[i][j], i, j);
            }
        }
        return testMatrix;
    }

    public static CardsMatrix uprightJMatrix() {
        PlayableCard[][] playableCardsMatrix = new PlayableCard[][]{
                {null, null, null, null, null},
                {null, null, redPlayableCard, null, null},
                {null, null, bluePlayableCard, null, null},
                {null, null, null, bluePlayableCard, null},
                {null, null, null, null, null}
        };
        CardsMatrix testMatrix = new CardsMatrix(playableCardsMatrix.length);
        for (int i = 0; i < playableCardsMatrix.length; i++) {
            for (int j = 0; j < playableCardsMatrix[i].length; j++) {
                testMatrix.insert(playableCardsMatrix[i][j], i, j);
            }
        }
        return testMatrix;
    }

    public static CardsMatrix invertedLMatrix() {
        PlayableCard[][] playableCardsMatrix = new PlayableCard[][]{
                {null, null, null, null, null},
                {null, bluePlayableCard, null, null, null},
                {null, null, bluePlayableCard, null, null},
                {null, null, redPlayableCard, null, null},
                {null, null, null, null, null}
        };
        CardsMatrix testMatrix = new CardsMatrix(playableCardsMatrix.length);
        for (int i = 0; i < playableCardsMatrix.length; i++) {
            for (int j = 0; j < playableCardsMatrix[i].length; j++) {
                testMatrix.insert(playableCardsMatrix[i][j], i, j);
            }
        }
        return testMatrix;
    }

    public static CardsMatrix invertedJMatrix() {
        PlayableCard[][] playableCardsMatrix = new PlayableCard[][]{
                {null, null, null, null, null},
                {null, bluePlayableCard, null, null, null},
                {null, null, bluePlayableCard, redPlayableCard, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
        };
        CardsMatrix testMatrix = new CardsMatrix(playableCardsMatrix.length);
        for (int i = 0; i < playableCardsMatrix.length; i++) {
            for (int j = 0; j < playableCardsMatrix[i].length; j++) {
                testMatrix.insert(playableCardsMatrix[i][j], i, j);
            }
        }
        return testMatrix;
    }
}
