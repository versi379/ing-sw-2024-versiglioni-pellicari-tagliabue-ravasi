package it.polimi.sw.GC50.net.observ;

import it.polimi.sw.GC50.net.util.Message;

public interface Observer {
    void update(Observable o, Object arg);
    void onUpdate(Message message);
}
