package it.polimi.sw.GC50.model.objectives;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;

/**
 * Interface for different objectives management
 */
public interface Objective extends Serializable {
    int checkCondition(PlayerData playerData);

    String toStringTUI();
}
