package it.polimi.sw.GC50.net.util;

import com.google.gson.Gson;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.net.UpdateController;
import it.polimi.sw.GC50.net.observ.Observable;
import it.polimi.sw.GC50.net.observ.Observer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match {
    private boolean isFree;
    private int numOfPlayer;
    private final int code;
    private final String gameName;

    private Map<ClientInterface, String> playerMap;
    private final GameController controller;

    private final UpdateController updateController;

    //////////////////////////////////////////
    //PRE GAME
    ///////////////////////////////////////////

    public Match(int code, ClientInterface player, int numOfPlayer, String gameName, String nickName) {
        this.code = code; ///used if there are more than one match with the same name
        this.controller = new GameController();
        this.numOfPlayer = numOfPlayer;
        this.gameName = gameName;
        controller.addObserver(player);
        this.updateController = new UpdateController();
        this.isFree = true;
        this.playerMap = new HashMap<>();
        this.playerMap.put(player, nickName);

    }


    public boolean isFree() {
        return isFree;
    }

    public Boolean addPlayer(ClientInterface player, String nickName) {
        Boolean add = false;
        if (this.playerMap.size() < numOfPlayer) {
            this.playerMap.put( player,nickName);
            controller.addObserver(player);
            add = true;
        }
        if (numOfPlayer == playerMap.size()) {
            this.isFree = false;
            controller.startGame(playerMap);
        }
        return add;

    }

    public String getName() {
        return gameName;
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////


    public synchronized Request update(String nickName, ClientInterface clientInterface, Request request, Object object) {
        Request rq = updateController.update(controller, nickName, clientInterface, request, object);
        return rq;
    }
    public synchronized void updateChat(String nickName, ClientInterface clientInterface, Request request, Object object) {
        updateController.updateChat(controller, nickName, clientInterface, request, object);
    }

    public synchronized Object getModel(ClientInterface clientInterface, Request request, Object object) {
        return updateController.getModel(controller);
    }



}
