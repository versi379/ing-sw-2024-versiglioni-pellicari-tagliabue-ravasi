package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.util.*;

public class GoldCard extends PlayableCard {
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
        List<Resource> listConstraint = new ArrayList<>();
        for (Map.Entry<Resource, Integer> entry : constraint.entrySet()) {
            Resource resource = entry.getKey();
            int value = entry.getValue();
            for (int i = 0; i < value; i++) {
                listConstraint.add(resource);
            }
        }
        return listConstraint;
    }

    @Override
    public boolean isPlaceable(PlayerData board, int x, int y) {
        return board.isPositionValid(x, y) &&
                this.checkConstraint(board);
    }

    /**
     * Checks if the player has enough resources to place this card
     *
     * @param playerData
     * @return
     */
    public boolean checkConstraint(PlayerData playerData) {
        return constraint.keySet().stream()
                .noneMatch(x -> constraint.get(x) > playerData.numOfResource(x));
    }
}
