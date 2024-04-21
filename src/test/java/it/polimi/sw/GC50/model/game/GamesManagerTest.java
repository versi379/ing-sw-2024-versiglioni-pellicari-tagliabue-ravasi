package it.polimi.sw.GC50.model.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GamesManagerTest {



    @Test
    void testGetInstance() {
        GamesManager m= GamesManager.getInstance();
        assertNotEquals(null,m);
    }

    @Test
    void testAddPlayer() {
        GamesManager m= GamesManager.getInstance();
        Player pl2=new Player("pl2");
        m.setGame("GAME1",2,20,new Player("pl1"));
        m.addPlayer(pl2);
       assertEquals(true,m.containsPlayer(pl2));
    }

    @Test
    void testRemovePlayer() {
        GamesManager m= GamesManager.getInstance();
        Player pl2=new Player("pl2");
        m.setGame("GAME1",2,20,new Player("pl1"));
        m.addPlayer(pl2);
        assertEquals(true,m.containsPlayer(pl2));
        m.removePlayer(pl2);
        assertEquals(false,m.containsPlayer(pl2));

    }

    @Test
    void testContainsPlayer() {
        GamesManager m= GamesManager.getInstance();
        Player pl2=new Player("pl2");
        m.setGame("GAME1",2,20,new Player("pl1"));
        m.addPlayer(pl2);
        assertEquals(true,m.containsPlayer(pl2));
    }

    @Test
    void testSetGame() {
        GamesManager m= GamesManager.getInstance();
        Player pl2=new Player("pl2");
        m.setGame("GAME1",2,20,new Player("pl1"));
        assertEquals(true,m.containsGame("GAME1"));
    }

    @Test
    void testDeleteGame() {
        GamesManager m= GamesManager.getInstance();
        Player pl2=new Player("pl2");
        m.setGame("GAME1",2,20,new Player("pl1"));
        assertEquals(true,m.containsGame("GAME1"));
        m.deleteGame("GAME1");
        assertEquals(false,m.containsGame("GAME1"));

    }

    @Test
    void testContainsGame() {
        GamesManager m= GamesManager.getInstance();
        Player pl2=new Player("pl2");
        m.setGame("GAME1",2,20,new Player("pl1"));
        assertEquals(true,m.containsGame("GAME1"));
        assertEquals(false,m.containsGame("ciao"));
    }

    @Test
    void testGetGame() {
        GamesManager m= GamesManager.getInstance();
        Player pl2=new Player("pl2");
        m.setGame("GAME1",2,20,new Player("pl1"));
        Game g1= new Game("GAME1",2,20,new Player("pl1"));
        assertEquals(g1,m.getGame("GAME1"));
        assertEquals(null,m.getGame("GAME2"));

    }
}