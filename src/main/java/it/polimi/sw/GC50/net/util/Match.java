package it.polimi.sw.GC50.net.util;

import com.google.gson.Gson;
import it.polimi.sw.GC50.controller.Controller2;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private boolean isFree;
    private int numOfPlayer;
    private final int code;
    private final String gameName;
    private List<ClientInterface> player;
    //private GameController gameController;
    private Controller2 controller;


    public Match(int code, ClientInterface player, int numOfPlayer, String gameName) {
        this.code = code;
        this.player = new ArrayList<>();
        this.player.add(player);
        this.controller = new Controller2();
        this.numOfPlayer = numOfPlayer;
        this.gameName = gameName;
    }

    public void addObserver(Observer ob) {
        controller.addObserver(ob);
        controller.add();

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


    public String getName() {
        return gameName;
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    public Request update(ClientInterface clientInterface, Request request, Gson gson) {
        return null;
    }

    public Gson getModel(ClientInterface clientInterface, Request request, Gson gson) {
        return null;
    }
}
