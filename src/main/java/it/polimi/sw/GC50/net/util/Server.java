package it.polimi.sw.GC50.net.util;

import com.google.gson.Gson;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Match;
import it.polimi.sw.GC50.net.util.Request;
import it.polimi.sw.GC50.view.View;

import java.util.ArrayList;

public class Server {

    private ArrayList<ClientInterface> freeClient;
    private ArrayList<Match> matches;
    private int generatorId;
    private int test;

    public Server() {
        this.matches = new ArrayList<>();
        this.freeClient = new ArrayList<>();
        this.generatorId = 1;
        System.out.println("Server Ready!");
    }

    ///////////////////////////////////////////////////////////
    ///LOBBY
    ///////////////////////////////////////////////////////////

    public int connect(ClientInterface client) {
        freeClient.add(client);
        System.out.println("Client connected to Server");
        this.generatorId++;
        return this.generatorId;
    }

    public int createMatch(ClientInterface client, int numOfPlayer, String gameName, View view) {
        matches.add(new Match(matches.size(), client, numOfPlayer, gameName));
        matches.get(matches.size()-1).addObserver(view);
        System.out.println(gameName + " Match created");
        return matches.size() - 1;
    }

    public void enterMatch(String code, ClientInterface player, View view) {
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getName().equals(code) && matches.get(i).isFree()) {
                matches.get(i).addPlayer(player);
                matches.get(i).addObserver(view);
            }
        }
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
    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    public Request message(String gameName, ClientInterface clientInterface, Request request, Gson gson) {
        Request rq = Request.GAMENOTFOUND;
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getName().equals(gameName)) {
                rq = matches.get(i).update(clientInterface, request, gson);
            }
        }
        return rq;
    }

    public Gson getModel(String gameName, ClientInterface clientInterface, Request request, Gson gson) {
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getName().equals(gameName)) {
                return matches.get(i).getModel(clientInterface, request, gson);
            }
        }
        return null;
    }
}
