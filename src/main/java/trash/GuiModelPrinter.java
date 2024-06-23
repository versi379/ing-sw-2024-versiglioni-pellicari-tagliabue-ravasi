package trash;

import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.view.PlayerDataView;
import it.polimi.sw.GC50.view.TUI.TuiView;
import trash.PrintBoardTUI2;

public class GuiModelPrinter {

    public static void printPlayerArea(String nickname, PlayerDataView playerArea) {
        String[][] boardMatrix;

        CardsMatrix cardsMatrix = playerArea.getCardsMatrix();
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
                        // qui stampo la carta card[i][j] (senza chiamare il toString, ma direttamente con img)
                        // in posizione boardX,boardY nella GUI

                    }
                }
            }
        } else {
            boardMatrix = new String[1][1];
            boardMatrix[0][0] = TuiView.redTxt + "No cards placed" + TuiView.baseTxt;
        }

        //printMatrix(boardMatrix);
    }

// ALTERNATIVE WAY TO ASSOCIATE PLAYABLE CARDS TO IMAGES (USING A CODE)
//    /**
//     * Identifies the image associated with the given playable card by
//     * returning the name of the image file in the resources.
//     * Convention: c.p.b.ri.sw.nw.ne.se
//     */
//    public static String identifyPlayableCard(PlayableCard card) {
//        String colorCode = String.valueOf(card.getColor().ordinal());
//        String pointsCode = String.valueOf(card.getPoints());
//        String bonusCode = extractBonusCode(card);
//        String fixedResourcesCode = extractFixedResourcesCode(card);
//        String cornersCode = extractCornersCode(card);
//        StringBuilder imageCodeBuilder = new StringBuilder();
//        imageCodeBuilder.append(colorCode)
//                .append(pointsCode)
//                .append(bonusCode)
//                .append(fixedResourcesCode)
//                .append(cornersCode);
//        String imageCode = imageCodeBuilder.toString();
//        return imageCode;
//    }
//
//    private static String extractBonusCode(PlayableCard card) {
//        if (card.getBonus() instanceof BlankBonus) {
//            return "0";
//        } else if(card.getBonus() instanceof CoveredCornersBonus) {
//            return "1";
//        } else {
//            return "2";
//        }
//    }
//
//    private static String extractFixedResourcesCode(PlayableCard card) {
//        String code = "";
//        for (Resource fixedResource : card.getFixedResources()) {
//            code += fixedResource.ordinal();
//        }
//        return code;
//    }
//
//    private static String extractCornersCode(PlayableCard card) {
//        String code = "";
//        for (Corner corner : card.getCorners()) {
//            if (corner.isFull()) {
//                code += corner.getResource().ordinal();
//            }
//        }
//        return code;
//    }

}
