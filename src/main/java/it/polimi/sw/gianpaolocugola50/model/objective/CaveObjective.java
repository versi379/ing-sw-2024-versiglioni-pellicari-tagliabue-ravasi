package it.polimi.sw.gianpaolocugola50.model.objective;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;
import it.polimi.sw.gianpaolocugola50.model.card.Color;

public class CaveObjective implements Objective {
    private final Color targetColor1;
    private final Color targetColor2;
    private final CaveOrientation orientation;

    public CaveObjective(Color targetColor1, Color targetColor2, CaveOrientation orientation) {
        this.targetColor1 = targetColor1;
        this.targetColor2 = targetColor2;
        this.orientation = orientation;
    }

    @Override
    public int checkCondition(PlayerData playerData) {
        return 0;
    }
}
