package it.polimi.sw.GC50.model.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {

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
