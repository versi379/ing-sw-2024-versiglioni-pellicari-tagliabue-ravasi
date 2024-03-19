package it.polimi.sw.gianpaolocugola50.model.card;
import it.polimi.sw.gianpaolocugola50.model.game.PlayerData;

import java.util.*;
import static java.util.stream.Stream.concat;

public class PlayableCard {
    private final Color color;
    private final int points;
    private final Bonus bonus;
    private final List<Resource> fixedResources;
    private final Corner[] corners;

    public PlayableCard(Color color, int points, Bonus bonus, List<Resource> fixedResources, Corner[] corners) {
        this.color = color;
        this.points = points;
        this.bonus = bonus;
        this.fixedResources = new ArrayList<>(fixedResources);
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
    public List<Resource> getFixedResources() {
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
                .filter(x -> x.equals(targetResource))
                .count();
    }
    public boolean isPlaceable(PlayerData board, int x, int y) {
        return board.isPositionValid(x, y);
    }
    public int scoreIncrement(PlayerData board, int coveredCorners) {
        return points*bonus.checkBonus(board, coveredCorners);
    }
}
