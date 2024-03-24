package it.polimi.sw.gianpaolocugola50.model.card;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

/**
 * Utilized for cards whose score depends on the amount
 * of a certain resource present in the player's area
 * in the instant immediately after the placement
 */
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
