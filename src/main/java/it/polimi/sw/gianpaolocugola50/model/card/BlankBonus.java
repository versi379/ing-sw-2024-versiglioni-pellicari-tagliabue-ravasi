package it.polimi.sw.gianpaolocugola50.model.card;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

public class BlankBonus implements Bonus {
    @Override
    public int checkBonus(PlayerData playerData, int coveredCorners) {
        return 1;
    }
}
