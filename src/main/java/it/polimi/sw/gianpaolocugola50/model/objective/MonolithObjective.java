package it.polimi.sw.gianpaolocugola50.model.objective;

import it.polimi.sw.gianpaolocugola50.model.card.Color;
import it.polimi.sw.gianpaolocugola50.model.game.CardsMatrix;
import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;
import it.polimi.sw.gianpaolocugola50.model.card.PlayableCard;

import java.util.Optional;

/**
 * Utilized for objective cards which have a monolith picture on the front,
 * whose condition consists of the number of cards of a certain color
 * arranged in a specific diagonal pattern
 */
public class MonolithObjective implements Objective {
    private final Color targetColor;
    private final MonolithOrientation orientation;

    public MonolithObjective(Color targetColor, MonolithOrientation orientation) {
        this.targetColor = targetColor;
        this.orientation = orientation;
    }

    @Override
    public int checkCondition(PlayerData playerData) {
        CardsMatrix cardsArea = adjustOrientation(playerData.getCardsArea());
        int result = 0;
        int currentCount;

        for (int i = 0; i < cardsArea.length(); i++) {
            currentCount = 0;
            for (int j = 0; j < cardsArea.length(); j++) {
                if (targetColor.equals(Optional.ofNullable(cardsArea.get(i, j))
                        .map(PlayableCard::getColor)
                        .orElse(null))) {
                    currentCount++;
                }
                if (currentCount == 3) {
                    result++;
                    currentCount = 0;
                }
            }
        }
        return result;
    }

    private CardsMatrix adjustOrientation(CardsMatrix matrix) {
        if (orientation == MonolithOrientation.LEFTDIAGONAL) {
            return matrix.transpose();
        }
        return matrix;
    }
}
