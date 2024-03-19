package it.polimi.sw.gianpaolocugola50.model.card;

public class Corner {
    private final CornerStatus status;



    private final Resource resource;

    public boolean isVisible() {
        return status.equals(CornerStatus.EMPTY) ||
                status.equals(CornerStatus.FULL);
    }
    public Corner(CornerStatus status, Resource resource) {
        this.status = status;
        this.resource = resource;
    }
    public boolean isFull() {
        return status.equals(CornerStatus.FULL);
    }
    public Resource getResource() {
        return resource;
    }
}
