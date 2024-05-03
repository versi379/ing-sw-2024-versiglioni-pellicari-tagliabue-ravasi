package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.view.TypeOfView;
import it.polimi.sw.GC50.view.View;

import java.util.ArrayList;

public interface RequestFromClietToServer {
    void addView(View view, TypeOfView typeOfView);
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
    public void placeCard() ;
    public void sendMessage(String message);
    public void selectStarterFace();
    public void selectObjectiveCard();
    public void drawCard();
    public Object getModel();
    public void waitNotifyModelChangedFromServer();


}
