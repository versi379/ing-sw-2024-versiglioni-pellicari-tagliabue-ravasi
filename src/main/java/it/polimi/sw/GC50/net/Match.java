package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.controller.Controller;
import it.polimi.sw.GC50.view.View;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private boolean isFree;
    private int numOfPlayer;
    private final int code;
    private List<ClientInterface> player;
    private final Controller controller;

    public Match(int code,ClientInterface player, int numOfPlayer) {
        this.code = code;
        this.player = new ArrayList<>();
        this.player.add(player);
        this.controller = new Controller(new View());
        this.numOfPlayer=numOfPlayer;

    }

    public boolean isFree() {
        return isFree;
    }

    public void addPlayer(ClientInterface player) {
        this.player.add(player);
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
