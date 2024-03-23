package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;

import java.util.Collections;

public class CardsMatrix {
    private final PlayableCard[][] matrix;

    public CardsMatrix(int length) {
        matrix = new PlayableCard[length][length];
    }

    public int length() {
        return matrix.length;
    }

    public CardsMatrix copy() {
        CardsMatrix result = new CardsMatrix(length());

        for (int i = 0; i < length(); i++) {
            for (int j = 0; j < length(); j++) {
                result.insert(matrix[i][j], i, j);
            }
        }
        return result;
    }

    private int cornersToCardsX(int x, int y) {
        return (x + y) / 2;
    }

    private int cornersToCardsY(int x, int y) {
        return ((length() - 2) - x + y) / 2;
    }

    // alla posizione effettiva in matrix
    public void insert(PlayableCard card, int x, int y) {
        matrix[x][y] = card;
    }

    // alla posizione che avrebbe se fosse organizzata come cornersArea
    public void insertAtCornersCoordinates(PlayableCard card, int x, int y) {
        matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y)] = card;
    }

    // alla posizione effettiva in matrix
    public PlayableCard get(int x, int y) {
        return matrix[x][y];
    }

    // alla posizione che avrebbe se fosse organizzata come cornersArea
    public PlayableCard getAtCornersCoordinates(int x, int y) {
        return matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y)];
    }

    public PlayableCard[] getNearCards(int x, int y) {
        PlayableCard[] result = new PlayableCard[4];

        result[0] = (cornersToCardsX(x, y) > 0) ? matrix[cornersToCardsX(x, y) - 1][cornersToCardsY(x, y)] : null;
        result[1] = (cornersToCardsY(x, y) < length() - 1) ? matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y) + 1] : null;
        result[2] = (cornersToCardsX(x, y) < length() - 1) ? matrix[cornersToCardsX(x, y) + 1][cornersToCardsY(x, y)] : null;
        result[3] = (cornersToCardsY(x, y) > 0) ? matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y) - 1] : null;
        return result;
    }

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
