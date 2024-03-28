package it.polimi.sw.gianpaolocugola50.model.card;

public class Corner {
    /**
     * Status of the corner (HIDDEN, EMPTY or FULL)
     */
    private final CornerStatus status;

    /**
     * Resource contained in the corner (null if status is not FULL)
     */
    private final Resource resource;

    public Corner(CornerStatus status, Resource resource) {
        this.status = status;
        this.resource = resource;
    }

    public boolean isVisible() {
        return status.equals(CornerStatus.EMPTY) ||
                status.equals(CornerStatus.FULL);
    }

    public boolean isFull() {
        return status.equals(CornerStatus.FULL);
    }

    public Resource getResource() {
        return isFull() ? resource : null;
    }
}
