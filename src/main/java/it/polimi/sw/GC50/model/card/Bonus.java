package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;

/**
 * Interface for different bonus management
 */
@FunctionalInterface
public interface Bonus extends Serializable {

    /**
     * Abstract method to compute bonus points
     *
     * @param card
     * @param playerData
     * @param x
     * @param y
     * @return
     */
    int checkBonus(PlayableCard card, PlayerData playerData, int x, int y);
}
