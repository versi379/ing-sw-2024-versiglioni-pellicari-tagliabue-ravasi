package it.polimi.sw.GC50.model.card;

import it.polimi.sw.GC50.model.game.CardsMatrixTest;
import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.*;

import static it.polimi.sw.GC50.model.card.CornerTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayableCardTest {
    public static final Corner[] corners = new Corner[]
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
        PlayerData playerData = new PlayerData(CardsMatrixTest.testCardsMatrix());

        assertFalse(redPlayableCard.isPlaceable(playerData, 100, 100));
    }

    @Test
    void testIsPlaceablePositive() {
        PlayerData playerData = new PlayerData(CardsMatrixTest.testCardsMatrix());
        playerData.placeCard(whitePlayableCard, 2, 2);

        assertTrue(redPlayableCard.isPlaceable(playerData, 3, 1));
    }

    @Test
    void testScoreIncrement() {
        PlayerData playerData = new PlayerData(2);
        playerData.placeCard(whitePlayableCard, 2, 2);
        PlayableCard card = new PlayableCard(Color.RED, 2, new CoveredCornersBonus(), new ArrayList<>(), corners);

        assertEquals(2, card.scoreIncrement(playerData, 3, 3));
    }

    @Test
    void testEqualsFalse() {
        Corner[] testCorners = corners.clone();
        testCorners[0] = fungiCorner;
        PlayableCard card1 = new PlayableCard(Color.RED, 1, new ResourcesBonus(Resource.INK), new ArrayList<>(), testCorners);
        PlayableCard card2 = new PlayableCard(Color.RED, 1, new ResourcesBonus(Resource.INK), new ArrayList<>(), corners);
        PlayableCard card3 = new PlayableCard(Color.RED, 1, new ArrayList<>(), testCorners);

        assertFalse(card1.equals(card2));
        assertNotEquals(card1.hashCode(), card2.hashCode());
        assertFalse(card1.equals(card3));
        assertNotEquals(card1.hashCode(), card3.hashCode());
        assertFalse(card1.equals(redPlayableCard));
        assertNotEquals(card1.hashCode(), redPlayableCard.hashCode());
    }

    @Test
    void testEqualsTrue() {
        Corner[] testCorners = corners.clone();
        testCorners[0] = fungiCorner;
        PlayableCard card1 = new PlayableCard(Color.RED, 1, new BlankBonus(), new ArrayList<>(), testCorners);
        PlayableCard card2 = new PlayableCard(Color.RED, 1, testCorners);

        assertTrue(card1.equals(card2));
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void toStringTui() {
        new PlayableCard(Color.RED, 1, new BlankBonus(), new ArrayList<>(), corners).toStringTui();

         new PlayableCard(Color.BLUE, 1, new ResourcesBonus(Resource.INK), new ArrayList<>(), corners).toStringTui();
        ;

    }
}
