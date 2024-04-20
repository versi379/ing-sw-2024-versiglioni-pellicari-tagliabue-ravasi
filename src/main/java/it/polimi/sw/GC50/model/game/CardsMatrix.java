package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PlayableCard;

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
                result.insert(get(i, j), i, j);
            }
        }
        return result;
    }

    /**
     * Converts the position utilized for storing information in cornersArea
     * to the corresponding X coordinate in cardsArea
     *
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
     *
     * @param x
     * @param y
     * @return
     */
    private int cornersToCardsY(int x, int y) {
        return ((length() - 2) - x + y) / 2;
    }

    /**
     * Inserts an element at the position (x, y)
     *
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
     *
     * @param card
     * @param x
     * @param y
     */
    public void insertAtCornersCoordinates(PlayableCard card, int x, int y) {
        insert(card, cornersToCardsX(x, y), cornersToCardsY(x, y));
    }

    /**
     * Gets the element present at the position (x, y)
     *
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
     *
     * @param x
     * @param y
     * @return
     */
    public PlayableCard getAtCornersCoordinates(int x, int y) {
        return get(cornersToCardsX(x, y), cornersToCardsY(x, y));
    }

    /**
     * Returns all adjacent cards to the one present at the position (a, b)
     *
     * @param x
     * @param y
     * @return
     */
    public PlayableCard[] getNearCards(int x, int y) {
        PlayableCard[] result = new PlayableCard[4];
        int a = cornersToCardsX(x, y);
        int b = cornersToCardsY(x, y);

        result[0] = (a > 0) ? get(a - 1, b) : null;
        result[1] = (b < length() - 1) ? get(a, b + 1) : null;
        result[2] = (a < length() - 1) ? get(a + 1, b) : null;
        result[3] = (b > 0) ? get(a, b - 1) : null;
        return result;
    }

    /**
     * Flips matrix over its primary diagonal
     *
     * @return
     */
    public CardsMatrix transposePrimary() {
        CardsMatrix result = new CardsMatrix(length());

        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length(); j++) {
                result.insert(get(j, i), i, j);
            }
        }
        return result;
    }

    /**
     * Flips matrix over its secondary diagonal
     *
     * @return
     */
    public CardsMatrix transposeSecondary() {
        CardsMatrix result = new CardsMatrix(length());

        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length(); j++) {
                result.insert(get(length() - 1 - j, length() - 1 - i), i, j);
            }
        }
        return result;
    }

    // test
    public void printCardsArea() {
        System.out.println("_____________________________________________________________________________________________");
        for (int j = 47; j > 35; j--) {
            for (int i = 35; i < 47; i++) {
                PlayableCard x = get(i, j);
                if (x != null) {
                    System.out.print((x.getColor() + "\t"));
                } else {
                    System.out.print("null\t");
                }
            }
            System.out.print("\n");
        }
    }

    public void setCardsMatrix(PlayableCard[][] testPlayableCardsMatrix) {
        for (int i = 0; i < testPlayableCardsMatrix.length; i++) {
            for (int j = 0; j < testPlayableCardsMatrix[i].length; j++) {
                insert(testPlayableCardsMatrix[i][j], i, j);
            }
        }
    }
}
