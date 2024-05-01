package it.polimi.sw.GC50.net;

import com.google.gson.Gson;
import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.card.BlankBonus;
import it.polimi.sw.GC50.model.card.Resource;
import it.polimi.sw.GC50.model.card.ResourcesBonus;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.net.socket.ClientHandler;
import it.polimi.sw.GC50.net.util.ClientInterface;
import it.polimi.sw.GC50.net.util.Request;

import java.rmi.RemoteException;

public class UpdateController {
    public void addPlayer(GameController controller, ClientInterface clientInterface) {


    }

     public Request update(GameController gameController, String nickName, ClientInterface clientInterface, Request request, Object object) {
        switch (request) {

            case Request.DRAWCARDGOLD0: {

            }
            case Request.DRAWCARDGOLD1: {

            }
            case Request.DRAWCARDGOLD2: {

            }
            case Request.DRAWRESOURCE0: {

            }
            case Request.DRAWRESOURCE1: {

            }
            case Request.DRAWRESOURCE2: {

            }
            case Request.SELECTOBJECTIVE: {

            }
            case Request.PLACECARD: {

            }
            case Request.MEXCHAT: {

            }
            case Request.STARTERFACE:{

            }
        }

        return null;
    }
    public Object getModel(GameController controller){

        return null;
    }
}
