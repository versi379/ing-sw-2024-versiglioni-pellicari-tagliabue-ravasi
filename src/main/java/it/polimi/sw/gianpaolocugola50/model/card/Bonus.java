package it.polimi.sw.gianpaolocugola50.model.card;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

@FunctionalInterface
public interface Bonus {
    int checkBonus(PlayerData playerData, int x, int y);
}