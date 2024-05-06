package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;

public class PrintBoard {
    private CardsMatrix cardsMatrix;
    private int d = (84 * 4) - 13;
    private int d2 = (84 * 2) - 7;
    String[][] mat2;

    PrintBoard(CardsMatrix cardsMatrix) {
        PlayableCard[][] mat = new PlayableCard[84][84];
        mat = cardsMatrix.getAsCornerMatrixWithoutOrder();
        mat2 = new String[d][d2];
        int index = 0;
        int index2 = 0;
        String[][] tmpString = new String[7][3];
    /*
        for (int i = 0; i < 82; i = i + 2) {
            for (int j = 1; j < 82; j = j + 2) {
                Game game2 = new Game("Partita", 1, 20, new Player("Gianpaolo"));
                PhysicalCard card;
                card = game2.pickCard(DrawingPosition.GOLDDECK);
                mat[i][j] = card.getFront();
            }
        }
        for (int i = 1; i < 82; i = i + 2) {
            for (int j = 0; j < 82; j = j + 2) {
                Game game2 = new Game("Partita", 1, 20, new Player("Gianpaolo"));
                PhysicalCard card;
                card = game2.pickCard(DrawingPosition.GOLDDECK);
                mat[i][j] = card.getFront();
            }
        }*/

        for (int i = 0; i < 84; i++) {
            index2 = 0;
            for (int j = 0; j < 84; j++) {

                if (mat[i][j] != null) {
                    tmpString = mat[i][j].toStringTui();
                    for (int k = 0; k < 7; k++) {
                        for (int z = 0; z < 3; z++) {
                            mat2[index + k][index2 + z] = new String();
                            mat2[index + k][index2 + z] = tmpString[k][z];

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

    private static Boolean ck(int x, int h, String[][] mat2) {
        int d = (84 * 4);
        int d2 = (84 * 2);
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





