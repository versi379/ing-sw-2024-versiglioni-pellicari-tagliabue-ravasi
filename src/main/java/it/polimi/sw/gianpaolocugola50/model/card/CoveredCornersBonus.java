package it.polimi.sw.gianpaolocugola50.model.card;

import java.util.*;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

public class CoveredCornersBonus implements Bonus {
    @Override
    public int checkBonus(PlayerData playerData, int x, int y) {
        return (int) Arrays.stream(playerData.getCardsArea().getNearCards(x, y))
                .filter(Objects::nonNull)
                .count();
    }
}
