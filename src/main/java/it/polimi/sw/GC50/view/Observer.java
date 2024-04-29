package it.polimi.sw.GC50.view;

import it.polimi.sw.GC50.net.Message;
import it.polimi.sw.GC50.net.Observable;

public interface Observer {
    void update(Observable o, Object arg);
    void onUpdate(Message message);
}
