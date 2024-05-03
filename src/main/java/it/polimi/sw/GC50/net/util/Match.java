package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.net.gameMexFromClient.PlaceCardMex;


import java.util.HashMap;
import java.util.Map;

public class Match {
    private boolean isFree;
    private int numOfPlayer;
    private final int code;
    private final String gameName;

    private Map<ClientInterface, String> playerMap;
    private final GameController controller;


    //////////////////////////////////////////
    //PRE GAME
    ///////////////////////////////////////////

    public Match(int code, ClientInterface player, int numOfPlayer, String gameName, String nickName) {
        this.code = code; ///used if there are more than one match with the same name
        this.controller = new GameController();
        this.numOfPlayer = numOfPlayer;
        this.gameName = gameName;
        controller.addObserver(player);
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
            this.playerMap.put(player, nickName);
            this.controller.addObserver(player);
            this.controller.addPlayer(new Player(nickName));
            add = true;
        }
        if (numOfPlayer == playerMap.size()) {
            this.isFree = false;
        }
        return add;

    }

    public String getName() {
        return gameName;
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////

    synchronized public void updateController(Request request, ClientInterface clientInterface, Object update, String nickName) {
        if (!playerMap.get(clientInterface).equals(nickName)) {
            return;
        }
        Player player = controller.getPlayer(nickName);
        if (player == null) {
            return;
        }
        switch (request) {
            case PLACE_CARD:
                controller.placeCard(player, (PlaceCardMex) update);
                break;
            case SELECT_STARTER_FACE:
                controller.chooseStarterFace(player, (Boolean) update);
                break;
            case SELECT_OBJECTIVE_CARD:
                controller.chooseObjective(player, (Integer) update);
                break;
            case DRAW_CARD:
                controller.drawCard(player, (DrawingPosition) update);
                break;
            default:
                break;
        }
    }

    synchronized public void updateChat(ClientInterface clientInterface, String nickName, String message) {
        if (!playerMap.get(clientInterface).equals(nickName)) {
            return;
        }
        Player player = controller.getPlayer(nickName);
        if (player == null) {
            return;
        }
        System.out.println("chat updated");
        controller.updateChat(player, message);
    }
    synchronized public Object getModel(String nickName, ClientInterface clientInterface) {
        if (!playerMap.get(clientInterface).equals(nickName)) {
            return null;
        }
        Player player = controller.getPlayer(nickName);
        if (player == null) {
            return null;
        }
        return controller.getGameModel(nickName);
    }


}
