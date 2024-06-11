package it.polimi.sw.GC50.net.Messages;

import it.polimi.sw.GC50.net.util.PlaceCardRequest;

import java.io.Serializable;

public class ObjectMessage implements Message {

    private Object object;
    private PlaceCardRequest placeCardRequest;

    public ObjectMessage(Object object) {
        this.object = object;
    }

    public ObjectMessage(PlaceCardRequest placeCardRequest) {
        this.placeCardRequest = placeCardRequest;
    }

    public Object getObject() {
        return object;
    }

    public PlaceCardRequest getPlaceCardRequest() {
        return placeCardRequest;
    }
}
