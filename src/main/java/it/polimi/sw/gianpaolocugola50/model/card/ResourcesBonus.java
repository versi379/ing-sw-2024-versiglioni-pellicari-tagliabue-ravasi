package it.polimi.sw.gianpaolocugola50.model.card;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

public class ResourcesBonus implements Bonus {
    private final Resource targetResource;

    public ResourcesBonus(Resource targetResource) {
        this.targetResource = targetResource;
    }

    @Override
    public int checkBonus(PlayerData playerData, int x, int y) {
        return playerData.numOfResource(targetResource);
    }
}
