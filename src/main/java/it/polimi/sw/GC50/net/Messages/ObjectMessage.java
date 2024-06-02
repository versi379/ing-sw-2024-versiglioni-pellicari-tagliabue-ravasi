package it.polimi.sw.GC50.net.Messages;

import java.io.Serializable;

public class ObjectMessage implements Message, Serializable {

    Object object;

    public ObjectMessage(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

}
