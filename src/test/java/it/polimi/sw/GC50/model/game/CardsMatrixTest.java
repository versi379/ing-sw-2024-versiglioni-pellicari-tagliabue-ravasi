package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.card.Corner;
import it.polimi.sw.GC50.model.card.PlayableCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private static PlayableCard[][] getPlayableCards() {
        Corner[] corners = new Corner[4];
        PlayableCard greenPlayableCard = new PlayableCard(Color.GREEN, 1, corners);
        PlayableCard bluePlayableCard = new PlayableCard(Color.BLUE, 1, corners);
        PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
        return new PlayableCard[][]{
                {null, null, null, null, greenPlayableCard},
                {null, bluePlayableCard, null, null, null},
                {null, null, bluePlayableCard, null, null},
                {greenPlayableCard, null, redPlayableCard, null, greenPlayableCard},
                {null, null, null, bluePlayableCard, redPlayableCard}
        };
    }

    @Test
    void testCopy() {
        PlayableCard[][] testPlayableCards = getPlayableCards();
        CardsMatrix cardsMatrix1 = new CardsMatrix(testPlayableCards.length);
        cardsMatrix1.setCardsMatrix(testPlayableCards);
        CardsMatrix cardsMatrix2 = cardsMatrix1.copy();
        for (int i = 0; i < testPlayableCards.length; i++) {
            for (int j = 0; j < testPlayableCards.length; j++) {
                assertEquals(cardsMatrix2.get(i, j), testPlayableCards[i][j]);
            }
        }
    }

    @Test
    void testInsert() {
        Corner[] corners = new Corner[4];
        PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
        PlayableCard[][] testPlayableCards = getPlayableCards();
        CardsMatrix cardsMatrix = new CardsMatrix(testPlayableCards.length);
        cardsMatrix.setCardsMatrix(testPlayableCards);
        cardsMatrix.insert(redPlayableCard, 0, 1);
        for (int i = 0; i < testPlayableCards.length; i++) {
            for (int j = 0; j < testPlayableCards.length; j++) {
                if(i == 0 && j == 1) {
                    assertEquals(cardsMatrix.get(i, j), redPlayableCard);
                } else {
                    assertEquals(cardsMatrix.get(i, j), testPlayableCards[i][j]);
                }
            }
        }
    }

    @Test
    void testInsertAtCornersCoordinates() {
        Corner[] corners = new Corner[4];
        PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
        PlayableCard[][] testPlayableCards = getPlayableCards();
        CardsMatrix cardsMatrix = new CardsMatrix(testPlayableCards.length);
        cardsMatrix.setCardsMatrix(testPlayableCards);
        cardsMatrix.insertAtCornersCoordinates(redPlayableCard, 0, 0);
        for (int i = 0; i < testPlayableCards.length; i++) {
            for (int j = 0; j < testPlayableCards.length; j++) {
                if(i == 0 && j == 1) {
                    assertEquals(cardsMatrix.get(i, j), redPlayableCard);
                } else {
                    assertEquals(cardsMatrix.get(i, j), testPlayableCards[i][j]);
                }
            }
        }
    }

    @Test
    void testGet() {
        PlayableCard[][] testPlayableCards = getPlayableCards();
        CardsMatrix cardsMatrix = new CardsMatrix(testPlayableCards.length);
        cardsMatrix.setCardsMatrix(testPlayableCards);
        for (int i = 0; i < testPlayableCards.length; i++) {
            for (int j = 0; j < testPlayableCards.length; j++) {
                assertEquals(cardsMatrix.get(i, j), testPlayableCards[i][j]);
            }
        }
    }

    @Test
    void testGetAtCornersCoordinates() {
        PlayableCard[][] testPlayableCards = getPlayableCards();
        CardsMatrix cardsMatrix = new CardsMatrix(testPlayableCards.length);
        cardsMatrix.setCardsMatrix(testPlayableCards);
        for (int i = 0; i < testPlayableCards.length; i++) {
            for (int j = 0; j < testPlayableCards.length; j++) {
                assertEquals(cardsMatrix.get(i, j), testPlayableCards[i][j]);
            }
        }
    }

    @Test
    void getNearCards() {
    }

    @Test
    void transpose() {
    }

    @Test
    void invert() {
    }

    @Test
    void printCardsArea() {
    }
}