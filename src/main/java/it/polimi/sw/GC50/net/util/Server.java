package it.polimi.sw.GC50.net.util;

import com.google.gson.Gson;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Match;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.View;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Server {

    private ArrayList<ClientInterface> freeClient;
    private ArrayList<Match> matches;
    private ArrayList<String> nicknames;
    private int generatorId;
    private int test;

    public Server() {
        this.matches = new ArrayList<>();
        this.freeClient = new ArrayList<>();
        this.nicknames = new ArrayList<>();
        this.generatorId = 1;
        System.out.println("Server Ready!");
    }

    ///////////////////////////////////////////////////////////
    ///LOBBY
    ///////////////////////////////////////////////////////////

    public int connect(ClientInterface client) {
        freeClient.add(client);
        System.out.println("Client connected to Server");
        return this.generatorId;
    }

    public Match createMatch(ClientInterface client, int numOfPlayer, String gameName, View view) {

        matches.add(new Match(matches.size(), client, numOfPlayer, gameName));
        System.out.println(gameName + " Match created");
        return matches.get(matches.size()-1);
    }

    public Match enterMatch(String code, ClientInterface player, String nickname) {
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getName().equals(code) && matches.get(i).isFree()) {
                matches.get(i).addPlayer(player, nickname);
                return matches.get(i);
            }
        }
        return null;
    }


    public ArrayList<String> getFreeMatchesNames() {
        ArrayList<String> freeMatch = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).isFree()) {
                freeMatch.add(matches.get(i).getName());
            }
        }
        return freeMatch;
    }

    public boolean addName(String name) {

        for (int i = 0; i < nicknames.size(); i++)
            if (name.equals(nicknames.get(i))) {
                return false;
            }
        nicknames.add(name);
        return true;
    }

}
