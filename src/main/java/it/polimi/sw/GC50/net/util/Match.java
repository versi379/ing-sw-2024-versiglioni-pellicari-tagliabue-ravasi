package it.polimi.sw.GC50.net.util;

import com.google.gson.Gson;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.net.UpdateController;
import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.observ.Observer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Match {
    private boolean isFree;
    private int numOfPlayer;
    private final int code;
    private final String gameName;
    private List<ClientInterface> player;
    private final GameController controller;

    private final UpdateController updateController;


    public Match(int code, ClientInterface player, int numOfPlayer, String gameName) {
        this.code = code;
        this.player = new ArrayList<>();
        this.player.add(player);
        this.controller = new GameController();
        this.numOfPlayer = numOfPlayer;
        this.gameName = gameName;
        controller.addObserver(this);
        this.updateController = new UpdateController();
        this.isFree = true;


    }

    public boolean isFree() {
        return isFree;
    }

    public Boolean addPlayer(ClientInterface player, String nickName) {
        Boolean add = false;
        if (this.player.size() < numOfPlayer) {
            this.player.add(player);
            add = true;
        }
        if (numOfPlayer == this.player.size()) {
            this.isFree = false;
        }
        return add;

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


    public Request update(String nickName, ClientInterface clientInterface, Request request, Object object) {
        Request rq = updateController.update(controller, nickName, clientInterface, request, object);
        return rq;
    }

    public Object getModel(ClientInterface clientInterface, Request request, Object object) {
        return updateController.getModel(controller);
    }

    public void addObserver(Observer o) {
        controller.addObserver(null);
    }

}
