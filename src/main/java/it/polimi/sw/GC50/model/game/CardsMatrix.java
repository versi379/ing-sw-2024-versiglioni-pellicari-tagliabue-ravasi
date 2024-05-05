package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PlayableCard;
import javafx.util.Pair;

import java.io.Serializable;

/**
 * Represents a square matrix of PlayableCards
 */
public class CardsMatrix implements Serializable {
    private final PlayableCard[][] cardsMatrix;
    private final Integer[][] orderMatrix;
    private int currentCards;

    public CardsMatrix(int length) {
        cardsMatrix = new PlayableCard[length][length];
        orderMatrix = new Integer[length][length];
        currentCards = 0;
    }

    public int length() {
        return cardsMatrix.length;
    }

    /**
     * @return a new matrix whose elements are the same of this one
     */
    public CardsMatrix copy() {
        CardsMatrix result = new CardsMatrix(length());

        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length(); j++) {
                result.cardsMatrix[i][j] = cardsMatrix[i][j];
                result.orderMatrix[i][j] = orderMatrix[i][j];
            }
        }
        result.currentCards = currentCards;
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
        return (length() - x + y) / 2;
    }

    /**
     * Inserts an element at the position (x, y)
     *
     * @param card
     * @param x
     * @param y
     */
    public void insert(PlayableCard card, int x, int y) {
        cardsMatrix[x][y] = card;
        orderMatrix[x][y] = currentCards;
        currentCards++;
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
        return cardsMatrix[x][y];
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

    // GETS CARD AT CORNER COORDINATES!!
    public int getCardOrder(int x, int y) {
        if (getAtCornersCoordinates(x, y) != null) {
            return orderMatrix[cornersToCardsX(x, y)][cornersToCardsY(x, y)];
        }
        return -1;
    }

    // TELLS IF THE CORNER IN POSITION corner (0->SW, 1->NW, 2->NE, 3->SE)
    // OF THE CARD AT (x, y) (CORNERS COORDINATES!) IS VISIBLE FROM THE PLAYER'S PERSPECTIVE
    public boolean isCornerVisible(int x, int y, int corner) {
        switch(corner) {
            case 0 -> {return getCardOrder(x, y) > getCardOrder(x - 1, y - 1);}
            case 1 -> {return getCardOrder(x, y) > getCardOrder(x - 1, y + 1);}
            case 2 -> {return getCardOrder(x, y) > getCardOrder(x + 1, y + 1);}
            case 3 -> {return getCardOrder(x, y) > getCardOrder(x + 1, y - 1);}
            default -> {return false;}
        }
    }

    // SELF EXPLANATORY
    public Pair<PlayableCard, Integer>[][] getAsCornerMatrixWithOrder() {
        Pair[][] result = new Pair[length()][length()];
        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length(); j++) {
                result[i][j] = new Pair(getAtCornersCoordinates(i, j), getCardOrder(i, j));
            }
        }
        return result;
    }

    public PlayableCard[][] getAsCornerMatrixWithoutOrder() {
        PlayableCard[][] result = new PlayableCard[length()][length()];
        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length(); j++) {
                result[i][j] = getAtCornersCoordinates(i, j);
            }
        }
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
                result.cardsMatrix[i][j] = cardsMatrix[j][i];
                result.orderMatrix[i][j] = orderMatrix[j][i];
            }
        }
        result.currentCards = currentCards;
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
                result.cardsMatrix[i][j] = cardsMatrix[length() - 1 - j][length() - 1 - i];
                result.orderMatrix[i][j] = orderMatrix[length() - 1 - j][length() - 1 - i];
            }
        }
        result.currentCards = currentCards;
        return result;
    }

    // TEST METHODS ////////////////////////////////////////////////////////////////////////////////////////////////////
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
}
