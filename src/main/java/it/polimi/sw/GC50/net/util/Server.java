package it.polimi.sw.GC50.net.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private Map<ClientInterface, String> freePlayer;
    private ArrayList<ClientInterface> freeClient;
    private ArrayList<Match> matches;
    private ArrayList<String> nicknames;


    public Server() {
        this.matches = new ArrayList<>();
        this.freePlayer = new HashMap<>();
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
        freePlayer.put(client, null);
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
        if(numOfPlayer < 2 || numOfPlayer > 4){
            return null;
        }
        if (checkUser(client, nickname)) {
            matches.add(new Match(matches.size(), client, gameId, numOfPlayer, nickname));
            System.out.println(gameId + " Match created");
            freePlayer.remove(client);
            return matches.get(matches.size() - 1);

        } else {
            return null;
        }

    }

    private synchronized boolean checkUser(ClientInterface client, String nickname) {
        if (freePlayer.containsKey(client)) {
            if (freePlayer.get(client) == null) {
                return false;
            }
            if (freePlayer.get(client).equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public synchronized Match enterMatch(String code, ClientInterface player, String nickname) {
        if (checkUser(player, nickname)) {
            for (int i = 0; i < matches.size(); i++) {
                if (matches.get(i).getGameId().equals(code) && matches.get(i).isFree()) {
                    matches.get(i).addPlayer(player, nickname);
                    freePlayer.remove(player);
                    return matches.get(i);
                }
            }
        }
        return null;
    }

    public synchronized ArrayList<String> getFreeMatchesNames() {
        if (matches.isEmpty()) {
            return null;
        }
        ArrayList<String> freeMatch = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).isFree()) {
                freeMatch.add(matches.get(i).getGameId());
            }
        }
        return freeMatch;
    }

    public synchronized boolean addName(ClientInterface clientInterface, String name) {
        if (name == null) {
            return false;
        }
        if (name.equals("")) {
            return false;
        }
        if (nicknames.isEmpty()) {
            nicknames.add(name);
            freePlayer.put(clientInterface, name);
            return true;
        }

        for (int i = 0; i < nicknames.size(); i++)
            if (name.equals(nicknames.get(i))) {
                return false;
            }
        nicknames.add(name);
        freePlayer.put(clientInterface, name);
        return true;
    }
}
