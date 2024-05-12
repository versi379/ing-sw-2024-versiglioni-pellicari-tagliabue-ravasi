package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PlayableCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a square matrix of PlayableCards
 */
public class CardsMatrix implements Serializable {
    private final PlayableCard[][] cardsMatrix;
    private final List<Integer> orderList;

    public CardsMatrix(int length) {
        cardsMatrix = new PlayableCard[length][length];
        orderList = new ArrayList<>();
    }

    public int length() {
        return cardsMatrix.length;
    }

    public int getMinX() {
        return orderList.stream()
                .map(x -> x / length())
                .min(Integer::compareTo)
                .orElse(length());
    }

    public int getMaxX() {
        return orderList.stream()
                .map(x -> x / length())
                .max(Integer::compareTo)
                .orElse(0);
    }

    public int getMinY() {
        return orderList.stream()
                .map(x -> x % length())
                .min(Integer::compareTo)
                .orElse(length());
    }

    public int getMaxY() {
        return orderList.stream()
                .map(x -> x % length())
                .max(Integer::compareTo)
                .orElse(0);
    }

    /**
     * @return a new matrix whose elements are the same of this one and in the same order
     */
    public CardsMatrix copy() {
        CardsMatrix result = new CardsMatrix(length());

        for (Integer coordinates : orderList) {
            int x = coordinates / length();
            int y = coordinates % length();
            result.insert(get(x, y), x, y);
        }
        return result;
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
        orderList.add(x * length() + y);
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
     * Returns all adjacent cards to the one present at the position (x, y)
     *
     * @param x
     * @param y
     * @return
     */
    public PlayableCard[] getNearCards(int x, int y) {
        CardsMatrix rotatedMatrix = rotate45();
        int rotatedX = cornersToCardsX(x, y);
        int rotatedY = cornersToCardsY(x, y);

        PlayableCard[] result = new PlayableCard[4];
        result[0] = (rotatedX > 0) ? rotatedMatrix.get(rotatedX - 1, rotatedY) : null;
        result[1] = (rotatedY < length() - 1) ? rotatedMatrix.get(rotatedX, rotatedY + 1) : null;
        result[2] = (rotatedX < length() - 1) ? rotatedMatrix.get(rotatedX + 1, rotatedY) : null;
        result[3] = (rotatedY > 0) ? rotatedMatrix.get(rotatedX, rotatedY - 1) : null;
        return result;
    }

    public List<Integer> getOrderList() {
        return new ArrayList<>(orderList);
    }

    public int getOrder(int x, int y) {
        for (int order = 0; order < orderList.size(); order++) {
            if (orderList.get(order) / length() == x && orderList.get(order) % length() == y) {
                return order;
            }
        }
        return -1;
    }

    /**
     * Tells if the corner at the selected position (0->sw, 1->nw, 2->ne, 3->se)
     * of the card at (x, y) is visible from the player's perspective
     *
     * @param x
     * @param y
     * @param position
     * @return
     */
    public boolean isCornerUncovered(int x, int y, int position) {
        CardsMatrix rotatedMatrix = rotate45();
        int rotatedX = cornersToCardsX(x, y);
        int rotatedY = cornersToCardsY(x, y);

        return switch (position) {
            case 0 -> rotatedX <= 0 ||
                    rotatedMatrix.getOrder(rotatedX, rotatedY) > rotatedMatrix.getOrder(rotatedX - 1, rotatedY);
            case 1 -> rotatedY >= length() - 1 ||
                    rotatedMatrix.getOrder(rotatedX, rotatedY) > rotatedMatrix.getOrder(rotatedX, rotatedY + 1);
            case 2 -> rotatedX >= length() - 1 ||
                    rotatedMatrix.getOrder(rotatedX, rotatedY) > rotatedMatrix.getOrder(rotatedX + 1, rotatedY);
            case 3 -> rotatedY <= 0 ||
                    rotatedMatrix.getOrder(rotatedX, rotatedY) > rotatedMatrix.getOrder(rotatedX, rotatedY - 1);
            default -> false;
        };
    }

    private int cornersToCardsX(int x, int y) {
        return (x + y) / 2;
    }

    private int cornersToCardsY(int x, int y) {
        return (length() - x + y) / 2;
    }

    /**
     * Rotates matrix 45 degrees clockwise
     *
     * @return
     */
    public CardsMatrix rotate45() {
        CardsMatrix result = new CardsMatrix(length());

        for (Integer coordinates : orderList) {
            int x = coordinates / length();
            int y = coordinates % length();
            result.insert
                    (get(x, y), cornersToCardsX(x, y), cornersToCardsY(x, y));
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

        for (Integer coordinates : orderList) {
            int x = coordinates / length();
            int y = coordinates % length();
            result.insert(get(x, y), y, x);
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

        for (Integer coordinates : orderList) {
            int x = coordinates / length();
            int y = coordinates % length();
            result.insert(get(x, y), length() - 1 - y, length() - 1 - x);
        }
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
