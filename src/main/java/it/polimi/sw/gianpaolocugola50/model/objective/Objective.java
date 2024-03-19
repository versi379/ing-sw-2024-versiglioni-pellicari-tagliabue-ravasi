package it.polimi.sw.gianpaolocugola50.model.objective;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

public interface Objective {
    public int checkCondition(PlayerData playerData);
}
