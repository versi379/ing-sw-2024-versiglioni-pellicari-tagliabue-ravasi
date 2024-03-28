package it.polimi.sw.gianpaolocugola50.model.card;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

@FunctionalInterface
public interface Bonus {
    int checkBonus(PlayableCard card, PlayerData playerData, int x, int y);
}
