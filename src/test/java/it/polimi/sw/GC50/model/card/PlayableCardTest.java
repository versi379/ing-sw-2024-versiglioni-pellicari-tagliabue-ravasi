package it.polimi.sw.GC50.model.card;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {

    @Test
    void testPlayableCardFullConstructor() {
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
    void testPlayableCardNoBonusConstructor() {
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.PLANT, Resource.FUNGI));
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.GREEN, 2, fixedResources, corners);
        assertEquals(Color.GREEN, card.getColor());
        assertEquals(2, card.getPoints());
        assertInstanceOf(BlankBonus.class, card.getBonus());
        assertEquals(fixedResources, card.getFixedResources());
    }

    @Test
    void testPlayableCardNoBonusCentralResourcesConstructor() {
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.RED, 1, corners);
        assertEquals(Color.RED, card.getColor());
        assertEquals(1, card.getPoints());
        assertInstanceOf(BlankBonus.class, card.getBonus());
        assertEquals(new ArrayList<>(), card.getFixedResources());
    }

    @Test
    void testGetColor() {
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.PURPLE, 0, corners);
        assertEquals(Color.PURPLE, card.getColor());
    }

    @Test
    void testGetPoints() {
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.PURPLE, 3, corners);
        assertEquals(3, card.getPoints());
    }

    @Test
    void testGetBonus() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);
        assertEquals(bonus, card.getBonus());
    }

    @Test
    void testGetFixedResourcesFull() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);
        assertEquals(fixedResources, card.getFixedResources());
    }
    @Test
    void testGetFixedResourcesEmpty() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>();
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);
        assertEquals(fixedResources, card.getFixedResources());
    }

    @Test
    void testGetSwCorner() {
        Corner[] corners = new Corner[4];
        Corner testCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
        corners[0] = testCorner;
        PlayableCard card = new PlayableCard(Color.RED, 3, corners);
        assertEquals(testCorner, card.getSwCorner());
    }

    @Test
    void testGetNwCorner() {
        Corner[] corners = new Corner[4];
        Corner testCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
        corners[1] = testCorner;
        PlayableCard card = new PlayableCard(Color.RED, 3, corners);
        assertEquals(testCorner, card.getNwCorner());
    }

    @Test
    void testGetNeCorner() {
        Corner[] corners = new Corner[4];
        Corner testCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
        corners[2] = testCorner;
        PlayableCard card = new PlayableCard(Color.RED, 3, corners);
        assertEquals(testCorner, card.getNeCorner());
    }

    @Test
    void testGetSeCorner() {
        Corner[] corners = new Corner[4];
        Corner testCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
        corners[3] = testCorner;
        PlayableCard card = new PlayableCard(Color.RED, 3, corners);
        assertEquals(testCorner, card.getSeCorner());
    }

    @Test
    void testResourceCountZero() {
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CornerStatus.HIDDEN, null);
        corners[1] = new Corner(CornerStatus.EMPTY, null);
        corners[2] = new Corner(CornerStatus.FULL, Resource.PLANT);
        corners[3] = new Corner(CornerStatus.FULL, Resource.FUNGI);
        PlayableCard card = new PlayableCard(Color.RED, 3, corners);
        assertEquals(0, card.resourceCount(Resource.ANIMAL));
    }
    @Test
    void testResourceCountPositive() {
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.PLANT, Resource.FUNGI));
        Corner[] corners = new Corner[4];
        corners[0] = new Corner(CornerStatus.HIDDEN, null);
        corners[1] = new Corner(CornerStatus.EMPTY, null);
        corners[2] = new Corner(CornerStatus.FULL, Resource.INK);
        corners[3] = new Corner(CornerStatus.FULL, Resource.FUNGI);
        PlayableCard card = new PlayableCard(Color.RED, 3, fixedResources, corners);
        assertEquals(2, card.resourceCount(Resource.FUNGI));
    }

    @Test
    void testIsPlaceableNegative() {
    }
    @Test
    void testIsPlaceablePositive() {
    }

    @Test
    void testScoreIncrement() {
        Corner[] corners = new Corner[4];
        PlayableCard card = new PlayableCard(Color.BLUE, 3, corners);
        assertEquals(3, card.scoreIncrement(null, 0, 0));
    }
}