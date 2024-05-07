package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;

public class PrintBoard {
    private CardsMatrix cardsMatrix;
    private int d = (84 * 4) - 13;
    private int d2 = (84 * 2) - 7;
    String[][] mat2;

    public PrintBoard(CardsMatrix cardsMatrix) {
        PlayableCard[][] mat = cardsMatrix.getAsCornerMatrixWithoutOrder();
        mat2 = new String[d][d2];
        int index = 0;
        for (int i = 0; i < mat2.length; i++) {
            for (int j = 0; j < mat2[0].length; j++) {
                mat2[i][j] = null;
            }
        }

        for (int i = 0; i < cardsMatrix.length(); i++) {
            int index2 = 0;
            for (int j = 0; j < cardsMatrix.length(); j++) {
                if (mat[i][j] != null) {
                    String[][] tmpString = mat[i][j].toStringTUI();
                    for (int k = 0; k < 7; k++) {
                        switch (k) {
                            case 0, 1, 2: {
                                //nw
                                if (cardsMatrix.isCornerUncovered(i, j, 1)) {
                                    mat2[index + k][index2] = tmpString[k][0];
                                }
                                mat2[index + k][index2 + 1] = tmpString[k][1];
                                //ne
                                if (cardsMatrix.isCornerUncovered(i, j, 2)) {
                                    mat2[index + k][index2 + 2] = tmpString[k][2];
                                }
                                break;
                            }
                            case 3: {
                                //center
                                mat2[index + k][index2] = tmpString[k][0];
                                mat2[index + k][index2 + 1] = tmpString[k][1];
                                mat2[index + k][index2 + 2] = tmpString[k][2];
                                break;
                            }
                            case 4, 5, 6: {
                                //sw
                                if (cardsMatrix.isCornerUncovered(i, j, 0)) {
                                    mat2[index + k][index2] = tmpString[k][0];
                                }
                                mat2[index + k][index2 + 1] = tmpString[k][1];
                                //se
                                if (cardsMatrix.isCornerUncovered(i, j, 3)) {
                                    mat2[index + k][index2 + 2] = tmpString[k][2];
                                }
                                break;
                            }
                        }
                    }
                }
                index2 = index2 + 2;
            }
            index = index + 4;
        }
        int ck = 0;
        for (int i = 0; i < d; i++) {
            if (!ck(1, i, mat2)) {
                for (int j = 0; j < d2; j++) {
                    if (!ck(2, j, mat2)) {
                        if (ck(i, ck, mat2)) {
                        }
                        if (mat2[i][j] != null) {
                            System.out.print(mat2[i][j]);
                        } else {

                            System.out.print("       ");
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    private Boolean ck(int x, int h, String[][] mat2) {
        if (x == 1) {
            for (int i = 0; i < d2; i++) {
                if (mat2[h][i] != null) {
                    return false;
                }
            }
            return true;
        } else if (x == 2) {
            for (int i = 0; i < d; i++) {
                if (mat2[i][h] != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}





