package it.polimi.sw.gianpaolocugola50.model.card;

import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

import java.util.*;

public class GoldCard extends PlayableCard {
    private final Map<Resource, Integer> constraint;

    public GoldCard(Color color, int points, Bonus bonus, List<Resource> fixedResources, Corner[] corners, List<Resource> constraint) {
        super(color, points, bonus, fixedResources, corners);
        if (constraint != null) {
            this.constraint = new EnumMap<>(Resource.class);
            for (Resource resource : Resource.values()) {
                this.constraint.put(resource, 0);
            }
            for (Resource resource : constraint) {
                this.constraint.replace(resource, (this.constraint.get(resource)) + 1);
            }
        } else {
            this.constraint = new EnumMap<>(Resource.class);
        }
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
}
