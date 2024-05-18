package it.polimi.sw.GC50.net.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Map<ClientInterface, String> freePlayers;
    private final ArrayList<ClientInterface> freeClient;
    private final ArrayList<Match> matches;
    private final ArrayList<String> nicknames;


    public Server() {
        this.matches = new ArrayList<>();
        this.freePlayers = new HashMap<>();
        this.freeClient = new ArrayList<>();
        this.nicknames = new ArrayList<>();

        System.out.println("Server Ready!");
    }

    ///////////////////////////////////////////////////////////
    ///LOBBY
    ///////////////////////////////////////////////////////////

    /**
     * @param client this method is called when a client connects to the server
     */


    public synchronized void connect(ClientInterface client) {
        freeClient.add(client);
        freePlayers.put(client, null);
        System.out.println("Client connected to Server");
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
        if (numOfPlayer < 2 || numOfPlayer > 4) {
            return null;
        }
        if (checkUser(client, nickname)) {
            matches.add(new Match(matches.size(), numOfPlayer, gameId, client, nickname));
            System.out.println(gameId + " Match created");
            freePlayers.remove(client);
            return matches.getLast();
        } else {
            return null;
        }
    }

    private synchronized boolean checkUser(ClientInterface client, String nickname) {
        if (freePlayers.containsKey(client)) {
            if (freePlayers.get(client) == null) {
                return false;
            }
            if (freePlayers.get(client).equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public synchronized Match enterMatch(ClientInterface player, String code, String nickname) {
        if (checkUser(player, nickname)) {
            for (int i = 0; i < matches.size(); i++) {
                if (matches.get(i).getGameId().equals(code) && matches.get(i).isFree()) {
                    matches.get(i).addPlayer(player, nickname);
                    freePlayers.remove(player);
                    return matches.get(i);
                }
            }
        }
        return null;
    }

    public synchronized ArrayList<String> getFreeMatches() {
        if (matches.isEmpty()) {
            return null;
        }
        ArrayList<String> freeMatches = new ArrayList<>();
        for (Match match : matches) {
            if (match.isFree()) {
                freeMatches.add(match.getGameId());
            }
        }
        return freeMatches;
    }

    public synchronized boolean addNickname(ClientInterface clientInterface, String nickname) {
        if (nickname == null) {
            return false;
        }
        if (nickname.isEmpty()) {
            return false;
        }

        if (!nicknames.contains(nickname)) {
            nicknames.add(nickname);
            freePlayers.put(clientInterface, nickname);
            return true;
        }
        return false;
    }
}
