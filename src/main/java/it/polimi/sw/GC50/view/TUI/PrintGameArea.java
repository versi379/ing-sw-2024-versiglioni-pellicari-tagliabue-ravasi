package it.polimi.sw.GC50.view.TUI;


import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;

import java.util.ArrayList;
import java.util.List;

public class PrintGameArea {
    private List<PlayerArea> playerAreas;
    private PlayableCard[] decks;
    private PhysicalCard[] hand;
    private int d = (84 * 4);
    private int d2 = (84 * 2);


    public PrintGameArea() {
        playerAreas = new ArrayList<>();
        this.decks = new PlayableCard[6];
        this.hand = new PhysicalCard[3];
    }

    public void update(List<ModelMex.SinglePlayerArea> singlePlayerAreas, PlayableCard[] decks, List<PhysicalCard> hand) {
        this.playerAreas = new ArrayList<>();
        for (ModelMex.SinglePlayerArea singlePlayerArea : singlePlayerAreas) {
            this.playerAreas.add(new PlayerArea(singlePlayerArea.getNickname(), singlePlayerArea.getPoint(), singlePlayerArea.getColor(), singlePlayerArea.getCardsMatrix()));
        }
        this.decks = decks;
        for (int i = 0; i < hand.size(); i++) {
            this.hand[i] = hand.get(i);
        }
    }

    public int addPlayerArea(String nickname, int point, Color color, CardsMatrix cardsMatrix) {
        playerAreas.add(new PlayerArea(nickname, point, color, cardsMatrix));
        return playerAreas.size() - 1;
    }

    public void UpdateCardBoard(CardsMatrix cardsMatrix, int index) {
        this.playerAreas.get(index).setCardsMatrix(cardsMatrix);
    }

    public void updatePlayerPoint(int index, int point) {
        this.playerAreas.get(index).setPoints(point);
    }

    public void updatePlayerHand(PhysicalCard[] hand) {
        this.hand = hand;
    }

    public void updateDeck(PlayableCard[] decks) {
        this.decks = decks;
    }

    private String[][] stringBoard(int matrixIndex) {
        String[][] mat2 = new String[84 * 4][84 * 2];
        String[][] tmpString = new String[7][3];
        int index = 0;
        int index2 = 0;



        for (int i = this.playerAreas.get(matrixIndex).getCardsMatrix().length() - 1; i >= 0; i--) {
            index2 = 0;
            for (int j = 0; j < this.playerAreas.get(matrixIndex).getCardsMatrix().length(); j++) {
                if (this.playerAreas.get(matrixIndex).getCardsMatrix().get(j, i) != null) {
                    tmpString = this.playerAreas.get(matrixIndex).getCardsMatrix().get(j, i).toStringTUI();
                    int c = 0;
                    for (int k = 6; k >= 0; k--) {
                        switch (k) {
                            case 4, 5, 6: {
                                //nw
                                if (this.playerAreas.get(matrixIndex).getCardsMatrix().isCornerUncovered(j, i, 1)) {
                                    mat2[index + c][index2] = tmpString[0][k];
                                }
                                mat2[index + c][index2 + 1] = tmpString[1][k];
                                //ne
                                if (this.playerAreas.get(matrixIndex).getCardsMatrix().isCornerUncovered(j, i, 2)) {
                                    mat2[index + c][index2 + 2] = tmpString[2][k];
                                }
                                break;
                            }
                            case 3: {
                                //center
                                mat2[index + c][index2] = tmpString[0][k];
                                mat2[index + c][index2 + 1] = tmpString[1][k];
                                mat2[index + c][index2 + 2] = tmpString[2][k];
                                break;
                            }
                            case 0, 1, 2: {
                                //sw
                                if (this.playerAreas.get(matrixIndex).getCardsMatrix().isCornerUncovered(j, i, 0)) {
                                    mat2[index + c][index2] = tmpString[0][k];
                                }
                                mat2[index + c][index2 + 1] = tmpString[1][k];
                                //se
                                if (this.playerAreas.get(matrixIndex).getCardsMatrix().isCornerUncovered(j, i, 3)) {
                                    mat2[index + c][index2 + 2] = tmpString[2][k];
                                }
                                break;
                            }
                        }
                        c++;
                    }
                }
                index2 = index2 + 2;
            }
            index = index + 4;
        }
        return mat2;
    }

    public void print(int index) {
        if (index > this.playerAreas.size() - 1) {
            return;
        }
        String[][] mat = stringBoard(index);
        int ck = 0;
        for (int i = 0; i < mat.length; i++) {
            if (!ck(1, i, mat)) {
                for (int j = 0; j < mat[0].length; j++) {
                    if (!ck(2, j, mat)) {
                        if (ck(i, ck, mat)) {
                        }
                        if (mat[i][j] != null) {
                            System.out.print(mat[i][j]);
                        } else {
                            System.out.print("       ");
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    public void printAllBoard(boolean condition, int index) {
        ArrayList<String[][]> mat = new ArrayList<>();
        int[] ck = new int[this.playerAreas.size()];
        String hand[][] = new String[7][5];
        String[][] pointBoard = getPointBoard();
        int counter = 0;
        if (!condition) {
            mat.add(stringBoard(index));
        } else {
            for (int i = 0; i < this.playerAreas.size(); i++) {
                mat.add(stringBoard(i));
                ck[i] = 0;
            }
        }
        /////point board && chat
        for (int i = 0; i < pointBoard.length; i++) {
            for (int j = 0; j < pointBoard[0].length; j++) {
                System.out.print(pointBoard[i][j]);
            }
            System.out.println();
        }

        int control = 0;
        for (int i = 0; i < mat.get(0).length; i++) {
            if (!ck(1, i, mat)) {
                for (int z = 0; z < mat.size(); z++) {
                    for (int j = 0; j < mat.get(0)[0].length; j++) {
                        if (j == 0) {
                            System.out.print(" ║║ ");
                        }
                        if (!ck(2, j, mat)) {

                            if (mat.get(z)[i][j] != null) {
                                System.out.print(mat.get(z)[i][j]);
                            } else {
                                System.out.print("       ");
                            }
                        }

                    }
                }
                System.out.println();
                control = 1;
            }
        }
        if (condition) {
            for (int i = 0; i < this.playerAreas.size(); i++) {
                System.out.print("Player: " + this.playerAreas.get(i).getNickname());
                for (int j = 0; j < counter; j++) {

                    System.out.print("       ");

                }
            }
        }


        ////print hand && deck
        System.out.println("\n");
        System.out.println("Hand:                                                                                Uncovered cards:                            Up: Gold deck down: Resource deck");
        for (int i = 0; i < 2; i++) {
            for (int j = 6; j >= 0; j--) {
                for (int z = 0; z < 6; z++) {
                    for (int k = 0; k < 3; k++) {

                        if (i == 0) {
                            if (z < 3) {
                                if (z < this.hand.length) {
                                    if (this.hand[z] != null) {
                                        System.out.print(this.hand[z].getFront().toStringTUI()[k][j]);
                                    } else {
                                        System.out.print("       ");
                                    }

                                    if (z == 2 && k == 2) {
                                        System.out.print("         ║║          ");
                                    }


                                } else {
                                    System.out.print("       ");
                                    if (z == 2 && k == 2) {
                                        System.out.print("         ║║          ");

                                    }
                                }
                            } else {
                                if (z - 3 < decks.length) {
                                    if (this.decks[z - 3] != null) {
                                        System.out.print(decks[z - 3].toStringTUI()[k][j]);
                                    } else {
                                        System.out.print("       ");
                                    }

                                } else {
                                    System.out.print("       ");
                                }
                            }
                        } else {
                            if (z < 3) {
                                if (z < this.hand.length) {
                                    System.out.print(this.hand[z].getBack().toStringTUI()[k][j]);
                                    if (z == 2 && k == 2) {
                                        System.out.print("         ║║          ");

                                    }
                                } else {
                                    System.out.print("       ");
                                    if (z == 2 && k == 2) {
                                        System.out.print("         ║║          ");

                                    }
                                }
                            } else {
                                if (z < decks.length && decks[z] != null) {
                                    System.out.print(decks[z].toStringTUI()[k][j]);
                                } else {
                                    System.out.print("       ");
                                }
                            }
                        }
                    }
                }
                System.out.println();

            }
            System.out.print("                                                                        ║║          ");
            System.out.println();
        }
    }


    private Boolean ck(int x, int h, ArrayList<String[][]> mat2) {
        //raw
        if (x == 1) {
            for (int i = 0; i < mat2.get(0)[0].length; i++) {
                for (int z = 0; z < mat2.size(); z++) {
                    if (mat2.get(z)[h][i] != null) {
                        return false;
                    }
                }
            }
            return true;
        } else if (x == 2) {
            //colum
            for (int i = 0; i < d; i++) {
                for (int z = 0; z < mat2.size(); z++) {
                    if (mat2.get(z)[i][h] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
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

    private String[][] getPointBoard() {
        String[][] pointBoard = new String[8 * 5][4 * 3];
        int c = 0;
        int index = 0;
        int index2 = 0;
        for (int i = 0; i < 8; i++) {
            if (i % 2 != 0) {
                c = c + 3;
            }
            index2 = 0;
            for (int j = 0; j < 4; j++) {
                String[][] tmp = square(c);
                for (int k = 0; k < tmp.length; k++) {
                    for (int h = 0; h < tmp[0].length; h++) {
                        pointBoard[index + k][index2 + h] = tmp[k][h];
                    }
                }
                if (i % 2 != 0) {
                    c--;
                } else {
                    c++;
                }

                index2 = index2 + 3;
            }
            if (i % 2 != 0) {
                c = c + 5;
            }
            index = index + 5;
        }
        return pointBoard;
    }

    private String[][] square(int point) {
        String[][] square = new String[5][3];
        ArrayList<String> color = new ArrayList<>();


        if (point == 30 || point == 31) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 3; j++) {
                    if (square[i][j] == null) {
                        if (j == 0 || j == 2) {
                            square[i][j] = "   ";
                        } else {
                            square[i][j] = "           ";
                        }

                    }
                }
            }
            return square;
        }


        String colorEnd = "\u001B[0m";


        if (point >= 10) {
            square[2][1] = ("╣   " + point + "    ╠");
        } else {
            square[2][1] = ("╣    " + point + "    ╠");
        }
        if (point == 20) {
            String pointventi = "\u001B[31m" + point + colorEnd;
            square[2][1] = ("╣   " + pointventi + "    ╠");
        }
        square[0][1] = ("╬════╩════╬");
        square[3][1] = ("╬════╦════╬");
        // square[0][0]=("       ");

        for (int i = 0; i < this.playerAreas.size(); i++) {
            if (this.playerAreas.get(i).getPoints() == point) {
                color.add(switch (this.playerAreas.get(i).getColor()) {
                    case WHITE -> "\u001B[37m";
                    case GREEN -> "\u001B[32m";
                    case BLUE -> "\u001B[34m";
                    case RED -> "\u001B[31m";
                    case PURPLE -> "\u001B[35m";
                });

            }
        }

        if (color.size() == 4) {
            String p1 = color.get(0) + "◉" + colorEnd;
            String p2 = color.get(1) + "◉" + colorEnd;
            String p3 = color.get(2) + "◉" + colorEnd;
            String p4 = color.get(3) + "◉" + colorEnd;
            square[1][1] = ("╣ " + p1 + " " + p2 + " " + p3 + " " + p4 + " ╠");
        } else if (color.size() == 3) {

            String p1 = color.get(0) + "◉" + colorEnd;
            String p2 = color.get(1) + "◉" + colorEnd;
            String p3 = color.get(2) + "◉" + colorEnd;

            square[1][1] = ("╣ " + p1 + " " + p2 + " " + p3 + " " + " " + " ╠");
        } else if (color.size() == 2) {
            String p1 = color.get(0) + "◉" + colorEnd;
            String p2 = color.get(1) + "◉" + colorEnd;

            square[1][1] = ("╣ " + p1 + " " + p2 + " " + " " + " " + " " + " ╠");
        } else if (color.size() == 1) {
            String p1 = color.get(0) + "◉" + colorEnd;

            square[1][1] = ("╣ " + p1 + " " + " " + " " + " " + " " + " " + " ╠");

        } else {
            square[1][1] = ("╣ " + " " + " " + " " + " " + " " + " " + "  ╠");
        }


        switch (point) {
            case 0: {
                square[2][2] = ("═══");
                break;
            }
            case 1, 2, 6, 5, 9, 10, 14, 13, 17, 18, 22, 21, 25, 26: {
                square[2][2] = ("═══");
                square[2][0] = ("═══");
                break;
            }
            case 3, 11, 19, 27: {
                square[2][0] = ("═══");
                square[4][1] = ("        ║     ");

                break;
            }
            case 7, 15, 23: {
                square[2][2] = ("═══");
                square[4][1] = ("     ║     ");

                break;
            }
            case 4, 12, 20, 28: {
                square[2][0] = ("═══");

                break;
            }
            case 8, 16, 24, 29: {
                square[2][2] = ("═══");
                break;
            }
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if (square[i][j] == null) {
                    if (j == 0 || j == 2) {
                        square[i][j] = "   ";
                    } else {
                        square[i][j] = "          ";
                    }

                }
            }
        }

        return square;
    }

    private class PlayerArea {
        private String nickname;
        private int points;
        private Color color;
        private CardsMatrix cardsMatrix;

        public PlayerArea(String nickname, int points, Color color, CardsMatrix cardsMatrix) {
            this.nickname = nickname;
            this.points = points;
            this.color = color;
            this.cardsMatrix = cardsMatrix;
        }

        public String getNickname() {
            return nickname;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public int getPoints() {
            return points;
        }

        public Color getColor() {
            return color;
        }

        public void setCardsMatrix(CardsMatrix cardsMatrix) {
            this.cardsMatrix = cardsMatrix;
        }

        public CardsMatrix getCardsMatrix() {
            return cardsMatrix;
        }
    }
}
