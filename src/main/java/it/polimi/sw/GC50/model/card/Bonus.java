package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

@FunctionalInterface
public interface Bonus {

    /**
     *
     * @param card
     * @param playerData
     * @param x
     * @param y
     * @return
     */
    int checkBonus(PlayableCard card, PlayerData playerData, int x, int y);

}
