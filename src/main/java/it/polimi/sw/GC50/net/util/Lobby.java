package it.polimi.sw.GC50.net.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
    private final Map<ClientInterface, String> freePlayers;
    private final ArrayList<String> nicknames;
    private final ArrayList<Match> matches;


    public Lobby() {
        freePlayers = new HashMap<>();
        nicknames = new ArrayList<>();
        matches = new ArrayList<>();

        System.out.println("Server Ready!");
    }

    ///////////////////////////////////////////////////////////
    ///LOBBY
    ///////////////////////////////////////////////////////////

    /*
    /**
     * @param client this method is called when a client connects to the server

    public synchronized void connect(ClientInterface client) {
        freePlayers.put(client, null);
        System.out.println("Client connected to Server");
    }
    */

    public synchronized boolean addPlayer(ClientInterface clientInterface, String nickname) {
        if (nickname == null || nickname.isEmpty() || nicknames.contains(nickname)) {
            return false;
        }
        nicknames.add(nickname);
        freePlayers.put(clientInterface, nickname);
        return true;
    }

    /**
     * @param client
     * @param numOfPlayer
     * @param gameId
     * @param nickname
     * @return Match
     * the return value is the match created by the client
     * if is it null the Match is not created
     * this method is called when a client wants to create a match
     * @
     */
    public synchronized Match createMatch(ClientInterface client, String gameId, int numOfPlayer, String nickname) {
        if (isPlayerPresent(client) && !isGamePresent(gameId)) {
            matches.add(new Match(matches.size(), gameId, numOfPlayer, client, nickname));
            System.out.println(gameId + " Match created");
            freePlayers.remove(client);
            return matches.getLast();
        } else {
            return null;
        }
    }

    private synchronized boolean isPlayerPresent(ClientInterface client) {
        return freePlayers.containsKey(client);
    }

    private synchronized boolean isGamePresent(String gameId) {
        return matches.stream()
                .map(Match::getGameId)
                .anyMatch(gameId::equals);
    }

    public synchronized Match joinMatch(ClientInterface player, String gameId, String nickname) {
        if (isPlayerPresent(player)) {
            for (Match match : matches) {
                if (match.getGameId().equals(gameId) && match.isFree()) {
                    match.addPlayer(player, nickname);
                    freePlayers.remove(player);
                    return match;
                }
            }
        }
        return null;
    }

    public synchronized List<String> getFreeMatches() {
        List<String> freeMatches = new ArrayList<>();
        for (Match match : matches) {
            if (match.isFree()) {
                freeMatches.add(match.getGameId());
            }
        }
        return freeMatches;
    }
}
