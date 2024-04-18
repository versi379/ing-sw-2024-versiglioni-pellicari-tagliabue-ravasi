package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.PlayerData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Stream.concat;

/**
 * Represents either front or back of a game card
 */
public class PlayableCard {

    private final Color color;

    /**
     * Represents the number at the top of the card
     * (not necessarily the score obtained by the player when placing it)
     */
    private final int points;

    /**
     * Represents the condition regarding the player's score
     * increment obtained when placing the card,
     * computed as points * bonus.checkBonus(...)
     */
    private final Bonus bonus;

    /**
     * Represents the list of resources present in the center of the card
     */
    private final List<Resource> fixedResources;

    /**
     * Represents the corners of the card (always initialized as an array of length 4)
     * In order, it contains the SW, NW, NE and SE corners
     */
    private final Corner[] corners;

    public PlayableCard(Color color, int points, Bonus bonus, List<Resource> fixedResources, Corner[] corners) {
        this.color = color;
        this.points = points;
        this.bonus = bonus;
        this.fixedResources = fixedResources;
        this.corners = corners.clone();
    }

    /**
     * Constructor for cards without bonus
     */
    public PlayableCard(Color color, int points, Corner[] corners) {
        this.color = color;
        this.points = points;
        this.bonus = new BlankBonus();
        this.fixedResources = new ArrayList<>();
        this.corners = corners.clone();
    }

    /**
     * Constructor for cards without bonus and central resource
     */
    public PlayableCard(Color color, int points, Corner[] corners, List<Resource> fixedResources) {
        this.color = color;
        this.points = points;
        this.bonus = new BlankBonus();
        this.fixedResources = fixedResources;
        this.corners = corners.clone();
    }

    public Color getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public Corner[] getCorners() {
        return corners;
    }

    public List<Resource> getFixedResources() {
        if (fixedResources == null) {
            return null;
        }
        return new ArrayList<>(fixedResources);
    }

    public Corner getSwCorner() {
        return corners[0];
    }

    public Corner getNwCorner() {
        return corners[1];
    }

    public Corner getNeCorner() {
        return corners[2];
    }

    public Corner getSeCorner() {
        return corners[3];
    }

    public int resourceCount(Resource targetResource) {
        return (int) concat(Arrays.stream(corners).map(Corner::getResource), fixedResources.stream())
                .filter(targetResource::equals)
                .count();
    }

    public boolean isPlaceable(PlayerData board, int x, int y) {
        return board.isPositionValid(x, y);
    }

    public int scoreIncrement(PlayerData board, int x, int y) {
        return points * bonus.checkBonus(this, board, x, y);
    }

}
