package it.polimi.sw.gianpaolocugola50.model;

import java.io.Serializable;
import java.time.LocalTime;

public class Message implements Serializable {

    private String text;
    private Player sender;
    private LocalTime time;

    //private int x;

    public Message(String text, Player sender) {
        this.text = text;
        this.sender = sender;
        this.time = java.time.LocalTime.now();
    }

    public String toString(){
        return null;
    } // to do

}
