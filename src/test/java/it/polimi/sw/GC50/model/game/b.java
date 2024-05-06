package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.lobby.Player;

import java.util.ArrayList;

public class b {
    int d = (84 * 4) - 13;
    int d2 = (84 * 2) - 7;

    public static void main(String[] args) {
        // Creazione della matrice principale 82x82 di carte
        PlayableCard[][] mat = new PlayableCard[84][84];
        int d = (84 * 4);
        int d2 = (84 * 2);
        String[][] mat2 = new String[d][d2];
        int index = 0;
        int index2 = 0;
        String[][] tmpString = new String[7][3];
        //CardsMatrix mat3 = new CardsMatrix(84);
        PlayerData pl= new PlayerData(40);
        for (int i = 0; i < 10; i=i+2) {
            for (int j = 1; j < 10; j=j+2) {
                Game game2 = new Game("Partita", 1, 20, new Player("Gianpaolo"));
                PhysicalCard card;
                card = game2.pickCard(DrawingPosition.GOLDDECK);
               // mat3.insertAtCornersCoordinates(card.getFront(), i, j);
                //mat3.insert(card.getFront(), i,j);
                //mat[i][j] = card.getFront();
                pl.placeCard(card.getFront(),i,j);
            }
        }
        //mat = mat3.getAsCornerMatrixWithoutOrder();
        for (int i = 1; i < 10; i=i+2) {
            for (int j = 0; j < 10; j=j+2) {
                Game game2 = new Game("Partita", 1, 20, new Player("Gianpaolo"));
                PhysicalCard card;
                card = game2.pickCard(DrawingPosition.GOLDDECK);
                //mat[i][j] = card.getFront();
                //mat3.insertAtCornersCoordinates(card.getFront(), i, j);
               // mat3.insert(card.getFront(), i,j);
                pl.placeCard(card.getFront(),i,j);
            }
        }
        CardsMatrix mat3= pl.getCardsArea();
        for (int i = 0; i < mat3.length(); i++) {
            index2 = 0;
            for (int j = 0; j < mat3.length(); j++) {
                if(mat3.getAtCornersCoordinates(i,j) != null) {
                //if (mat[i][j] != null) {
                    //tmpString = mat[i][j].toStringTui();
                    tmpString = mat3.getAtCornersCoordinates(i, j).toStringTui();
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



