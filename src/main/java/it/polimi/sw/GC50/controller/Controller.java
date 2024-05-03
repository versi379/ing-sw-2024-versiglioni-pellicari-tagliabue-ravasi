package it.polimi.sw.GC50.controller;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.card.PlayableCard;
import it.polimi.sw.GC50.model.game.*;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.model.objective.ObjectiveCard;
import it.polimi.sw.GC50.net.util.ClientInterface;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Controller  {
    private  Game game = null;

    public Controller(String gameName, int numPlayers, String nickname) {
        this.game = new Game(gameName, numPlayers, 20, new Player(nickname));
    }
    public void addObserver(ClientInterface clientInterface) {
        game.addObserver(clientInterface);
    }

    public void addPlayer(String nickname){
        Player player = new Player(nickname);
        this.game.addPlayer(player);
    }
    public void placeCard (String nickname,boolean face, int x, int y,String code){

        game.placeCard(nickname,code,x,y,face);

    }



    // TEST METHODS ____________________________________________________________________________________________________
    public Game getGame() {
        return game;
    }




}
