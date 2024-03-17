package it.polimi.sw.gianpaolocugola50.model;

public class BlankBonus implements Bonus{
    @Override
    public int checkBonus(PlayerData playerData, int coveredCorners) {
        return 1;
    }
}
