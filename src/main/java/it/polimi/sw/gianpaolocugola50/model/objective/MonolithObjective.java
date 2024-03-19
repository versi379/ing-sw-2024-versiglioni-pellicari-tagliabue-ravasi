package it.polimi.sw.gianpaolocugola50.model.objective;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;
import it.polimi.sw.gianpaolocugola50.model.card.Color;

public class MonolithObjective implements Objective {
    private final Color targetColor;
    private final MonolithOrientation orientation;
    public MonolithObjective(Color targetColor, MonolithOrientation orientation) {
        this.targetColor = targetColor;
        this.orientation = orientation;
    }

    @Override
    public int checkCondition(PlayerData playerData) {
        return 0;
    }
}
