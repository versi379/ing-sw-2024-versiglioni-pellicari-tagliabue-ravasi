package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.gameMexNet.ModelMex;
import it.polimi.sw.GC50.view.PlayerArea1;

import java.util.*;

public class ModelPrinter {
    private Map<String, PlayerArea1> playerAreas;
    private List<PlayableCard> decks;
    private List<PhysicalCard> hand;
    private ObjectiveCard secretObjective;

    public void update(List<ModelMex.SinglePlayerArea> singlePlayerAreas, PlayableCard[] decks, List<PhysicalCard> hand) {
        this.playerAreas = new HashMap<>();
        for (ModelMex.SinglePlayerArea singlePlayerArea : singlePlayerAreas) {
            this.playerAreas.put(singlePlayerArea.getNickname(),
                    new PlayerArea1(singlePlayerArea.getNickname(), singlePlayerArea.getColor(),
                            singlePlayerArea.getCardsMatrix(),singlePlayerArea.getPoint()));
        }
        this.decks = new ArrayList<>(Arrays.asList(decks));
        this.hand = new ArrayList<>(hand);
    }

    public void printHand() {
        System.out.println();
        System.out.println("Carte in mano:");
        System.out.println();

        String[][] handMatrix = new String[4 * hand.size()][7 * 2 + 1];

        handMatrix[0][7 * 2] = "Indici: ";
        handMatrix[0][7 + 3] = "  1)   ";
        handMatrix[0][3] = "  2)   ";

        for (int cardsCounter = 0; cardsCounter < hand.size(); cardsCounter++) {
            handMatrix[4 * cardsCounter + 2][7 * 2] = "  " + (cardsCounter + 1) + ")   ";

            String[][] cardTUI = hand.get(cardsCounter).getFront().toStringTUI();
            for (int i = 0; i < cardTUI.length; i++) {
                int matrixX = i + 4 * cardsCounter + 1;
                for (int j = 0; j < cardTUI[i].length; j++) {
                    int matrixY = j + 7;
                    handMatrix[matrixX][matrixY] = cardTUI[i][j];
                }
            }

            cardTUI = hand.get(cardsCounter).getBack().toStringTUI();
            for (int i = 0; i < cardTUI.length; i++) {
                int matrixX = i + 4 * cardsCounter + 1;
                for (int j = 0; j < cardTUI[i].length; j++) {
                    int matrixY = j;
                    handMatrix[matrixX][matrixY] = cardTUI[i][j];
                }
            }
        }

        printMatrix(handMatrix);
    }

    public void printDecks() {
        System.out.println();
        System.out.println("Carte pescabili:");
        System.out.println();

        String[][] handMatrix = new String[4 * 3][7 * 2 + 2];

        for (int cardsCounter = 0; cardsCounter < 3; cardsCounter++) {

            handMatrix[4 * cardsCounter + 2][7 * 2 + 1] = "  " + (cardsCounter + 1) + ")   ";
            String[][] cardTUI = decks.get(cardsCounter).toStringTUI();
            for (int i = 0; i < cardTUI.length; i++) {
                int matrixX = i + 4 * cardsCounter + 1;
                for (int j = 0; j < cardTUI[i].length; j++) {
                    int matrixY = j + 7 + 1;
                    handMatrix[matrixX][matrixY] = cardTUI[i][j];
                }
            }

            handMatrix[4 * cardsCounter + 2][7] = "  " + (cardsCounter + 3 + 1) + ")   ";
            cardTUI = decks.get(cardsCounter + 3).toStringTUI();
            for (int i = 0; i < cardTUI.length; i++) {
                int matrixX = i + 4 * cardsCounter + 1;
                for (int j = 0; j < cardTUI[i].length; j++) {
                    int matrixY = j;
                    handMatrix[matrixX][matrixY] = cardTUI[i][j];
                }
            }
        }

        printMatrix(handMatrix);
    }

    public void printPlayerArea(String nickname) {
        System.out.println();
        System.out.println("Area di gioco del giocatore " + nickname);
        System.out.println();

        String[][] boardMatrix;
        CardsMatrix cardsMatrix = playerAreas.get(nickname).getCardsMatrix();
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
            boardMatrix[0][0] = "Nessuna carta piazzata";
        }

        printMatrix(boardMatrix);
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

    private static String centerString(int width, String s) {
        return String.format("%-" + width + "s",
                String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }
}
