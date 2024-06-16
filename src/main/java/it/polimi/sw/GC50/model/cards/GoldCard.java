package it.polimi.sw.GC50.model.cards;

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

    public GoldCard(String code, Color color, int points, Bonus bonus, List<Resource> fixedResources, Corner[] corners, List<Resource> constraint) {
        super(code, color, points, bonus, fixedResources, corners);

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

    /**
     * constructs a new Gold Card instance
     *
     * @param color          specify the color
     * @param points         specify the points associated
     * @param bonus          specify the bonus associated
     * @param fixedResources specify fixed resources associated
     * @param corners        specify the corners
     * @param constraint     specify constraints of gold card
     */
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

    /**
     * Specifies List of constraints required for play a gold card
     * @return  an arrayList of constraints
     */
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
    /**
     * Verify if the position selected is valid
     * @param board     identify game board
     * @param x         X coordinate of playerData
     * @param y         Y coordinate of playerData
     * @return          boolean (true if it is valid)
     */
    @Override
    public boolean isPlaceable(PlayerData board, int x, int y) {
        return board.isPositionValid(x, y) &&
                this.checkConstraint(board);
    }

    /**
     * Verify if constraints are respected
     * @param playerData PlayerData in which card will be placed
     * @return a boolean (true if it is valid)
     */
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
    /**
     * Returns the String representing this card in the TUI graphics
     *
     * @return space character String
     */
    @Override
    public String[][] toStringTUI() {
        String[][] card = super.toStringTUI();

        // BOTTOM
        String constraint = "";
        int emptySpaces = 7;
        for (Resource resource : getConstraintList()) {
            constraint += resource.toStringTUI();
            emptySpaces--;
        }
        String leftPadding = "";
        String rightPadding = "";
        for (int i = 0; i < emptySpaces; i++) {
            if (i % 2 == 0) {
                rightPadding += " ";
            } else {
                leftPadding += " ";
            }
        }
        card[1][1] = leftPadding + constraint + rightPadding;

        return card;
    }
}
