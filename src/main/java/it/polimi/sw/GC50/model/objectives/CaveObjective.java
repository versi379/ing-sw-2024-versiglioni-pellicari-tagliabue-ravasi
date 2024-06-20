package it.polimi.sw.GC50.model.objectives;

import it.polimi.sw.GC50.model.cards.Color;
import it.polimi.sw.GC50.model.cards.PlayableCard;
import it.polimi.sw.GC50.model.game.CardsMatrix;
import it.polimi.sw.GC50.model.game.PlayerData;
import it.polimi.sw.GC50.view.TUI.TuiView;

import java.io.Serializable;
import java.util.Optional;

/**
 * Utilized for objective cards which have a cave picture on the front,
 * whose condition consists of the number of cards of certain colors
 * arranged in a specific L-like pattern
 */
public class CaveObjective implements Objective , Serializable {
    /**
     * Represents the color of the two vertical cards
     */
    private final Color targetColor1;

    /**
     * Represents the color of the remaining card
     */
    private final Color targetColor2;

    /**
     * Represents the orientation of the pattern required
     */
    private final CaveOrientation orientation;

    /**
     * Constructs an instance of CaveObjective
     * @param targetColor1  Represents the color of the two vertical cards
     * @param targetColor2  Represents the color of the remaining card
     * @param orientation   Represents the orientation of the pattern required
     */
    public CaveObjective(Color targetColor1, Color targetColor2, CaveOrientation orientation) {
        this.targetColor1 = targetColor1;
        this.targetColor2 = targetColor2;
        this.orientation = orientation;
    }

    /**
     * @return the color of the two vertical cards
     */
    public Color getTargetColor1() {
        return targetColor1;
    }

    /**
     * @return the color of the remaining card
     */
    public Color getTargetColor2() {
        return targetColor2;
    }

    /**
     * @return the orientation of the pattern required
     */
    public CaveOrientation getOrientation() {
        return orientation;
    }

    /**
     * To check L-shaped patterns, cards can't be repeated
     * when a pattern is found (going in the best order) set to NULL cards that compose it and continue
     * this method works on a copy on Card Matrix not on the original one
     * @param playerData        player selected
     * @return result
     */
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

    @Override
    public String toStringTUI() {
        return "Cave Objective => target color 1: " + getTargetColor1().toStringTUI() + "██" + TuiView.baseTxt +
                ", target color 2: " + getTargetColor2().toStringTUI() + "██" + TuiView.baseTxt +
                ", orientation: " + getOrientation().toStringTUI();
    }

    /**
     * if CaveOrientation is UPRIGHTJ ->  Flips matrix over its primary diagonal
     * if CaveOrientation is INVERTEDL ->  Flips matrix over its secondary diagonal
     * if CaveOrientation is INVERTEDJ ->  Flips matrix over its primary diagonal then over its secondary diagonal
     * @param matrix    card matrix
     * @return          flipped matrix
     */
    public CardsMatrix adjustOrientation(CardsMatrix matrix) {
        switch (orientation) {
            case CaveOrientation.UPRIGHTJ -> {
                return matrix.rotate45().transposePrimary();
            }
            case CaveOrientation.INVERTEDL -> {
                return matrix.rotate45().transposeSecondary();
            }
            case CaveOrientation.INVERTEDJ -> {
                return matrix.rotate45().transposePrimary().transposeSecondary();
            }
            default -> {
                return matrix.rotate45();
            }
        }
    }
}
