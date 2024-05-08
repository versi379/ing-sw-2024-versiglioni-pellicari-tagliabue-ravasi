package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;

public class PrintBoardTUI2 {
    String[][] board;

    public PrintBoardTUI2(CardsMatrix cardsMatrix) {
        int minX = cardsMatrix.length();
        int maxX = 0;
        int minY = cardsMatrix.length();
        int maxY = 0;
        for (int i = 0; i < cardsMatrix.length(); i++) {
            for (int j = 0; j < cardsMatrix.length(); j++) {
                if ((i + j) % 2 == 0 && cardsMatrix.getAtCornersCoordinates(i, j) != null) {
                    if (i < minX) {
                        minX = i;
                    }
                    if (i >= maxX) {
                        maxX = i + 1;
                    }
                    if (j < minY) {
                        minY = j;
                    }
                    if (j >= maxY) {
                        maxY = j + 1;
                    }
                }
            }
        }

        int targetAreaWidth = maxX - minX;
        int targetAreaHeight = maxY - minY;
        if (targetAreaWidth > 0 && targetAreaHeight > 0) {
            board = new String[(targetAreaWidth) * 2 + 1][(targetAreaHeight) * 4 + 3];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    board[i][j] = "       ";
                }
            }

            for (int x = 0; x < targetAreaWidth; x++) {
                int cornersX = minX + x;
                for (int y = 0; y < targetAreaHeight; y++) {
                    int cornersY = minY + y;
                    if ((cornersX + cornersY) % 2 == 0) {
                        PlayableCard card = cardsMatrix.getAtCornersCoordinates(cornersX, cornersY);
                        if (card != null) {
                            String[][] cardTUI = card.toStringTUI();

                            for (int i = 0; i < cardTUI.length; i++) {
                                for (int j = 0; j < cardTUI[i].length; j++) {
                                    board[i + 2 * x][j + 4 * y] = cardTUI[i][j];
                                }
                            }

                            // SW corner
                            if (!cardsMatrix.isCornerUncovered(cornersX, cornersY, 0)) {
                                String[][] cardTUItmp = cardsMatrix
                                        .getAtCornersCoordinates(cornersX - 1, cornersY - 1).toStringTUI();
                                board[2 * x][4 * y] = cardTUItmp[2][4];
                                board[2 * x][4 * y + 1] = cardTUItmp[2][5];
                                board[2 * x][4 * y + 2] = cardTUItmp[2][6];
                            }
                            // NW corner
                            if (!cardsMatrix.isCornerUncovered(cornersX, cornersY, 1)) {
                                String[][] cardTUItmp = cardsMatrix
                                        .getAtCornersCoordinates(cornersX - 1, cornersY + 1).toStringTUI();
                                board[2 * x][4 * y + 4] = cardTUItmp[2][0];
                                board[2 * x][4 * y + 5] = cardTUItmp[2][1];
                                board[2 * x][4 * y + 6] = cardTUItmp[2][2];
                            }
                            // NE corner
                            if (!cardsMatrix.isCornerUncovered(cornersX, cornersY, 2)) {
                                String[][] cardTUItmp = cardsMatrix
                                        .getAtCornersCoordinates(cornersX + 1, cornersY + 1).toStringTUI();
                                board[2 * x + 2][4 * y + 4] = cardTUItmp[0][0];
                                board[2 * x + 2][4 * y + 5] = cardTUItmp[0][1];
                                board[2 * x + 2][4 * y + 6] = cardTUItmp[0][2];
                            }
                            // SE corner
                            if (!cardsMatrix.isCornerUncovered(cornersX, cornersY, 3)) {
                                String[][] cardTUItmp = cardsMatrix
                                        .getAtCornersCoordinates(cornersX + 1, cornersY - 1).toStringTUI();
                                board[2 * x + 2][4 * y] = cardTUItmp[0][4];
                                board[2 * x + 2][4 * y + 1] = cardTUItmp[0][5];
                                board[2 * x + 2][4 * y + 2] = cardTUItmp[0][6];
                            }
                        }
                    }
                }
            }
        } else {
            board = new String[1][1];
            board[0][0] = "No cards placed";
        }
    }

    public void print() {
        for (int i = board[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[j][i]);
            }
            System.out.println();
        }
    }
}
