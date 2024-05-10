package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.net.gameMexNet.ModelMex;

import java.io.Serializable;

public interface View extends Serializable {

    String askName();
    String askGameName();
    int askNumberOfPlayer();
    void waitPlayer();
    int joinorcreate();
    void allPlayerReady();
    void addModel(ModelMex modelmex);

    int SelectObjectiveCard();
    Boolean selectStarterFace();
    int game();

}
