package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;

import java.util.Collections;

/**
 * Represents a square matrix of PlayableCards
 */
public class CardsMatrix {
    private final PlayableCard[][] matrix;

    public CardsMatrix(int length) {
        matrix = new PlayableCard[length][length];
    }

    public int length() {
        return matrix.length;
    }

    /**
     * @return a new matrix whose elements are the same of this one
     */
    public CardsMatrix copy() {
        CardsMatrix result = new CardsMatrix(length());

        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length(); j++) {
                result.insert(matrix[i][j], i, j);
            }
        }
        return result;
    }

    /**
     * Converts the position utilized for storing information in cornersArea
     * to the corresponding X coordinate in cardsArea
     * @param x
     * @param y
     * @return
     */
    private int cornersToCardsX(int x, int y) {
        return (x + y) / 2;
    }

    /**
     * Converts the position utilized for storing information in cornersArea
     * to the corresponding Y coordinate in cardsArea
     * @param x
     * @param y
     * @return
     */
    private int cornersToCardsY(int x, int y) {
        return ((length() - 2) - x + y) / 2;
    }

    /**
     * Inserts an element at the position (x, y)
     * @param card
     * @param x
     * @param y
     */
    public void insert(PlayableCard card, int x, int y) {
        matrix[x][y] = card;
    }

    /**
     * Converts the received cornersArea's coordinates to the corresponding ones in cardsArea
     * and inserts an element at such position
     * @param card
     * @param x
     * @param y
     */
    public void insertAtCornersCoordinates(PlayableCard card, int x, int y) {
        matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y)] = card;
    }

    /**
     * Gets the element present at the position (x, y)
     * @param x
     * @param y
     * @return
     */
    public PlayableCard get(int x, int y) {
        return matrix[x][y];
    }

    /**
     * Converts the received cornersArea's coordinates to the corresponding ones in cardsArea
     * and gets the element present at such position
     * @param x
     * @param y
     * @return
     */
    public PlayableCard getAtCornersCoordinates(int x, int y) {
        return matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y)];
    }

    /**
     * Returns all adjacent cards to the one present at the position (x, y)
     * @param x
     * @param y
     * @return
     */
    public PlayableCard[] getNearCards(int x, int y) {
        PlayableCard[] result = new PlayableCard[4];

        result[0] = (cornersToCardsX(x, y) > 0) ? matrix[cornersToCardsX(x, y) - 1][cornersToCardsY(x, y)] : null;
        result[1] = (cornersToCardsY(x, y) < length() - 1) ? matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y) + 1] : null;
        result[2] = (cornersToCardsX(x, y) < length() - 1) ? matrix[cornersToCardsX(x, y) + 1][cornersToCardsY(x, y)] : null;
        result[3] = (cornersToCardsY(x, y) > 0) ? matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y) - 1] : null;
        return result;
    }

    /**
     * Transposes this matrix
     * @return
     */
    public CardsMatrix transpose() {
        for (int i = 0; i < length(); i++) {
            for (int j = i; j < length(); j++) {
                PlayableCard tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }
        return this;
    }

    /**
     * Rotates of 180 degrees this matrix
     * @return
     */
    public CardsMatrix invert() {
        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length() - i; j++) {
                PlayableCard tmp = matrix[i][j];
                matrix[i][j] = matrix[length() - 1 - i][length() - 1 - j];
                matrix[length() - 1 - i][length() - 1 - j] = tmp;
            }
        }
        return this;
    }
}
