package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

/**
 * Utilized for cards without a specific bonus
 */
public class BlankBonus implements Bonus {

    public BlankBonus() {
    }

    /**
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

}
