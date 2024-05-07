package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;

public class PrintBoard {
    String[][] board;

    public PrintBoard(CardsMatrix cardsMatrix) {
        board = new String[cardsMatrix.length() * 2 + 1][cardsMatrix.length() * 4 + 3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = "       ";
            }
        }

        for (int x = 0; x < cardsMatrix.length(); x++) {
            for (int y = 0; y < cardsMatrix.length(); y++) {
                if ((x + y) % 2 == 0) {
                    PlayableCard card = cardsMatrix.getAtCornersCoordinates(x, y);
                    if (card != null) {
                        String[][] cardTUI = card.toStringTUI();

                        for (int i = 0; i < cardTUI.length; i++) {
                            for (int j = 0; j < cardTUI[i].length; j++) {
                                board[i + 2 * x][j + 4 * y] = cardTUI[i][j];
                            }
                        }

                        // SW corner
                        if (!cardsMatrix.isCornerUncovered(x, y, 0)) {
                            String[][] cardTUItmp = cardsMatrix.getAtCornersCoordinates(x - 1, y - 1).toStringTUI();
                            board[2 * x][4 * y] = cardTUItmp[2][4];
                            board[2 * x][4 * y + 1] = cardTUItmp[2][5];
                            board[2 * x][4 * y + 2] = cardTUItmp[2][6];
                        }
                        // NW corner
                        if (!cardsMatrix.isCornerUncovered(x, y, 1)) {
                            String[][] cardTUItmp = cardsMatrix.getAtCornersCoordinates(x - 1, y + 1).toStringTUI();
                            board[2 * x][4 * y + 4] = cardTUItmp[2][0];
                            board[2 * x][4 * y + 5] = cardTUItmp[2][1];
                            board[2 * x][4 * y + 6] = cardTUItmp[2][2];
                        }
                        // NE corner
                        if (!cardsMatrix.isCornerUncovered(x, y, 2)) {
                            String[][] cardTUItmp = cardsMatrix.getAtCornersCoordinates(x + 1, y + 1).toStringTUI();
                            board[2 * x + 2][4 * y + 4] = cardTUItmp[0][0];
                            board[2 * x + 2][4 * y + 5] = cardTUItmp[0][1];
                            board[2 * x + 2][4 * y + 6] = cardTUItmp[0][2];
                        }
                        // SE corner
                        if (!cardsMatrix.isCornerUncovered(x, y, 3)) {
                            String[][] cardTUItmp = cardsMatrix.getAtCornersCoordinates(x + 1, y - 1).toStringTUI();
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
        for (int i = board[1].length - 1; i >= 0; i--) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[j][i]);
            }
            System.out.println();
        }
    }
}
