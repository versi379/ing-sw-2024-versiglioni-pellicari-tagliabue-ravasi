package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.controller.LobbyController;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Server {
    ArrayList<User> clConnected;
    ArrayList<ClientInterface> freeClient;
    ArrayList<Match> matches;
    int generatorId;
    int test;
    public Server() {
        this.matches =new ArrayList<>();
        this.clConnected=new ArrayList<>();
        this.freeClient=new ArrayList<>();
        this.generatorId=1;
        System.out.println("Server Ready!");
    }

    public int connect(ClientInterface client) {
        this.clConnected.add(new User(client));
        freeClient.add(client);
        System.out.println("Client connected to Server");
        this.generatorId++;
        return this.generatorId;
    }
    public int createMatch(ClientInterface client,int numOfPlayer){
        matches.add(new Match(matches.size(),client,numOfPlayer));
        System.out.println("new Match created");
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
