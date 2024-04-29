package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.controller.Controller2;
import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.game.Game;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.view.Observer;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private boolean isFree;
    private int numOfPlayer;
    private final int code;
    private List<ClientInterface> player;
    //private GameController gameController;
    private final Controller2 controller;


    public Match(int code,ClientInterface player, int numOfPlayer) {
        this.code = code;
        this.player = new ArrayList<>();
        this.player.add(player);
        //this.gameController = new GameController();
        this.controller=new Controller2(new Game(null,numOfPlayer,20,new Player(null)));
        this.numOfPlayer=numOfPlayer;
    }
    public void addObserver(Observer ob){
        controller.addObserver(ob);
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

    public GameController getController() {
        //return gameController;
        return null;
    }
}
