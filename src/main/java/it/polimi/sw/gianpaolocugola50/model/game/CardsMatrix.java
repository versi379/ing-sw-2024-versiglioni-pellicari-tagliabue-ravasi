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

    // alla posizione che avrebbe se fosse organizzata come cornersArea
    public void insert(PlayableCard card, int x, int y) {
        matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y)] = card;
    }

    // alla posizione che avrebbe se fosse organizzata come cornersArea
    public PlayableCard getAtCornersCoordinates(int x, int y) {
        return matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y)];
    }

    // alla posizione effettiva in matrix
    public PlayableCard getAtNaturalCoordinates(int x, int y) {
        return matrix[x][y];
    }

    // alla posizione effettiva in matrix trasposta
    public PlayableCard getAtTransposeCoordinates(int x, int y) {
        return matrix[y][x];
    }

    public PlayableCard[] getNearCards(int x, int y) {
        PlayableCard[] result = new PlayableCard[4];

        result[0] = matrix[cornersToCardsX(x, y) - 1][cornersToCardsY(x, y)];
        result[1] = matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y) + 1];
        result[2] = matrix[cornersToCardsX(x, y) + 1][cornersToCardsY(x, y)];
        result[3] = matrix[cornersToCardsX(x, y)][cornersToCardsY(x, y) - 1];
        return result;
    }

    private int cornersToCardsX(int x, int y) {
        return (x + y) / 2;
    }

    private int cornersToCardsY(int x, int y) {
        return (length() - 2 - x + y) / 2;
    }

    public void clockwiseRotate(int times) {
        if(times > 0) {
            PlayableCard temp;

            horizontalMirror();
            for (int i = 0; i < length(); i++) {
                for (int j = 0; i + j <= length() - 1; j++) {
                    temp = matrix[i][j];
                    matrix[i][j] = matrix[length() - j - 1][length() - i - 1];
                    matrix[length() - j - 1][length() - i - 1] = temp;
                }
            }
            clockwiseRotate(times - 1);
        }
    }

    public void horizontalMirror() {
        PlayableCard temp;

        for (int i = 0; i < length() / 2; i++) {
            for (int j = 0; j < length(); j++) {
                temp = matrix[i][j];
                matrix[i][j] = matrix[length() - i - 1][j];
                matrix[length() - i - 1][j] = temp;
            }
        }
    }
}
