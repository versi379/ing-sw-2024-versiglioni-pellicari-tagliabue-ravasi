package it.polimi.sw.gianpaolocugola50.model;

public class ResourcesBonus implements Bonus{
    private final Resource targetResource;

    @Override
    public int checkBonus(PlayerData playerData, int coveredCorners) {
        return playerData.numOfResource(targetResource);
    }
}
