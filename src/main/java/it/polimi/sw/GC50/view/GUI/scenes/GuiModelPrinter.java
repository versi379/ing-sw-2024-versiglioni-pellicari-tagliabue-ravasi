package it.polimi.sw.GC50.view.GUI.scenes;

import it.polimi.sw.GC50.model.card.*;

public class GuiModelPrinter {

    /**
     * Identifies the image associated with the given playable card by
     * returning the name of the image file in the resources.
     * Convention: c.p.b.ri.sw.nw.ne.se
     */
    public static String identifyPlayableCard(PlayableCard card) {
        String colorCode = String.valueOf(card.getColor().ordinal());
        String pointsCode = String.valueOf(card.getPoints());
        String bonusCode = extractBonusCode(card);
        String fixedResourcesCode = extractFixedResourcesCode(card);
        String cornersCode = extractCornersCode(card);
        StringBuilder imageCodeBuilder = new StringBuilder();
        imageCodeBuilder.append(colorCode)
                .append(pointsCode)
                .append(bonusCode)
                .append(fixedResourcesCode)
                .append(cornersCode);
        String imageCode = imageCodeBuilder.toString();
        return imageCode;
    }

    private static String extractBonusCode(PlayableCard card) {
        if (card.getBonus() instanceof BlankBonus) {
            return "0";
        } else if(card.getBonus() instanceof CoveredCornersBonus) {
            return "1";
        } else {
            return "2";
        }
    }

    private static String extractFixedResourcesCode(PlayableCard card) {
        String code = "";
        for (Resource fixedResource : card.getFixedResources()) {
            code += fixedResource.ordinal();
        }
        return code;
    }

    private static String extractCornersCode(PlayableCard card) {
        String code = "";
        for (Corner corner : card.getCorners()) {
            if (corner.isFull()) {
                code += corner.getResource().ordinal();
            }
        }
        return code;
    }

}
