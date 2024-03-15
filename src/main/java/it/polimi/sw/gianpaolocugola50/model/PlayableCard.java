package it.polimi.sw.gianpaolocugola50.model;
import java.util.*;
import static java.util.stream.Stream.concat;

public class PlayableCard {
    private final Color color;
    private final int score;
    private final List<Resource> fixedResources;
    private final List<Corner> corners;

    public PlayableCard(Color color, int score, List<Resource> fixedResources, List<Corner> corners) {
        this.color = color;
        this.score = score;
        this.fixedResources = new ArrayList<>(fixedResources);
        this.corners = new ArrayList<>(corners);
    }
    public Color getColor() {
        return color;
    }
    public int getScore() {
        return score;
    }
    public List<Resource> getFixedResources() {
        return new ArrayList<>(fixedResources);
    }
    public Corner getSwCorner() {
        return corners.get(0);
    }
    public Corner getNwCorner() {
        return corners.get(1);
    }
    public Corner getNeCorner() {
        return corners.get(2);
    }
    public Corner getSeCorner() {
        return corners.get(3);
    }
    public int resourceCount(Resource targetResource) {
        return (int)concat(corners.stream().map(Corner::getResource), fixedResources.stream())
                .filter(x -> x.equals(targetResource))
                .count();
    }
}
