package it.polimi.sw.GC50.net;

import it.polimi.sw.GC50.controller.Controller2;
import it.polimi.sw.GC50.controller.GameController;
import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.net.socket.ClientHandler;
import it.polimi.sw.GC50.net.util.ClientInterface;

import java.rmi.RemoteException;

public class UpdateController {
    public void addPlayer(GameController controller,ClientInterface clientInterface){
        try {
            controller.addPlayer(new Player(clientInterface.getNickName()));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
    public void placeCard(GameController gameController,ClientInterface clientInterface){
        try {
            if(clientInterface.getNickName().equals(clientInterface.getNickName())){
                gameController.placeCard(gameController.getGame().getCurrentPlayer(),true,1,1);
            }else{
                //errore
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
