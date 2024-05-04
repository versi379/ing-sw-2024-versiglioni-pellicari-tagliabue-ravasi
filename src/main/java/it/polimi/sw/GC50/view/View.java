package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.net.observ.Observer;

import java.io.Serializable;

public interface View extends Serializable {

    String askName();
    String askGameName();
    int askNumberOfPlayer();
    void waitPlayer();
    int joinorcreate();
    void allPlayerReady();
}
