package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.view.ViewType;
import it.polimi.sw.GC50.view.View;

import java.util.ArrayList;

public interface RequestFromClientToServer {
    void addView(View view, ViewType viewType);
    //////////////////////////////////////////
    //LOBBY
    ///////////////////////////////////////////
    public String createGame(String matchName, int numberOfPlayer) ;

    public String enterGame(String matchName) ;

    public String setName(String name) ;

    public ArrayList<String> getFreeMatch();
    //////////////////////////////////////////
    //ACTIVE GAME
    ///////////////////////////////////////////
    public void placeCard(boolean face, int index, int x, int y) ;
    public void sendMessage(String message);
    public void selectStarterFace();
    public void selectObjectiveCard();
    public void drawCard();
    public Object getModel();
    public void waitNotifyModelChangedFromServer();


}
