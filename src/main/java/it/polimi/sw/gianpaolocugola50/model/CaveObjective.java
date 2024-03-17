package it.polimi.sw.gianpaolocugola50.model;

public class CaveObjective implements Objective {
    private final Color targetColor1;
    private final Color targetColor2;
    private final CaveOrientation orientation;

    @Override
    public int checkCondition(PlayerData playerData) {
        return 0;
    }
}
