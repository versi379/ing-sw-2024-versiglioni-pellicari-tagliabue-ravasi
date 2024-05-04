package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.io.Serializable;
import java.util.*;

/**
 * Specific type of playable card, characterized by specific rules
 */
public class GoldCard extends PlayableCard implements Serializable {

    /**
     * Represents the constraint regarding the amount of resources
     * needed in the player's area in order to place the card
     */
    private final Map<Resource, Integer> constraint;

    public GoldCard(Color color, int points, Bonus bonus, List<Resource> fixedResources, Corner[] corners, List<Resource> constraint) {
        super(color, points, bonus, fixedResources, corners);

        this.constraint = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            this.constraint.put(resource, 0);
        }
        if (constraint != null) {
            for (Resource resource : constraint) {
                this.constraint.replace(resource, (this.constraint.get(resource)) + 1);
            }
        }
    }

    public List<Resource> getConstraintList() {
        List<Resource> constraintList = new ArrayList<>();
        for (Map.Entry<Resource, Integer> entry : constraint.entrySet()) {
            Resource resource = entry.getKey();
            for (int i = entry.getValue(); i > 0; i--) {
                constraintList.add(resource);
            }
        }
        return constraintList;
    }

    @Override
    public boolean isPlaceable(PlayerData board, int x, int y) {
        return board.isPositionValid(x, y) &&
                this.checkConstraint(board);
    }

    public boolean checkConstraint(PlayerData playerData) {
        return constraint.keySet().stream()
                .noneMatch(x -> constraint.get(x) > playerData.numOfResource(x));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GoldCard goldCard)) {
            return false;
        }
        return getColor().equals(goldCard.getColor()) &&
                getPoints() == goldCard.getPoints() &&
                getBonus().equals(goldCard.getBonus()) &&
                getFixedResources().equals(goldCard.getFixedResources()) &&
                getCorners().equals(goldCard.getCorners()) &&
                getConstraintList().equals(goldCard.getConstraintList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getPoints(), getBonus(), getFixedResources(), getCorners(), getConstraintList());
    }
}
