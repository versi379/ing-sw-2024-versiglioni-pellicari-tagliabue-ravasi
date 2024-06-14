package it.polimi.sw.GC50.net.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeGamesMex implements Message {
    private final Map<String, List<String>> freeGames;

    public FreeGamesMex(Map<String, List<String>> freeGames) {
        this.freeGames = new HashMap<>(freeGames);
    }

    public Map<String, List<String>> getFreeGames() {
        return new HashMap<>(freeGames);
    }
}
