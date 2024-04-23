package it.polimi.sw.GC50.net;

import java.util.ArrayList;

public class Server {
    ArrayList<Match> matches;
    int test;
    public Server() {
        this.matches =new ArrayList<>();
        System.out.println("Server Ready!");
    }

    public void connect() {

    }
    public void createMatch(){
        matches.add(new Match(matches.size()));
    }
    public void enterMatch(int code){
        if(matches.get(code).isFree)
    }
}
