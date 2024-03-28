package it.polimi.sw.gianpaolocugola50.model.card;

import java.util.*;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

/**
 * Utilized for cards whose score depends on the amount of corners is covered by them
 */
public class CoveredCornersBonus implements Bonus {
    @Override
    public int checkBonus(PlayableCard card, PlayerData playerData, int x, int y) {
        return (int) Arrays.stream(playerData.getCardsArea().getNearCards(x, y))
                .filter(Objects::nonNull)
                .count();
    }
}
