package it.polimi.sw.GC50.net.messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class that shows free games
 */
public class FreeGamesMex implements Message {
    private final Map<String, List<String>> freeGames;

    /**
     * Constructs an instance of FreeMex class
     *
     * @param freeGames a list of free games
     */
    public FreeGamesMex(Map<String, List<String>> freeGames) {
        this.freeGames = new HashMap<>(freeGames);
    }

    /**
     * Returns a hashmap of free games
     */
    public Map<String, List<String>> getFreeGames() {
        return new HashMap<>(freeGames);
    }
}
