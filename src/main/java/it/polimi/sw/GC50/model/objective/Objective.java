package it.polimi.sw.GC50.model.objective;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;

/**
 * Interface for different objectives management
 */
@FunctionalInterface
public interface Objective extends Serializable {
    public int checkCondition(PlayerData playerData);
}
