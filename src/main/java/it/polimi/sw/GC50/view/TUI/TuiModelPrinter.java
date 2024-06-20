package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.cards.PhysicalCard;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.objectives.ObjectiveCard;
import javafx.util.Pair;
import trash.PrintBoardTUI2;
import it.polimi.sw.GC50.view.PlayerDataView;

import java.util.List;
import java.util.Map;

public abstract class TuiModelPrinter {

    public static void printStarterCard(PhysicalCard starterCard) {
        String[][] starterMatrix = new String[3 * 2 + 1][7 + 1];

        starterMatrix[0][7] = "1) Fron";
        starterMatrix[1][7] = "t      ";
        starterMatrix[3 + 1][7] = "2) Back";

        String[][] cardTUI = starterCard.getFront().toStringTUI();
        for (int i = 0; i < cardTUI.length; i++) {
            int matrixX = i;
            for (int j = 0; j < cardTUI[i].length; j++) {
                int matrixY = j;
                starterMatrix[matrixX][matrixY] = cardTUI[i][j];
            }
        }

        cardTUI = starterCard.getBack().toStringTUI();
        for (int i = 0; i < cardTUI.length; i++) {
            int matrixX = i + 3 + 1;
            for (int j = 0; j < cardTUI[i].length; j++) {
                int matrixY = j;
                starterMatrix[matrixX][matrixY] = cardTUI[i][j];
            }
        }

        printMatrix(starterMatrix);
    }

    public static void printHand(List<PhysicalCard> hand) {
        String[][] handMatrix = new String[4 * hand.size() + 1][7 * 2 + 1];

        handMatrix[0][7 + 3] = " 1) Fro";
        handMatrix[1][7 + 3] = "nt:    ";
        handMatrix[0][3] = "  2) Ba";
        handMatrix[1][3] = "ck:    ";

        for (int cardsCounter = 0; cardsCounter < hand.size(); cardsCounter++) {
            handMatrix[4 * cardsCounter + 3][7 * 2] = "  " + (cardsCounter + 1) + ")   ";

            String[][] cardTUI = hand.get(cardsCounter).getFront().toStringTUI();
            for (int i = 0; i < cardTUI.length; i++) {
                int matrixX = i + 4 * cardsCounter + 2;
                for (int j = 0; j < cardTUI[i].length; j++) {
                    int matrixY = j + 7;
                    handMatrix[matrixX][matrixY] = cardTUI[i][j];
                }
            }

            cardTUI = hand.get(cardsCounter).getBack().toStringTUI();
            for (int i = 0; i < cardTUI.length; i++) {
                int matrixX = i + 4 * cardsCounter + 2;
                for (int j = 0; j < cardTUI[i].length; j++) {
                    int matrixY = j;
                    handMatrix[matrixX][matrixY] = cardTUI[i][j];
                }
            }
        }

        printMatrix(handMatrix);
    }

    public static void printPlayerArea(CardsMatrix cardsMatrix) {
        String[][] boardMatrix;

        int minX = cardsMatrix.getMinX();
        int maxX = cardsMatrix.getMaxX();
        int minY = cardsMatrix.getMinY();
        int maxY = cardsMatrix.getMaxY();

        int targetAreaWidth = maxX - minX + 1;
        int targetAreaHeight = maxY - minY + 1;
        if (targetAreaWidth > 0 && targetAreaHeight > 0) {
            boardMatrix = new String[(targetAreaWidth) * 2 + 1 + 1][(targetAreaHeight) * 4 + 3 + 1];

            int counter = minX;
            for (int i = 0; i < targetAreaWidth; i++) {
                boardMatrix[i * 2 + 2][0] = PrintBoardTUI2.centerString(7, Integer.toString(counter + 1));
                counter++;
            }
            counter = minY;
            for (int i = 0; i < targetAreaHeight; i++) {
                boardMatrix[0][i * 4 + 4] = PrintBoardTUI2.centerString(7, Integer.toString(counter + 1));
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
                        boardMatrix[boardX][boardY] = cardTUI[i][j];
                    }
                }
            }
        } else {
            boardMatrix = new String[1][1];
            boardMatrix[0][0] = TuiView.redTxt + "No cards placed" + TuiView.baseTxt;
        }

        printMatrix(boardMatrix);
    }

    public static void printScoresPlaying(Map<String, Integer> scores) {
       for (String nickname : scores.keySet()) {
            System.out.println("Player \"" + nickname + "\" -> " + scores.get(nickname));
       }
    }

    public static void printScoresEnd(Map<String, Pair<Integer, Integer>> scores) {
        for (String nickname : scores.keySet()) {
            System.out.println("Player \"" + nickname + "\" -> total: " + scores.get(nickname).getKey() +
                    ", objectives: " + scores.get(nickname).getValue());
        }
    }

    public static void printDecks(PlayableCard[] decks) {
        String[][] decksMatrix = new String[4 * 3 + 1][7 * 2 + 2];

        decksMatrix[0][11] = " Resour";
        decksMatrix[1][11] = "ce:    ";
        decksMatrix[0][3] = "     Go";
        decksMatrix[1][3] = "ld:    ";

        for (int cardsCounter = 0; cardsCounter < 3; cardsCounter++) {

            decksMatrix[4 * cardsCounter + 3][7 * 2 + 1] = "  " + (cardsCounter + 1) + ")   ";
            String[][] cardTUI = decks[cardsCounter].toStringTUI();
            if (cardTUI != null) {
                for (int i = 0; i < cardTUI.length; i++) {
                    int matrixX = i + 4 * cardsCounter + 2;
                    for (int j = 0; j < cardTUI[i].length; j++) {
                        int matrixY = j + 7 + 1;
                        decksMatrix[matrixX][matrixY] = cardTUI[i][j];
                    }
                }
            } else {
                decksMatrix[4 * cardsCounter + 3][7 * 2 - 1] = " Empty ";
            }

            decksMatrix[4 * cardsCounter + 3][7] = "  " + (cardsCounter + 3 + 1) + ")   ";
            cardTUI = decks[cardsCounter + 3].toStringTUI();
            if (cardTUI != null) {
                for (int i = 0; i < cardTUI.length; i++) {
                    int matrixX = i + 4 * cardsCounter + 2;
                    for (int j = 0; j < cardTUI[i].length; j++) {
                        int matrixY = j;
                        decksMatrix[matrixX][matrixY] = cardTUI[i][j];
                    }
                }
            } else {
                decksMatrix[4 * cardsCounter + 3][7 - 1] = " Empty ";
            }
        }

        printMatrix(decksMatrix);
    }

    private static void printMatrix(String[][] stringMatrix) {
        for (int i = stringMatrix[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < stringMatrix.length; j++) {
                if (stringMatrix[j][i] != null) {
                    System.out.print(stringMatrix[j][i]);
                } else {
                    System.out.print("       ");
                }
            }
            System.out.println();
        }
    }
}
