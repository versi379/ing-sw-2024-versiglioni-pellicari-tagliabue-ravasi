package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PlayableCard;
import javafx.util.Pair;

import java.io.Serializable;

/**
 * Represents a square matrix of PlayableCards
 */
public class CardsMatrix implements Serializable {
    private final PlayableCard[][] cardsMatrix;
    private final int[][] orderMatrix;
    private int currentCards;
    private int minX, maxX, minY, maxY;

    public CardsMatrix(int length) {
        cardsMatrix = new PlayableCard[length][length];
        orderMatrix = new int[length][length];
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
     * @param cornersX
     * @param cornersY
     */
    public void insertAtCornersCoordinates(PlayableCard card, int cornersX, int cornersY) {
        insert(card, cornersToCardsX(cornersX, cornersY), cornersToCardsY(cornersX, cornersY));
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
     * @param cornersX
     * @param cornersY
     * @return
     */
    public PlayableCard getAtCornersCoordinates(int cornersX, int cornersY) {
        return get(cornersToCardsX(cornersX, cornersY), cornersToCardsY(cornersX, cornersY));
    }

    /**
     * Returns all adjacent cards to the one present at the position (x, y)
     *
     * @param cornersX
     * @param cornersY
     * @return
     */
    public PlayableCard[] getNearCards(int cornersX, int cornersY) {
        PlayableCard[] result = new PlayableCard[4];
        int x = cornersToCardsX(cornersX, cornersY);
        int y = cornersToCardsY(cornersX, cornersY);

        result[0] = (x > 0) ? get(x - 1, y) : null;
        result[1] = (y < length() - 1) ? get(x, y + 1) : null;
        result[2] = (x < length() - 1) ? get(x + 1, y) : null;
        result[3] = (y > 0) ? get(x, y - 1) : null;
        return result;
    }

    public int getOrder(int x, int y) {
        if (get(x, y) != null) {
            return orderMatrix[x][y];
        }
        return -1;
    }

    // GETS CARD AT CORNER COORDINATES!!
    public int getOrderAtCornerCoordinates(int cornersX, int cornersY) {
        return getOrder(cornersToCardsX(cornersX, cornersY), cornersToCardsY(cornersX, cornersY));
    }

    // TELLS IF THE CORNER IN POSITION position (0->SW, 1->NW, 2->NE, 3->SE)
    // OF THE CARD AT (cornersX, cornersY) (CORNERS COORDINATES!) IS VISIBLE FROM THE PLAYER'S PERSPECTIVE
    public boolean isCornerUncovered(int cornersX, int cornersY, int position) {
        int x = cornersToCardsX(cornersX, cornersY);
        int y = cornersToCardsY(cornersX, cornersY);

        switch (position) {
            case 0 -> {
                return x <= 0 || getOrder(x, y) > getOrder(x - 1, y);
            }
            case 1 -> {
                return y >= length() - 1 || getOrder(x, y) > getOrder(x, y + 1);
            }
            case 2 -> {
                return x >= length() - 1 || getOrder(x, y) > getOrder(x + 1, y);
            }
            case 3 -> {
                return y <= 0 || getOrder(x, y) > getOrder(x, y - 1);
            }
            default -> {
                return false;
            }
        }
    }

    // SELF EXPLANATORY
    public Pair<PlayableCard, Integer>[][] getAsCornerMatrixWithOrder() {
        Pair[][] result = new Pair[length()][length()];
        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length(); j++) {
                result[i][j] = new Pair(getAtCornersCoordinates(i, j), getOrderAtCornerCoordinates(i, j));
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

    public void printBoard(int cornersX, int cornersY) {
        String[][] board = new String[11][23];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = "       ";
            }
        }

        for (int x = cornersX - 2; x < cornersX + 3; x++) {
            for (int y = cornersY - 2; y < cornersX + 3; y++) {
                if (x >= 0 && x < length() &&
                        y >= 0 && y < length() &&
                        (x + y) % 2 == 0) {
                    PlayableCard card = getAtCornersCoordinates(x, y);
                    if (card != null) {
                        String[][] cardTUI = card.toStringTUI();

                        for (int i = 0; i < cardTUI.length; i++) {
                            for (int j = 0; j < cardTUI[i].length; j++) {
                                board[i + 2 * x][j + 4 * y] = cardTUI[i][j];
                            }
                        }

                        // SW corner
                        if (!isCornerUncovered(x, y, 0)) {
                            String[][] cardTUItmp = getAtCornersCoordinates(x - 1, y - 1).toStringTUI();
                            board[2 * x][4 * y] = cardTUItmp[2][4];
                            board[2 * x][4 * y + 1] = cardTUItmp[2][5];
                            board[2 * x][4 * y + 2] = cardTUItmp[2][6];
                        }
                        // NW corner
                        if (!isCornerUncovered(x, y, 1)) {
                            String[][] cardTUItmp = getAtCornersCoordinates(x - 1, y + 1).toStringTUI();
                            board[2 * x][4 * y + 4] = cardTUItmp[2][0];
                            board[2 * x][4 * y + 5] = cardTUItmp[2][1];
                            board[2 * x][4 * y + 6] = cardTUItmp[2][2];
                        }
                        // NE corner
                        if (!isCornerUncovered(x, y, 2)) {
                            String[][] cardTUItmp = getAtCornersCoordinates(x + 1, y + 1).toStringTUI();
                            board[2 * x + 2][4 * y + 4] = cardTUItmp[0][0];
                            board[2 * x + 2][4 * y + 5] = cardTUItmp[0][1];
                            board[2 * x + 2][4 * y + 6] = cardTUItmp[0][2];
                        }
                        // SE corner
                        if (!isCornerUncovered(x, y, 3)) {
                            String[][] cardTUItmp = getAtCornersCoordinates(x + 1, y - 1).toStringTUI();
                            board[2 * x + 2][4 * y] = cardTUItmp[0][4];
                            board[2 * x + 2][4 * y + 1] = cardTUItmp[0][5];
                            board[2 * x + 2][4 * y + 2] = cardTUItmp[0][6];
                        }
                    }
                }
            }
        }
        for (int i = 22; i >= 0; i--) {
            for (int j = 0; j < 11; j++) {
                System.out.print(board[j][i]);
            }
            System.out.println();
        }
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
