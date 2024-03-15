package it.polimi.sw.gianpaolocugola50.model;
import java.util.*;

public class GoldCard extends PlayableCard {
    private final Map<Resource, Integer> constraint;
    private final Bonus bonus;

    public GoldCard(Color color, int score, List<Resource> fixedResources, List<Corner> corners, Map<Resource, Integer> constraint, Bonus bonus) {
        super(color, score, fixedResources, corners);
        this.constraint = new HashMap<>(constraint);
        this.bonus = bonus;
    }

    public Map<Resource, Integer> getConstraint() {
        return new HashMap<>(constraint);
    }

    public Bonus getBonus() {
        return bonus;
    }
}
