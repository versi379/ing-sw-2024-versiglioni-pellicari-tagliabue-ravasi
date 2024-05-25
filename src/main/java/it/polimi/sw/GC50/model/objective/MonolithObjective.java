package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.card.Color;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.Optional;

/**
 * Utilized for objective cards which have a monolith picture on the front,
 * whose condition consists of the number of cards of a certain color
 * arranged in a specific diagonal pattern
 */
public class MonolithObjective implements Objective , Serializable {
    private final Color targetColor;
    private final MonolithOrientation orientation;

    /**
     * Constructs an instance of MonolithObjective
     * @param targetColor
     * @param orientation
     */
    public MonolithObjective(Color targetColor, MonolithOrientation orientation) {
        this.targetColor = targetColor;
        this.orientation = orientation;
    }

    /**
     * @return target color
     */
    public Color getTargetColor() {
        return targetColor;
    }

    /**
     * @return card orientation
     */
    public MonolithOrientation getOrientation() {
        return orientation;
    }

    /**
     * To check diagonal patterns, cards can't be repeated
     * when a pattern is found (going in the best order) set to NULL cards that compose it and continue
     * this method works on a copy on Card Matrix not on the original one
     * @param playerData
     * @return
     */
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

    @Override
    public String toStringTUI() {
        return "Monolith Objective => target color: " + getTargetColor() +
                ", orientation: " + getOrientation();
    }

    /**
     * rotate the matrix 45°
     * @param matrix
     * @return
     */
    private CardsMatrix adjustOrientation(CardsMatrix matrix) {
        if (orientation == MonolithOrientation.LEFTDIAGONAL) {
            return matrix.rotate45().transposePrimary();
        }
        return matrix.rotate45();
    }
}
