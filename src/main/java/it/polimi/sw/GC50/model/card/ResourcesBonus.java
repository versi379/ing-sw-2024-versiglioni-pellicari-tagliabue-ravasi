package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.CornerPointer;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.util.Arrays;

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

    public Resource getTargetResource() {
        return targetResource;
    }

    /**
     * @param card
     * @param playerData
     * @param x
     * @param y
     * @return
     */
    @Override
    public int checkBonus(PlayableCard card, PlayerData playerData, int x, int y) {
        return playerData.numOfResource(targetResource)
                - (int) Arrays.stream(playerData.getTargetCorners(x, y))
                .map(CornerPointer::getCorner)
                .map(Corner::getResource)
                .filter(targetResource::equals)
                .count()
                + card.resourceCount(targetResource);
    }

}