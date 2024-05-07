package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Utilized for cards whose score depends on the amount of corners covered by them
 */
public class CoveredCornersBonus implements Bonus , Serializable {

    /**
     * @param card
     * @param playerData
     * @param x
     * @param y
     * @return
     */
    @Override
    public int checkBonus(PlayableCard card, PlayerData playerData, int x, int y) {
        return (int) Arrays.stream(playerData.getCardsArea().getNearCards(x, y))
                .filter(Objects::nonNull)
                .count();
    }

    @Override
    public String toStringTUI() {
        return "\u001B[0mC";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CoveredCornersBonus;
    }

    @Override
    public int hashCode() {
        return CoveredCornersBonus.class.hashCode();
    }
}
