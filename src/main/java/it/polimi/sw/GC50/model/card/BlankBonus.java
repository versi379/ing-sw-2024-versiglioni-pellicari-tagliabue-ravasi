package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;

/**
 * Utilized for cards without a specific bonus
 */
public class BlankBonus implements Bonus, Serializable {

    public BlankBonus() {
    }

    /**
     * Return bonus points associated with these kind of cards (1)
     *
     * @param card
     * @param playerData
     * @param x
     * @param y
     * @return
     */
    @Override
    public int checkBonus(PlayableCard card, PlayerData playerData, int x, int y) {
        return 1;
    }

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
