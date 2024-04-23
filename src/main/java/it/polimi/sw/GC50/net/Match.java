package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.controller.Controller;

import java.util.ArrayList;
import java.util.List;

public class Match {
    public boolean isFree;
    private int numOfPlayer;
    private final int code;
    private List<ClientInterface> player;
    private final Controller controller;

    public Match(int code) {
        this.code=code;
        this.player=new ArrayList<>();
        controller=new Controller();

    }
    public void addPlayer(){


    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public void setNumOfPlayer(int numOfPlayer) {
        this.numOfPlayer = numOfPlayer;
    }

    public int getCode() {
        return code;
    }

    public List<ClientInterface> getPlayer() {
        return player;
    }

    public void setPlayer(List<ClientInterface> player) {
        this.player = player;
    }

    public Controller getController() {
        return controller;
    }
}
