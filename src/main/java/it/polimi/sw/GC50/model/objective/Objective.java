package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.game.PlayerData;

@FunctionalInterface
public interface Objective {
    public int checkCondition(PlayerData playerData);
}
