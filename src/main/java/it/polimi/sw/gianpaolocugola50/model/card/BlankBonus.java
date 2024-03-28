package it.polimi.sw.gianpaolocugola50.model.card;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

/**
 * Utilized for cards without a specific bonus
 */
public class BlankBonus implements Bonus {
    public BlankBonus() {
    }

    @Override
    public int checkBonus(PlayableCard card, PlayerData playerData, int x, int y) {
        return 1;
    }
}
