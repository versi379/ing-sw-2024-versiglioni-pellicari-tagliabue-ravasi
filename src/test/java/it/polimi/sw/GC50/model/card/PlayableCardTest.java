package it.polimi.sw.GC50.model.card;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {

    @Test
    public void testPlayableCardFullConstructor() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);
        assertEquals(Color.BLUE, card.getColor());
        assertEquals(3, card.getPoints());
        assertEquals(bonus, card.getBonus());
        assertEquals(fixedResources, card.getFixedResources());
    }

    @Test
    public void testPlayableCardNoBonusConstructor() {
        Corner[] corners = new Corner[4];
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.PLANT, Resource.FUNGI));
        PlayableCard card = new PlayableCard(Color.GREEN, 2, corners, fixedResources);
        assertEquals(Color.GREEN, card.getColor());
        assertEquals(2, card.getPoints());
        assertEquals(new BlankBonus(), card.getBonus());
        assertEquals(fixedResources, card.getFixedResources());
    }

    @Test
    public void testPlayableCardNoBonusCentralResourcesConstructor() {
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.RED, 1, corners);
        assertEquals(Color.RED, card.getColor());
        assertEquals(1, card.getPoints());
        assertEquals(new BlankBonus(), card.getBonus());
        assertEquals(new ArrayList<>(), card.getFixedResources());
    }

    @Test
    void getColor() {
    }

    @Test
    void getPoints() {
    }

    @Test
    void getBonus() {
    }

    @Test
    void getCorners() {
    }

    @Test
    void getFixedResources() {
    }

    @Test
    void getSwCorner() {
    }

    @Test
    void getNwCorner() {
    }

    @Test
    void getNeCorner() {
    }

    @Test
    void getSeCorner() {
    }

    @Test
    void resourceCount() {
    }

    @Test
    void isPlaceable() {
    }

    @Test
    void scoreIncrement() {
    }
}