package it.polimi.sw.gianpaolocugola50.model.card;

public class Corner {
    private final CornerStatus status;
    private final Resource resource;

    public Corner(CornerStatus status, Resource resource) {
        this.status = status;
        // da rivedere, non è necessario mettere l'if dato che
        // verrebbe comunque ritornato null in getResource
        this.resource = (status == CornerStatus.FULL) ? resource : null;
    }


    public boolean isVisible() {
        return status.equals(CornerStatus.EMPTY) ||
                status.equals(CornerStatus.FULL);
    }

    public boolean isFull() {
        return status.equals(CornerStatus.FULL);
    }

    public Resource getResource() {
        //return (status == CornerStatus.FULL) ? resource : null;
        return resource;
    }
}
