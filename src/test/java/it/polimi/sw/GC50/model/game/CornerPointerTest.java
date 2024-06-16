package it.polimi.sw.GC50.model.game;

import it.polimi.sw.GC50.model.cards.Corner;
import it.polimi.sw.GC50.model.cards.CornerStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CornerPointerTest {

    @Test
    void testCornerPointerConstructor() {
        CornerPointer cornerPointer = new CornerPointer();

        assertFalse(cornerPointer.isPresent());
        assertNull(cornerPointer.getCorner());
    }

    @Test
    void testSetCorner() {
        CornerPointer cornerPointer = new CornerPointer();
        Corner corner = new Corner(CornerStatus.EMPTY, null);
        cornerPointer.setCorner(corner);

        assertTrue(cornerPointer.isPresent());
        assertEquals(corner, cornerPointer.getCorner());
    }
}
