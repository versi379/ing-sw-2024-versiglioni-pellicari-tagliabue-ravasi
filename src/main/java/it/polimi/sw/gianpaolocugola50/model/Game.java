package it.polimi.sw.gianpaolocugola50.model;

import java.util.*;

public class Game {
    //idGame it identify the game
    private final int idGame;

    //number of player
    private final int numPlayers;

    //list of player of one game, max 4 player
    private  List<Player> players;

    //the player that is on turn
    private Player nowPlayer;

    //the winner of the game
    private Player winnerPlayer;

    //deck of resourceCard
    private Stack<PhysicalCard> resourceDeck;

    //deck of goldCard
    private Stack<PhysicalCard> goldDeck;

    //resource and gold card on the desk
    private PhysicalCard[] revealedCards;

    //gold card on the desk
    private ObjectiveCard[] commonObjectives;


    public Game(int idGame, int numPlayers, Player creator) {
        this.idGame = idGame;
        this.numPlayers = 0;
        this.players = new ArrayList<>();
        players.add(creator);
    }

    public void join(Player player) {
        players.add(player);
        if (players.size() == numPlayers) {
            start();
        }
    }

    public void start() {

    }
}
