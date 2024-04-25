package it.polimi.sw.GC50.model.card;

import org.junit.jupiter.api.Test;

import java.util.*;

import static it.polimi.sw.GC50.model.card.CornerTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayableCardTest {
    static final Corner[] corners = new Corner[]
            {CornerTest.emptyCorner, CornerTest.emptyCorner, CornerTest.emptyCorner, CornerTest.emptyCorner};
    public static final PlayableCard whitePlayableCard = new PlayableCard(Color.WHITE, 0, corners);
    public static final PlayableCard greenPlayableCard = new PlayableCard(Color.GREEN, 1, corners);
    public static final PlayableCard bluePlayableCard = new PlayableCard(Color.BLUE, 1, corners);
    public static final PlayableCard redPlayableCard = new PlayableCard(Color.RED, 1, corners);
    public static final PlayableCard purplePlayableCard = new PlayableCard(Color.PURPLE, 1, corners);

    @Test
    void testPlayableCardFullConstructor() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);

        assertEquals(Color.BLUE, card.getColor());
        assertEquals(3, card.getPoints());
        assertEquals(bonus, card.getBonus());
        assertEquals(fixedResources, card.getFixedResources());
    }

    @Test
    void testPlayableCardNoBonusConstructor() {
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.PLANT, Resource.FUNGI));
        PlayableCard card = new PlayableCard(Color.GREEN, 2, fixedResources, corners);

        assertEquals(Color.GREEN, card.getColor());
        assertEquals(2, card.getPoints());
        assertInstanceOf(BlankBonus.class, card.getBonus());
        assertEquals(fixedResources, card.getFixedResources());
    }

    @Test
    void testPlayableCardNoBonusCentralResourcesConstructor() {
        PlayableCard card = new PlayableCard(Color.RED, 1, corners);

        assertEquals(Color.RED, card.getColor());
        assertEquals(1, card.getPoints());
        assertEquals(new BlankBonus(), card.getBonus());
        assertEquals(new ArrayList<>(), card.getFixedResources());
    }

    @Test
    void testGetColor() {
        assertEquals(Color.PURPLE, purplePlayableCard.getColor());
    }

    @Test
    void testGetPoints() {
        assertEquals(1, greenPlayableCard.getPoints());
    }

    @Test
    void testGetBonus() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);

        assertEquals(bonus, card.getBonus());
    }

    @Test
    void testGetFixedResourcesFull() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);

        assertEquals(fixedResources, card.getFixedResources());
    }

    @Test
    void testGetFixedResourcesEmpty() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>();
        PlayableCard card = new PlayableCard(Color.BLUE, 3, bonus, fixedResources, corners);

        assertEquals(fixedResources, card.getFixedResources());
    }

    @Test
    void testGetSwCorner() {
        Corner[] testCorners = corners;
        Corner testCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
        testCorners[0] = testCorner;
        PlayableCard card = new PlayableCard(Color.RED, 3, testCorners);

        assertEquals(testCorner, card.getSwCorner());
    }

    @Test
    void testGetNwCorner() {
        Corner[] testCorners = corners;
        Corner testCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
        testCorners[1] = testCorner;
        PlayableCard card = new PlayableCard(Color.RED, 3, testCorners);

        assertEquals(testCorner, card.getNwCorner());
    }

    @Test
    void testGetNeCorner() {
        Corner[] testCorners = corners;
        Corner testCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
        testCorners[2] = testCorner;
        PlayableCard card = new PlayableCard(Color.RED, 3, testCorners);

        assertEquals(testCorner, card.getNeCorner());
    }

    @Test
    void testGetSeCorner() {
        Corner[] testCorners = corners;
        Corner testCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
        testCorners[3] = testCorner;
        PlayableCard card = new PlayableCard(Color.RED, 3, testCorners);

        assertEquals(testCorner, card.getSeCorner());
    }

    @Test
    void testResourceCountZero() {
        PlayableCard card = new PlayableCard(Color.RED, 3,
                new Corner[]{hiddenCorner, emptyCorner, plantCorner, fungiCorner});

        assertEquals(0, card.resourceCount(Resource.ANIMAL));
    }

    @Test
    void testResourceCountPositive() {
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.PLANT, Resource.FUNGI));
        PlayableCard card = new PlayableCard(Color.RED, 3, fixedResources,
                new Corner[]{hiddenCorner, emptyCorner, inkCorner, fungiCorner});

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
        assertEquals(1, greenPlayableCard.scoreIncrement(null, 0, 0));
    }
}
