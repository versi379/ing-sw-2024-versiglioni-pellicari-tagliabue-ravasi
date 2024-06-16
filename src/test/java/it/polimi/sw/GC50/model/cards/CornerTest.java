package it.polimi.sw.GC50.model.cards;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CornerTest {
    public static final Corner hiddenCorner = new Corner(CornerStatus.HIDDEN, null);
    public static final Corner emptyCorner = new Corner(CornerStatus.EMPTY, null);
    public static final Corner plantCorner = new Corner(CornerStatus.FULL, Resource.PLANT);
    public static final Corner animalCorner = new Corner(CornerStatus.FULL, Resource.ANIMAL);
    public static final Corner fungiCorner = new Corner(CornerStatus.FULL, Resource.FUNGI);
    public static final Corner insectCorner = new Corner(CornerStatus.FULL, Resource.INSECT);
    public static final Corner inkCorner = new Corner(CornerStatus.FULL, Resource.INK);

    @Test
    void testCornerConstructorHidden() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);

        assertEquals(CornerStatus.HIDDEN, corner.getStatus());
        assertNull(corner.getResource());
    }

    @Test
    void testCornerConstructorEmpty() {
        Corner corner = new Corner(CornerStatus.EMPTY, null);

        assertEquals(CornerStatus.EMPTY, corner.getStatus());
        assertNull(corner.getResource());
    }

    @Test
    void testCornerConstructorFull() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);

        assertEquals(CornerStatus.FULL, corner.getStatus());
        assertEquals(Resource.ANIMAL, corner.getResource());
    }

    @Test
    void testIsVisibleHidden() {
        assertFalse(hiddenCorner.isVisible());
    }

    @Test
    void testIsVisibleEmpty() {
        assertTrue(emptyCorner.isVisible());
    }

    @Test
    void testIsVisibleFull() {
        assertTrue(animalCorner.isVisible());
    }

    @Test
    void testIsFullHidden() {
        assertFalse(hiddenCorner.isFull());
    }

    @Test
    void testIsFullFull() {
        assertTrue(animalCorner.isFull());
    }

    @Test
    void testEqualsFalse() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);

        assertFalse(corner.equals(fungiCorner));
        assertNotEquals(corner.hashCode(), fungiCorner.hashCode());
        assertFalse(corner.equals(hiddenCorner));
        assertNotEquals(corner.hashCode(), hiddenCorner.hashCode());
    }

    @Test
    void testEqualsTrue() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);

        assertTrue(corner.equals(animalCorner));
        assertEquals(corner.hashCode(), animalCorner.hashCode());
    }
}
