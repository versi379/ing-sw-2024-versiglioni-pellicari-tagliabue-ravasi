package it.polimi.sw.GC50.net.util;

import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Observable;

public interface Observer {
    void update(Observable o, Object arg);
    void onUpdate(Message message);
}
