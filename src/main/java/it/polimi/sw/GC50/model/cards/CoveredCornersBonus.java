package it.polimi.sw.GC50.model.cards;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the bonus utilized for cards whose score depends
 * on the amount of covered corners at the moment of placement
 */
public class CoveredCornersBonus implements Bonus, Serializable {

    /**
     * Constructs a new CoveredCornersBonus instance
     */
    public CoveredCornersBonus() {
    }

    /**
     * Computes the bonus multiplier by which the card score is calculated when placed
     *
     * @param card       PlayableCard associated with this bonus
     * @param playerData PlayerData in which card will be placed at coordinates (x, y)
     * @param x          X coordinate of playerData
     * @param y          Y coordinate of playerData
     * @return the amount of corners covered by the card when placing it
     */
    @Override
    public int checkBonus(PlayableCard card, PlayerData playerData, int x, int y) {
        return (int) Arrays.stream(playerData.getCardsArea().getNearCards(x, y))
                .filter(Objects::nonNull)
                .count();
    }

    /**
     * Returns the String representing this bonus in the TUI graphics
     *
     * @return default colored "C" String
     */
    @Override
    public String toStringTUI() {
        return "\u001B[0mC";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CoveredCornersBonus;
    }

    @Override
    public int hashCode() {
        return CoveredCornersBonus.class.hashCode();
    }
}
