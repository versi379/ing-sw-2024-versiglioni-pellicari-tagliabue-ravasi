package it.polimi.sw.GC50.model.cards;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;

/**
 * Interface for different cards bonuses
 */
public interface Bonus extends Serializable {

    /**
     * Computes the bonus multiplier by which the card score is calculated when placed
     *
     * @param card          PlayableCard associated with this bonus
     * @param playerData    PlayerData in which card will be placed at coordinates (x, y)
     * @param x             X coordinate of playerData
     * @param y             Y coordinate of playerData
     * @return integer multiplier of the card when placed according to the given parameters
     */
    int checkBonus(PlayableCard card, PlayerData playerData, int x, int y);

    /**
     * Returns the String representing this bonus in the TUI graphics
     *
     * @return String representing this bonus (1 character long)
     */
    String toStringTUI();
}
