package it.polimi.sw.GC50.model.card;

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
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertFalse(corner.isVisible());
    }

    @Test
    void testIsVisibleEmpty() {
        Corner corner = new Corner(CornerStatus.EMPTY, null);
        assertTrue(corner.isVisible());
    }

    @Test
    void testIsVisibleFull() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);
        assertTrue(corner.isVisible());
    }

    @Test
    void testIsFullHidden() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertFalse(corner.isFull());
    }

    @Test
    void testIsFullFull() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);
        assertTrue(corner.isFull());
    }

    @Test
    void testGetResourceHidden() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertNull(corner.getResource());
    }

    @Test
    void testGetResourceFull() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);
        assertEquals(Resource.ANIMAL, corner.getResource());
    }

    @Test
    void testGetStatus() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertEquals(CornerStatus.HIDDEN, corner.getStatus());
    }
}
