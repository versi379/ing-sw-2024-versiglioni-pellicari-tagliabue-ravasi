package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.card.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CornerPointerTest {

    @Test
    void testCornerPointerConstructor() {
        CornerPointer cornerPointer = new CornerPointer();
        assertFalse(cornerPointer.isPresent());
        assertNull(cornerPointer.getCorner());
    }

    @Test
    void testIsPresent() {
        CornerPointer cornerPointer = new CornerPointer();
        cornerPointer.setCorner(new Corner(CornerStatus.EMPTY, null));
        assertTrue(cornerPointer.isPresent());
    }

    @Test
    void testSetCorner() {
        CornerPointer cornerPointer = new CornerPointer();
        Corner corner = new Corner(CornerStatus.EMPTY, null);
        cornerPointer.setCorner(corner);
        assertTrue(cornerPointer.isPresent());
        assertEquals(corner, cornerPointer.getCorner());
    }

    @Test
    void testGetCornerNotPresent() {
        CornerPointer cornerPointer = new CornerPointer();
        assertNull(cornerPointer.getCorner());
    }

    @Test
    void testGetCornerPresent() {
        CornerPointer cornerPointer = new CornerPointer();
        Corner corner = new Corner(CornerStatus.EMPTY, null);
        cornerPointer.setCorner(corner);
        assertEquals(corner, cornerPointer.getCorner());
    }
}