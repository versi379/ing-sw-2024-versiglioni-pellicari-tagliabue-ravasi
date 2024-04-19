package it.polimi.sw.GC50.model.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {

    @Test
    public void testCornerConstructorHidden() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertEquals(CornerStatus.HIDDEN, corner.getStatus());
        assertNull(corner.getResource());
    }
    @Test
    public void testCornerConstructorEmpty() {
        Corner corner = new Corner(CornerStatus.EMPTY, null);
        assertEquals(CornerStatus.EMPTY, corner.getStatus());
        assertNull(corner.getResource());
    }
    @Test
    public void testCornerConstructorFull() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);
        assertEquals(CornerStatus.FULL, corner.getStatus());
        assertEquals(Resource.ANIMAL, corner.getResource());
    }

    @Test
    public void isVisibleHidden() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertFalse(corner.isVisible());
    }
    @Test
    public void isVisibleEmpty() {
        Corner corner = new Corner(CornerStatus.EMPTY, null);
        assertTrue(corner.isVisible());
    }
    @Test
    public void isVisibleFull() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);
        assertTrue(corner.isVisible());
    }

    @Test
    public void isFullHidden() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertFalse(corner.isFull());
    }
    @Test
    public void isFullFull() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);
        assertTrue(corner.isFull());
    }

    @Test
    void getResourceHidden() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertNull(corner.getResource());
    }
    @Test
    void getResourceFull() {
        Corner corner = new Corner(CornerStatus.FULL, Resource.ANIMAL);
        assertEquals(Resource.ANIMAL, corner.getResource());
    }

    @Test
    void getStatus() {
        Corner corner = new Corner(CornerStatus.HIDDEN, null);
        assertEquals(CornerStatus.HIDDEN, corner.getStatus());
    }
}
