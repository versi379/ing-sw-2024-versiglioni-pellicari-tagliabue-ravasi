package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.CornerPointer;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Utilized for cards whose score depends on the amount
 * of a certain resource present in the player's area
 * in the instant immediately after the placement
 */
public class ResourcesBonus implements Bonus , Serializable {

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
                .filter(CornerPointer::isPresent)
                .map(CornerPointer::getCorner)
                .filter(Corner::isFull)
                .map(Corner::getResource)
                .filter(targetResource::equals)
                .count()
                + card.resourceCount(targetResource);
    }

    @Override
    public String toStringTUI() {
        return getTargetResource().toStringTUI();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ResourcesBonus resourcesBonus)) {
            return false;
        }
        return getTargetResource().equals(resourcesBonus.getTargetResource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetResource());
    }
}
