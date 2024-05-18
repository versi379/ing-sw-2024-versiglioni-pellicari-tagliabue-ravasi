package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.game.GameStatus;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;


import java.util.HashMap;
import java.util.Map;

public class Match {
    private final int code; ///used if there are more than one match with the same name
    private final String gameId;
    private final Map<ClientInterface, Player> playerMap;
    private final GameController controller;


    //////////////////////////////////////////
    //PRE GAME
    ///////////////////////////////////////////
    public Match(int code, int numOfPlayer, String gameId, ClientInterface client, String nickname) {
        this.code = code;
        this.gameId = gameId;
        playerMap = new HashMap<>();

        Player player = new Player(nickname);
        controller = new GameController(gameId, numOfPlayer, 20, player);
        playerMap.put(client, player);
        controller.addObserver(client);
    }

    public boolean isFree() {
        return controller.isWaiting();
    }

    public boolean addPlayer(ClientInterface client, String nickname) {
        if (isFree()) {
            Player player = new Player(nickname);
            this.playerMap.put(client, player);
            this.controller.addObserver(client);
            this.controller.addPlayer(player);
            return true;
        }
        return false;
    }

    public String getGameId() {
        return gameId;
    }

    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////
    synchronized public void updateController(Request request, Object update, ClientInterface clientInterface) {
       System.out.println(request.toString());
        if (!playerMap.containsKey(clientInterface)) {
            return;
        }
        Player player = playerMap.get(clientInterface);
        switch (request) {
            case SELECT_STARTER_FACE:
                controller.chooseStarterFace(player, (Boolean) update);
                break;
            case SELECT_OBJECTIVE_CARD:
                controller.chooseObjective(player, (Integer) update);
                break;
            case PLACE_CARD:
                controller.placeCard(player, (PlaceCardMex) update);
                break;
            case DRAW_CARD:
                controller.drawCard(player, (DrawingPosition) update);
                break;
            default:
                break;
        }
    }

    synchronized public void updateChat(String message, ClientInterface clientInterface) {
        if (!playerMap.containsKey(clientInterface)) {
            return;
        }
        Player player = playerMap.get(clientInterface);
        System.out.println("chat updated");
        controller.updateChat(player, message);
    }

    synchronized public Object getModel(ClientInterface clientInterface) {
        if (!playerMap.containsKey(clientInterface)) {
            return null;
        }

        Player player = playerMap.get(clientInterface);
        return controller.getGameModel(player);
    }
}
