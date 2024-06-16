package it.polimi.sw.GC50.model.cards;

import org.junit.jupiter.api.Test;

import static it.polimi.sw.GC50.model.cards.PlayableCardTest.greenPlayableCard;
import static it.polimi.sw.GC50.model.cards.PlayableCardTest.redPlayableCard;
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