package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PlayableCard;
import org.junit.jupiter.api.Test;

import static it.polimi.sw.GC50.model.card.PlayableCardTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class CardsMatrixTest {

    @Test
    void testCardsMatrixConstructor() {
        CardsMatrix cardsMatrix = new CardsMatrix(80);

        assertEquals(80, cardsMatrix.length());
        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                assertNull(cardsMatrix.get(i, j));
                assertEquals(-1, cardsMatrix.getOrderAtCornerCoordinates(i, j));
            }
        }


        cardsMatrix = testCardsMatrix();
        PlayableCard[][] testMatrix = testMatrix();

        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                assertEquals(testMatrix[i][j], cardsMatrix.get(i, j));
            }
        }
    }

    @Test
    void testCopy() {
        CardsMatrix cardsMatrix1 = testCardsMatrix();
        CardsMatrix cardsMatrix2 = cardsMatrix1.copy();

        for (int i = 0; i < cardsMatrix2.length(); i++) {
            for (int j = 0; j < cardsMatrix2.length(); j++) {
                assertEquals(cardsMatrix1.get(i, j), cardsMatrix2.get(i, j));
                assertEquals(cardsMatrix1.getOrderAtCornerCoordinates(i, j), cardsMatrix2.getOrderAtCornerCoordinates(i, j));
            }
        }
    }

    @Test
    void testInsert() {
        CardsMatrix cardsMatrix = testCardsMatrix();
        PlayableCard[][] testMatrix = testMatrix();
        cardsMatrix.insert(purplePlayableCard, 2, 3);

        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                if (i == 2 && j == 3) {
                    assertEquals(purplePlayableCard, cardsMatrix.get(i, j));
                } else {
                    assertEquals(testMatrix[i][j], cardsMatrix.get(i, j));
                }
            }
        }
    }

    @Test
    void testInsertAtCornersCoordinates() {
        CardsMatrix cardsMatrix = testCardsMatrix();
        cardsMatrix.insertAtCornersCoordinates(purplePlayableCard, 2, 4);

        assertEquals(purplePlayableCard, cardsMatrix.getAtCornersCoordinates(2, 4));
    }

    @Test
    void testGet() {
        CardsMatrix cardsMatrix = testCardsMatrix();
        PlayableCard[][] testMatrix = testMatrix();

        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                assertEquals(testMatrix[i][j], cardsMatrix.get(i, j));
            }
        }
    }

    @Test
    void testGetAtCornersCoordinates() {
        CardsMatrix cardsMatrix = testCardsMatrix();
        cardsMatrix.insert(purplePlayableCard, 1, 1);

        assertEquals(purplePlayableCard, cardsMatrix.getAtCornersCoordinates(2, 0));
    }

    @Test
    void testGetNearCardsCenter() {
        CardsMatrix cardsMatrix = testCardsMatrix();
        PlayableCard[] nearCards = cardsMatrix.getNearCards(1, 3);

        assertNull(nearCards[0]);
        assertEquals(redPlayableCard, nearCards[1]);
        assertEquals(bluePlayableCard, nearCards[2]);
        assertEquals(whitePlayableCard, nearCards[3]);
    }

    @Test
    void testGetNearCardsEdge() {
        CardsMatrix cardsMatrix = testCardsMatrix();
        PlayableCard[] nearCards = cardsMatrix.getNearCards(0, 4);

        assertNull(nearCards[0]);
        assertNull(nearCards[1]);
        assertNull(nearCards[2]);
        assertEquals(greenPlayableCard, nearCards[3]);
    }

    @Test
    void testGetCardOrder() {
        CardsMatrix cardsMatrix = new CardsMatrix(5);
        cardsMatrix.insertAtCornersCoordinates(purplePlayableCard, 0, 0);
        cardsMatrix.insertAtCornersCoordinates(purplePlayableCard, 1, 1);
        cardsMatrix.insertAtCornersCoordinates(purplePlayableCard, 2, 2);

        assertEquals(0, cardsMatrix.getOrderAtCornerCoordinates(0, 0));
        assertEquals(1, cardsMatrix.getOrderAtCornerCoordinates(1, 1));
        assertEquals(2, cardsMatrix.getOrderAtCornerCoordinates(2, 2));
    }

    @Test
    void testisCornerUncovered() {
        CardsMatrix cardsMatrix = new CardsMatrix(5);
        cardsMatrix.insertAtCornersCoordinates(purplePlayableCard, 2, 2);

        for (int i = 0; i < 4; i++){
            assertTrue(cardsMatrix.isCornerUncovered(2, 2, i));
        }
        assertFalse(cardsMatrix.isCornerUncovered(2, 2, 4));


        cardsMatrix.insertAtCornersCoordinates(purplePlayableCard, 0, 0);

        for (int i = 0; i < 4; i++){
            assertTrue(cardsMatrix.isCornerUncovered(0, 0, i));
        }


        cardsMatrix.insertAtCornersCoordinates(purplePlayableCard, 1, 1);

        assertFalse(cardsMatrix.isCornerUncovered(0, 0, 2));
        assertFalse(cardsMatrix.isCornerUncovered(2, 2, 0));
    }

    @Test
    void testTransposePrimary() {
        CardsMatrix cardsMatrix = testCardsMatrix();
        cardsMatrix = cardsMatrix.transposePrimary();
        PlayableCard[][] testMatrixTransposedPrimary = testMatrixTransposedPrimary();

        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                assertEquals(testMatrixTransposedPrimary[i][j], cardsMatrix.get(i, j));
            }
        }
    }

    @Test
    void testTransposeSecondary() {
        CardsMatrix cardsMatrix = testCardsMatrix();
        cardsMatrix = cardsMatrix.transposeSecondary();
        PlayableCard[][] testMatrixTransposedSecondary = testMatrixTransposedSecondary();

        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                assertEquals(testMatrixTransposedSecondary[i][j], cardsMatrix.get(i, j));
            }
        }
    }


    public static CardsMatrix testCardsMatrix() {
        PlayableCard[][] playableCardsMatrix = new PlayableCard[][]{
                {null, null, greenPlayableCard, null, null},
                {null, redPlayableCard, bluePlayableCard, null, null},
                {null, null, whitePlayableCard, greenPlayableCard, redPlayableCard},
                {null, bluePlayableCard, redPlayableCard, bluePlayableCard, null},
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

    public static PlayableCard[][] testMatrix() {
        return new PlayableCard[][]{
                {null, null, greenPlayableCard, null, null},
                {null, redPlayableCard, bluePlayableCard, null, null},
                {null, null, whitePlayableCard, greenPlayableCard, redPlayableCard},
                {null, bluePlayableCard, redPlayableCard, bluePlayableCard, null},
                {null, null, null, null, null}
        };
    }

    public static PlayableCard[][] testMatrixTransposedPrimary() {
        return new PlayableCard[][]{
                {null, null, null, null, null},
                {null, redPlayableCard, null, bluePlayableCard, null},
                {greenPlayableCard, bluePlayableCard, whitePlayableCard, redPlayableCard, null},
                {null, null, greenPlayableCard, bluePlayableCard, null},
                {null, null, redPlayableCard, null, null}
        };
    }

    public static PlayableCard[][] testMatrixTransposedSecondary() {
        return new PlayableCard[][]{
                {null, null, redPlayableCard, null, null},
                {null, bluePlayableCard, greenPlayableCard, null, null},
                {null, redPlayableCard, whitePlayableCard, bluePlayableCard, greenPlayableCard},
                {null, bluePlayableCard, null, redPlayableCard, null},
                {null, null, null, null, null}
        };
    }
}
