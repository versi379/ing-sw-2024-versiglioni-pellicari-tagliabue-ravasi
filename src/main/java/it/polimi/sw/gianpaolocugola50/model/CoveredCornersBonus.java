package it.polimi.sw.gianpaolocugola50.model;

public class CoveredCornersBonus implements Bonus {
    @Override
    public int checkBonus(PlayerData playerData, int coveredCorners) {
        return coveredCorners;
    }
}