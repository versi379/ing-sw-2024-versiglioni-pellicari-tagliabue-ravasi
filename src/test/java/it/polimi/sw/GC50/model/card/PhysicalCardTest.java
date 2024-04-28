package it.polimi.sw.GC50.model.card;

import org.junit.jupiter.api.Test;

import static it.polimi.sw.GC50.model.card.PlayableCardTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PhysicalCardTest {

    @Test
    void testPhysicalCardConstructor() {
        PhysicalCard card = new PhysicalCard(CardType.RESOURCE, redPlayableCard, greenPlayableCard);

        assertEquals(CardType.RESOURCE, card.getCardType());
        assertEquals(redPlayableCard, card.getFront());
        assertEquals(greenPlayableCard, card.getBack());
    }
}