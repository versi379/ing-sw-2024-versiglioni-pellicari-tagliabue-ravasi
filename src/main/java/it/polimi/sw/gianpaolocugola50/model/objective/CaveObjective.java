package it.polimi.sw.gianpaolocugola50.model.objective;

import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;
import it.polimi.sw.gianpaolocugola50.model.game.CardsMatrix;
import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;
import it.polimi.sw.gianpaolocugola50.model.card.Color;

import java.util.Optional;

public class CaveObjective implements Objective {
    private final Color targetColor1;
    private final Color targetColor2;
    private final CaveOrientation orientation;

    public CaveObjective(Color targetColor1, Color targetColor2, CaveOrientation orientation) {
        this.targetColor1 = targetColor1;
        this.targetColor2 = targetColor2;
        this.orientation = orientation;
    }

    @Override
    public int checkCondition(PlayerData playerData) {
        CardsMatrix cardsArea = adjustOrientation(playerData.getCardsArea());
        int result = 0;

        for (int i = 0; i < cardsArea.length() - 1; i++) {
            for (int j = 0; j < cardsArea.length() - 2; j++) {
                if (targetColor2.equals(Optional.ofNullable(cardsArea.get(i, j))
                        .map(PlayableCard::getColor)
                        .orElse(null)) &&
                        targetColor1.equals(Optional.ofNullable(cardsArea.get(i, j + 1))
                                .map(PlayableCard::getColor)
                                .orElse(null)) &&
                        targetColor1.equals(Optional.ofNullable(cardsArea.get(i + 1, j + 2))
                                .map(PlayableCard::getColor)
                                .orElse(null))) {
                    cardsArea.insert(null, i, j + 1);
                    cardsArea.insert(null, i + 1, j + 2);
                    result++;
                }
            }
        }
        return result;
    }

    private CardsMatrix adjustOrientation(CardsMatrix matrix) {
        switch (orientation) {
            case CaveOrientation.INVERTEDL -> {
                return matrix.transpose();
            }
            case CaveOrientation.INVERTEDJ -> {
                return matrix.invert();
            }
            case CaveOrientation.UPRIGHTJ -> {
                return matrix.transpose().invert();
            }
            default -> {
                return matrix;
            }
        }
    }
}
