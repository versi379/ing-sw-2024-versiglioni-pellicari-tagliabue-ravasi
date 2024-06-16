package it.polimi.sw.GC50.model.cards;

import it.polimi.sw.GC50.model.game.PlayerData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.sw.GC50.model.cards.CornerTest.fungiCorner;
import static it.polimi.sw.GC50.model.cards.PlayableCardTest.corners;
import static org.junit.jupiter.api.Assertions.*;

public class GoldCardTest {

    @Test
    void testGoldCardConstructor() {
        Bonus bonus = new ResourcesBonus(Resource.ANIMAL);
        List<Resource> fixedResources = new ArrayList<>(Arrays.asList(Resource.ANIMAL, Resource.PLANT));
        List<Resource> constraint = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.PLANT));
        GoldCard card = new GoldCard(Color.BLUE, 3, bonus, fixedResources, corners, constraint);

        assertEquals(Color.BLUE, card.getColor());
        assertEquals(3, card.getPoints());
        assertEquals(bonus, card.getBonus());
        assertEquals(fixedResources, card.getFixedResources());
        assertTrue(constraint.containsAll(card.getConstraintList()));
        assertTrue(card.getConstraintList().containsAll(constraint));
    }

    @Test
    void testIsPlaceableInvalidPosition() {
        PlayerData playerData = new PlayerData(2);
        List<Resource> resources = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.PLANT));
        GoldCard goldCard = new GoldCard(Color.RED, 3, new BlankBonus(), new ArrayList<>(), corners, resources);

        assertFalse(goldCard.isPlaceable(playerData, 100, 100));
    }

    @Test
    void testIsPlaceableConstraintNotMeet() {
        PlayerData playerData = new PlayerData(2);
        List<Resource> resources = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.PLANT));
        PlayableCard resourceCard = new PlayableCard(Color.WHITE, 0, corners);
        playerData.placeCard(resourceCard, 2, 2);
        GoldCard goldCard = new GoldCard(Color.RED, 3, new BlankBonus(), new ArrayList<>(), corners, resources);

        assertFalse(goldCard.isPlaceable(playerData, 3, 3));
    }

    @Test
    void testIsPlaceableTrue() {
        PlayerData playerData = new PlayerData(2);
        List<Resource> resources = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.PLANT));
        PlayableCard resourceCard = new PlayableCard(Color.WHITE, 0, resources, corners);
        playerData.placeCard(resourceCard, 2, 2);
        GoldCard goldCard = new GoldCard(Color.RED, 3, new BlankBonus(), new ArrayList<>(), corners, resources);

        assertTrue(goldCard.isPlaceable(playerData, 3, 3));
    }

    @Test
    void testCheckConstraintFalse() {
        PlayerData playerData = new PlayerData(2);
        List<Resource> resources = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.PLANT));
        GoldCard goldCard = new GoldCard(Color.RED, 3, new BlankBonus(), new ArrayList<>(), corners, resources);

        assertFalse(goldCard.checkConstraint(playerData));
    }

    @Test
    void testCheckConstraintTrue() {
        PlayerData playerData = new PlayerData(2);
        List<Resource> resources = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.PLANT));
        PlayableCard resourceCard = new PlayableCard(Color.WHITE, 0, resources, corners);
        playerData.placeCard(resourceCard, 2, 2);
        GoldCard goldCard = new GoldCard(Color.RED, 3, new BlankBonus(), new ArrayList<>(), corners, resources);

        assertTrue(goldCard.checkConstraint(playerData));
    }

    @Test
    void testEqualsFalse() {
        Corner[] testCorners = corners.clone();
        testCorners[0] = fungiCorner;
        List<Resource> constraint1 = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.PLANT));
        List<Resource> constraint2 = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.PLANT));
        GoldCard card1 = new GoldCard(Color.RED, 1, new ResourcesBonus(Resource.INK), new ArrayList<>(), testCorners, constraint1);
        GoldCard card2 = new GoldCard(Color.RED, 1, new ResourcesBonus(Resource.INK), new ArrayList<>(), testCorners, constraint2);

        assertFalse(card1.equals(card2));
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void testEqualsTrue() {
        Corner[] testCorners = corners.clone();
        testCorners[0] = fungiCorner;
        List<Resource> constraint1 = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.PLANT));
        List<Resource> constraint2 = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.PLANT, Resource.FUNGI));
        GoldCard card1 = new GoldCard(Color.RED, 1, new ResourcesBonus(Resource.INK), new ArrayList<>(), testCorners, constraint1);
        PlayableCard card2 = new GoldCard(Color.RED, 1, new ResourcesBonus(Resource.INK), new ArrayList<>(), testCorners, constraint1);
        GoldCard card3 = new GoldCard(Color.RED, 1, new ResourcesBonus(Resource.INK), new ArrayList<>(), testCorners, constraint2);

        assertTrue(card1.equals(card2));
        assertEquals(card1.hashCode(), card2.hashCode());
        assertTrue(card1.equals(card3));
        assertEquals(card1.hashCode(), card3.hashCode());
    }
}
