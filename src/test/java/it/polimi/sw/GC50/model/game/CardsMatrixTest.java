package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.card.Corner;
import it.polimi.sw.GC50.model.card.PlayableCard;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CardsMatrixTest {

    @Test
    void testCardsMatrixConstructor() {
        CardsMatrix cardsMatrix = new CardsMatrix(80);
        assertEquals(80, cardsMatrix.length());
    }

    @Test
    void testLength() {
        CardsMatrix cardsMatrix = new CardsMatrix(80);
        assertEquals(80, cardsMatrix.length());
    }

    @Test
    void testCopy() {
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix1 = new CardsMatrix(testMatrix.length);
        cardsMatrix1.setCardsMatrix(testMatrix);
        CardsMatrix cardsMatrix2 = cardsMatrix1.copy();
        for (int i = 0; i < cardsMatrix2.length(); i++) {
            for (int j = 0; j < cardsMatrix2.length(); j++) {
                assertEquals(testMatrix[i][j], cardsMatrix2.get(i, j));
            }
        }
    }

    @Test
    void testInsert() {
        Corner[] corners = new Corner[4];
        PlayableCard purplePlayableCard = new PlayableCard(Color.PURPLE, 1, corners);
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix = new CardsMatrix(testMatrix.length);
        cardsMatrix.setCardsMatrix(testMatrix);
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
        Corner[] corners = new Corner[4];
        PlayableCard purplePlayableCard = new PlayableCard(Color.PURPLE, 1, corners);
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix = new CardsMatrix(testMatrix.length);
        cardsMatrix.setCardsMatrix(testMatrix);
        cardsMatrix.insertAtCornersCoordinates(purplePlayableCard, 2, 4);
        assertEquals(purplePlayableCard, cardsMatrix.getAtCornersCoordinates(2, 4));
    }

    @Test
    void testGet() {
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix = new CardsMatrix(testMatrix.length);
        cardsMatrix.setCardsMatrix(testMatrix);
        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                assertEquals(testMatrix[i][j], cardsMatrix.get(i, j));
            }
        }
    }

    @Test
    void testGetAtCornersCoordinates() {
        Corner[] corners = new Corner[4];
        PlayableCard purplePlayableCard = new PlayableCard(Color.PURPLE, 1, corners);
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix = new CardsMatrix(testMatrix.length);
        cardsMatrix.setCardsMatrix(testMatrix);
        cardsMatrix.insert(purplePlayableCard, 1, 1);
        assertEquals(purplePlayableCard, cardsMatrix.getAtCornersCoordinates(2, 0));
    }

    @Test
    void testGetNearCardsCenter() {
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix = new CardsMatrix(testMatrix.length);
        cardsMatrix.setCardsMatrix(testMatrix);
        PlayableCard[] nearCards = cardsMatrix.getNearCards(1, 3);
        assertNull(nearCards[0]);
        assertEquals(Color.RED, nearCards[1].getColor());
        assertEquals(Color.BLUE, nearCards[2].getColor());
        assertEquals(Color.WHITE, nearCards[3].getColor());
    }

    @Test
    void testGetNearCardsEdge() {
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix = new CardsMatrix(testMatrix.length);
        cardsMatrix.setCardsMatrix(testMatrix);
        PlayableCard[] nearCards = cardsMatrix.getNearCards(0, 4);
        assertNull(nearCards[0]);
        assertNull(nearCards[1]);
        assertNull(nearCards[2]);
        assertEquals(Color.GREEN, nearCards[3].getColor());
    }

    @Test
    void testTransposePrimary() {
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix = new CardsMatrix(testMatrix.length);
        cardsMatrix.setCardsMatrix(testMatrix);
        cardsMatrix = cardsMatrix.transposePrimary();
        PlayableCard[][] testMatrixTransposedPrimary = testMatrixTransposedPrimary();
        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                if (testMatrixTransposedPrimary[i][j] == null) {
                    assertNull(cardsMatrix.get(i, j));
                } else {
                    assertEquals(testMatrixTransposedPrimary[i][j].getColor(), cardsMatrix.get(i, j).getColor());
                }
            }
        }
    }

    @Test
    void testTransposeSecondary() {
        PlayableCard[][] testMatrix = testMatrix();
        CardsMatrix cardsMatrix = new CardsMatrix(testMatrix.length);
        cardsMatrix.setCardsMatrix(testMatrix);
        cardsMatrix = cardsMatrix.transposeSecondary();
        PlayableCard[][] testMatrixTransposedSecondary = testMatrixTransposedSecondary();
        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                if (testMatrixTransposedSecondary[i][j] == null) {
                    assertNull(cardsMatrix.get(i, j));
                } else {
                    assertEquals(testMatrixTransposedSecondary[i][j].getColor(), cardsMatrix.get(i, j).getColor());
                }
            }
        }
    }

    private static PlayableCard[][] testMatrix() {
        Corner[] corners = new Corner[4];
        PlayableCard whitePlayableCard = new PlayableCard(Color.WHITE, 0, corners);
        PlayableCard greenPlayableCard = new PlayableCard(Color.GREEN, 1, corners);
        PlayableCard bluePlayableCard = new PlayableCard(Color.BLUE, 1, corners);
        PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
        return new PlayableCard[][]{
                {null, null, greenPlayableCard, null, null},
                {null, redPlayableCard, bluePlayableCard, null, null},
                {null, null, whitePlayableCard, greenPlayableCard, redPlayableCard},
                {null, bluePlayableCard, redPlayableCard, bluePlayableCard, null},
                {null, null, null, null, null}
        };
    }

    private static PlayableCard[][] testMatrixTransposedPrimary() {
        Corner[] corners = new Corner[4];
        PlayableCard whitePlayableCard = new PlayableCard(Color.WHITE, 0, corners);
        PlayableCard greenPlayableCard = new PlayableCard(Color.GREEN, 1, corners);
        PlayableCard bluePlayableCard = new PlayableCard(Color.BLUE, 1, corners);
        PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
        return new PlayableCard[][]{
                {null, null, null, null, null},
                {null, redPlayableCard, null, bluePlayableCard, null},
                {greenPlayableCard, bluePlayableCard, whitePlayableCard, redPlayableCard, null},
                {null, null, greenPlayableCard, bluePlayableCard, null},
                {null, null, redPlayableCard, null, null}
        };
    }

    private static PlayableCard[][] testMatrixTransposedSecondary() {
        Corner[] corners = new Corner[4];
        PlayableCard whitePlayableCard = new PlayableCard(Color.WHITE, 0, corners);
        PlayableCard greenPlayableCard = new PlayableCard(Color.GREEN, 1, corners);
        PlayableCard bluePlayableCard = new PlayableCard(Color.BLUE, 1, corners);
        PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
        return new PlayableCard[][]{
                {null, null, redPlayableCard, null, null},
                {null, bluePlayableCard, greenPlayableCard, null, null},
                {null, redPlayableCard, whitePlayableCard, bluePlayableCard, greenPlayableCard},
                {null, bluePlayableCard, null, redPlayableCard, null},
                {null, null, null, null, null}
        };
    }
}
