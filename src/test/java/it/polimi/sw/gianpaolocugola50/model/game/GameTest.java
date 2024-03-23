package it.polimi.sw.gianpaolocugola50.model.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void Test1() {
         Game a= new Game(12,3,null);

        System.out.println(a.drawCard(DeckType.RESOURCE).getFront().getColor());
    }


}