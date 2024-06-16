package it.polimi.sw.GC50.model.cards;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;

/**
 * Represents the bonus for cards without a specific one
 */
public class BlankBonus implements Bonus, Serializable {

    /**
     * Constructs a new BlankBonus instance
     */
    public BlankBonus() {
    }

    /**
     * Returns the bonus multiplier by which the card score is calculated when placed
     *
     * @param card          PlayableCard associated with this bonus
     * @param playerData    PlayerData in which card will be placed
     * @param x             X coordinate of playerData
     * @param y             Y coordinate of playerData
     * @return 1
     */
    @Override
    public int checkBonus(PlayableCard card, PlayerData playerData, int x, int y) {
        return 1;
    }

    /**
     * Returns the String representing this bonus in the TUI graphics
     *
     * @return space character String
     */
    @Override
    public String toStringTUI() {
        return " ";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlankBonus;
    }

    @Override
    public int hashCode() {
        return BlankBonus.class.hashCode();
    }
}
