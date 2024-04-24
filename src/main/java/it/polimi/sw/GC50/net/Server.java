package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.controller.MainController;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Server {
    MainController mainController;
    Map<String, GameController> gameControllers;
    ArrayList<ClientInterface> freeClient;
    ArrayList<Match> matches;
    int test;
    public Server() {
        this.matches =new ArrayList<>();
        System.out.println("Server Ready!");
    }

    public void connect(ClientInterface client) {
        freeClient.add(client);

    }
    public int createMatch(ClientInterface client,int numOfPlayer){
        matches.add(new Match(matches.size(),client,numOfPlayer));
        return matches.size()-1;
    }
    public void enterMatch(int code,ClientInterface player){
        if(matches.get(code).isFree()){
            freeClient.remove(player);
            matches.get(code).addPlayer(player);
        }
    }
    public Objects getSModel(){

        return null;
    }
}
