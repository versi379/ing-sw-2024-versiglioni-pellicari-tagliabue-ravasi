package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.CornerPointer;
import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the bonus utilized for cards whose score depends on the amount of a certain resource
 * present in the player's area in the instant immediately following its placement
 */
public class ResourcesBonus implements Bonus , Serializable {

    private final Resource targetResource;

    /**
     * Constructs a new ResourceBonus instance given the target resource
     *
     * @param targetResource        target resource of this bonus computation
     */
    public ResourcesBonus(Resource targetResource) {
        this.targetResource = targetResource;
    }

    /**
     * Returns this bonus's target resource
     *
     * @return the target resource
     */
    public Resource getTargetResource() {
        return targetResource;
    }

    /**
     * Computes the bonus multiplier by which the card score is calculated when placed
     *
     * @param card          PlayableCard associated with this bonus
     * @param playerData    PlayerData in which card will be placed at coordinates (x, y)
     * @param x             X coordinate of playerData
     * @param y             Y coordinate of playerData
     * @return the amount of target resource present in playerData after placing the card
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

    /**
     * Returns the String representing this bonus in the TUI graphics
     *
     * @return target resource's TUI String
     */
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
