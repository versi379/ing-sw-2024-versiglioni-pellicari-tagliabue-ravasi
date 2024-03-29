package it.polimi.sw.gianpaolocugola50.net.socket;

import java.rmi.RemoteException;

public interface ServerInterface {
    String test(String a);
    void sendMessage(String a) ;
    void joinGame(String nickName) ;
    void quitGame(String nickName);
    void createGame(String nickName,int numOfPlayer);
    void placeCard();
    void drawCard();
}
