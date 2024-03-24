package it.polimi.sw.gianpaolocugola50.model.game;

import it.polimi.sw.gianpaolocugola50.model.card.PhysicalCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void Test1() {
        Game a = new Game(12, 3, null);
        for (int i = 0; i < 40; i++) {
            PhysicalCard card = a.drawCard(DeckType.RESOURCE);
            System.out.println(card.getCardType());
            System.out.println(card.getFront().getPoints());
            System.out.println(card.getFront().getColor());
            if (card.getFront().getSwCorner().getResource() != null) {
                System.out.println(card.getFront().getSwCorner().getResource().toString());
            } else {
                System.out.println("null");
            }
            if (card.getFront().getSeCorner().getResource() != null) {
                System.out.println(card.getFront().getSeCorner().getResource().toString());
            } else {
                System.out.println("null");
            }
            if (card.getFront().getNwCorner().getResource() != null) {
                System.out.println(card.getFront().getNwCorner().getResource().toString());
            } else {
                System.out.println("null");
            }
            if (card.getFront().getNeCorner().getResource() != null) {
                System.out.println(card.getFront().getNeCorner().getResource().toString());
            } else {
                System.out.println("null");
            }


            System.out.println("______________________________________________________");
        }
        IntStream.range(0, 40).mapToObj(i -> a.drawCard(DeckType.GOLD)).forEach(card -> {
            System.out.println(card.getCardType());
            System.out.println(card.getFront().getPoints());
            System.out.println(card.getFront().getColor());
            if (card.getFront().getSwCorner().getResource() != null) {
                System.out.println(card.getFront().getSwCorner().getResource().toString());
            } else {
                System.out.println("null");
            }
            if (card.getFront().getSeCorner().getResource() != null) {
                System.out.println(card.getFront().getSeCorner().getResource().toString());
            } else {
                System.out.println("null");
            }
            if (card.getFront().getNwCorner().getResource() != null) {
                System.out.println(card.getFront().getNwCorner().getResource().toString());
            } else {
                System.out.println("null");
            }
            if (card.getFront().getNeCorner().getResource() != null) {
                System.out.println(card.getFront().getNeCorner().getResource().toString());
            } else {
                System.out.println("null");
            }
            System.out.println("______________________________________________________");
        });

    }


}