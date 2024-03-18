package it.polimi.sw.gianpaolocugola50.model;
import java.util.*;

public class GoldCard extends PlayableCard {
    private final Map<Resource, Integer> constraint;

    public GoldCard(Color color, int points, Bonus bonus, List<Resource> fixedResources, Corner[] corners, Map<Resource, Integer> constraint) {
        super(color, points, bonus, fixedResources, corners);
        this.constraint = new EnumMap<>(constraint);
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
