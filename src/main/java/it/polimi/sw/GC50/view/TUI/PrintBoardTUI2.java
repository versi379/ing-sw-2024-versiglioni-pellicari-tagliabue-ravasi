package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;

public class PrintBoardTUI2 {
    String[][] board;

    public PrintBoardTUI2(CardsMatrix cardsMatrix) {
        int minX = cardsMatrix.getMinX();
        int maxX = cardsMatrix.getMaxX();
        int minY = cardsMatrix.getMinY();
        int maxY = cardsMatrix.getMaxY();

        int targetAreaWidth = maxX - minX + 1;
        int targetAreaHeight = maxY - minY + 1;
        if (targetAreaWidth > 0 && targetAreaHeight > 0) {
            board = new String[(targetAreaWidth) * 2 + 1][(targetAreaHeight) * 4 + 3];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    board[i][j] = "       ";
                }
            }

            for (Integer coordinates : cardsMatrix.getOrderList()) {
                int actualX = coordinates / cardsMatrix.length();
                int actualY = coordinates % cardsMatrix.length();
                String[][] cardTUI = cardsMatrix.get(actualX, actualY).toStringTUI();

                for (int i = 0; i < cardTUI.length; i++) {
                    for (int j = 0; j < cardTUI[i].length; j++) {
                        board[i + 2 * (actualX - minX)][j + 4 * (actualY - minY)] = cardTUI[i][j];
                    }
                }
            }
        } else {
            board = new String[1][1];
            board[0][0] = "No cards placed";
        }
    }

    public PrintBoardTUI2(CardsMatrix cardsMatrix, int centerX, int centerY, int size) {
        board = new String[size * 2 + 1][size * 4 + 3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = "       ";
            }
        }

        for (int x = 0; x < size; x++) {
            int actualX = centerX - size / 2 + x;
            for (int y = 0; y < size; y++) {
                int actualY = centerY - size / 2 + y;
                if ((actualX + actualY) % 2 == 0) {
                    PlayableCard card = cardsMatrix.get(actualX, actualY);
                    if (card != null) {
                        String[][] cardTUI = card.toStringTUI();

                        for (int i = 0; i < cardTUI.length; i++) {
                            for (int j = 0; j < cardTUI[i].length; j++) {
                                board[i + 2 * x][j + 4 * y] = cardTUI[i][j];
                            }
                        }

                        // SW corner
                        if (!cardsMatrix.isCornerUncovered(actualX, actualY, 0)) {
                            String[][] cardTUItmp = cardsMatrix
                                    .get(actualX - 1, actualY - 1).toStringTUI();
                            board[2 * x][4 * y] = cardTUItmp[2][4];
                            board[2 * x][4 * y + 1] = cardTUItmp[2][5];
                            board[2 * x][4 * y + 2] = cardTUItmp[2][6];
                        }
                        // NW corner
                        if (!cardsMatrix.isCornerUncovered(actualX, actualY, 1)) {
                            String[][] cardTUItmp = cardsMatrix
                                    .get(actualX - 1, actualY + 1).toStringTUI();
                            board[2 * x][4 * y + 4] = cardTUItmp[2][0];
                            board[2 * x][4 * y + 5] = cardTUItmp[2][1];
                            board[2 * x][4 * y + 6] = cardTUItmp[2][2];
                        }
                        // NE corner
                        if (!cardsMatrix.isCornerUncovered(actualX, actualY, 2)) {
                            String[][] cardTUItmp = cardsMatrix
                                    .get(actualX + 1, actualY + 1).toStringTUI();
                            board[2 * x + 2][4 * y + 4] = cardTUItmp[0][0];
                            board[2 * x + 2][4 * y + 5] = cardTUItmp[0][1];
                            board[2 * x + 2][4 * y + 6] = cardTUItmp[0][2];
                        }
                        // SE corner
                        if (!cardsMatrix.isCornerUncovered(actualX, actualY, 3)) {
                            String[][] cardTUItmp = cardsMatrix
                                    .get(actualX + 1, actualY - 1).toStringTUI();
                            board[2 * x + 2][4 * y] = cardTUItmp[0][4];
                            board[2 * x + 2][4 * y + 1] = cardTUItmp[0][5];
                            board[2 * x + 2][4 * y + 2] = cardTUItmp[0][6];
                        }
                    }
                }
            }
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
