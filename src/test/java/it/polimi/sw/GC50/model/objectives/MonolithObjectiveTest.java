package it.polimi.sw.GC50.model.objectives;

import it.polimi.sw.GC50.model.cards.Color;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import static it.polimi.sw.GC50.model.cards.PlayableCardTest.redPlayableCard;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonolithObjectiveTest {

    @Test
    void monolithObjectiveTest() {
        MonolithObjective monolithObjective = new MonolithObjective(Color.PURPLE, MonolithOrientation.LEFTDIAGONAL);

        assertEquals(monolithObjective.getTargetColor(), Color.PURPLE);
        assertEquals(monolithObjective.getOrientation(), MonolithOrientation.LEFTDIAGONAL);
    }

    @Test
    void testRightDiagonalCondition() {
        PlayerData testPlayerData = new PlayerData(rightDiagonalMatrix());
        MonolithObjective monolithObjective = new MonolithObjective(Color.RED, MonolithOrientation.RIGHTDIAGONAL);

        assertEquals(1, monolithObjective.checkCondition(testPlayerData));
    }

    @Test
    void testLeftDiagonalCondition() {
        PlayerData testPlayerData = new PlayerData(leftDiagonalMatrix());
        MonolithObjective monolithObjective = new MonolithObjective(Color.RED, MonolithOrientation.LEFTDIAGONAL);

        assertEquals(1, monolithObjective.checkCondition(testPlayerData));
    }

    public static CardsMatrix rightDiagonalMatrix() {
        PlayableCard[][] playableCardsMatrix = new PlayableCard[][]{
                {null, null, null, null, null},
                {null, null, null, redPlayableCard, null},
                {null, null, redPlayableCard, null, null},
                {null, redPlayableCard, null, null, null},
                {null, null, null, null, null}
        };
        CardsMatrix testMatrix = new CardsMatrix(playableCardsMatrix.length);
        for (int i = 0; i < playableCardsMatrix.length; i++) {
            for (int j = 0; j < playableCardsMatrix[i].length; j++) {
                if (playableCardsMatrix[i][j] != null) {
                    testMatrix.insert(playableCardsMatrix[i][j], i, j);
                }
            }
        }
        return testMatrix;
    }

    public static CardsMatrix leftDiagonalMatrix() {
        PlayableCard[][] playableCardsMatrix = new PlayableCard[][]{
                {null, null, null, null, null},
                {null, redPlayableCard, null, null, null},
                {null, null, redPlayableCard, null, null},
                {null, null, null, redPlayableCard, null},
                {null, null, null, null, null}
        };
        CardsMatrix testMatrix = new CardsMatrix(playableCardsMatrix.length);
        for (int i = 0; i < playableCardsMatrix.length; i++) {
            for (int j = 0; j < playableCardsMatrix[i].length; j++) {
                if (playableCardsMatrix[i][j] != null) {
                    testMatrix.insert(playableCardsMatrix[i][j], i, j);
                }
            }
        }
        return testMatrix;
    }
}
