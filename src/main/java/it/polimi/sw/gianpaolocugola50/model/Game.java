package it.polimi.sw.gianpaolocugola50.model;
import java.util.*;

public class Game {
    private final String code;
    private final int numPlayers;
    private final List<Player> players;
    private Stack<PhysicalCard> resourceDeck;
    private Stack<PhysicalCard> goldDeck;
    private PhysicalCard[] revealedCards;
    private ObjectiveCard[] commonObjectives;

    public Game(String code, int numPlayers, Player creator) {
        this.code = code;
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>();
        players.add(creator);
    }
    public void join(Player player) {
        players.add(player);
        if(players.size() == numPlayers) {
            start();
        }
    }
    public void start() {

    }
}
