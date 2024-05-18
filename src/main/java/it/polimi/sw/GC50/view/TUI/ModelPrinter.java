package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.game.CardsMatrix;

public class ModelPrinter {


    private void printPlayerArea(CardsMatrix cardsMatrix) {
        String[][] board;
        int minX = cardsMatrix.getMinX();
        int maxX = cardsMatrix.getMaxX();
        int minY = cardsMatrix.getMinY();
        int maxY = cardsMatrix.getMaxY();

        int targetAreaWidth = maxX - minX + 1;
        int targetAreaHeight = maxY - minY + 1;
        if (targetAreaWidth > 0 && targetAreaHeight > 0) {
            board = new String[(targetAreaWidth) * 2 + 1 + 1][(targetAreaHeight) * 4 + 3 + 1];

            int counter = minX;
            for (int i = 0; i < targetAreaWidth; i++) {
                board[i * 2 + 2][0] = PrintBoardTUI2.centerString(7, Integer.toString(counter + 1));
                counter++;
            }
            counter = minY;
            for (int i = 0; i < targetAreaHeight; i++) {
                board[0][i * 4 + 4] = PrintBoardTUI2.centerString(7, Integer.toString(counter + 1));
                counter++;
            }

            for (Integer coordinates : cardsMatrix.getOrderList()) {
                int actualX = coordinates / cardsMatrix.length();
                int actualY = coordinates % cardsMatrix.length();
                String[][] cardTUI = cardsMatrix.get(actualX, actualY).toStringTUI();

                for (int i = 0; i < cardTUI.length; i++) {
                    int boardX = i + 2 * (actualX - minX) + 1;
                    for (int j = 0; j < cardTUI[i].length; j++) {
                        int boardY = j + 4 * (actualY - minY) + 1;
                        board[boardX][boardY] = cardTUI[i][j];
                    }
                }
            }
        } else {
            board = new String[1][1];
            board[0][0] = "Nessuna carta piazzata";
        }

        for (int i = board[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < board.length; j++) {
                if (board[j][i] != null) {
                    System.out.print(board[j][i]);
                } else {
                    System.out.print("       ");
                }
            }
            System.out.println();
        }
    }

    private static String centerString (int width, String s) {
        return String.format("%-" + width  + "s",
                String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }
}
