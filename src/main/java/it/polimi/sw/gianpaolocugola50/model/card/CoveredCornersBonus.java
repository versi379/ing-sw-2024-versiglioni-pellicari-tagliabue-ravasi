package it.polimi.sw.gianpaolocugola50.model.card;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

public class CoveredCornersBonus implements Bonus {
    @Override
    public int checkBonus(PlayerData playerData, int coveredCorners) {
        return coveredCorners;
    }
}
