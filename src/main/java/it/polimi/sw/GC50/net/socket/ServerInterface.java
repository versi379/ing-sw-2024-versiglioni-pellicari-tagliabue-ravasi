package it.polimi.sw.GC50.net.socket;

public interface ServerInterface {
    String test(String a);
    void sendMessage(String a) ;
    void joinGame(String nickName) ;
    void quitGame(String nickName);
    void createGame(String nickName,int numOfPlayer);
    void placeCard();
    void drawCard();
}
