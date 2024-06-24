package it.polimi.sw.GC50.model.cards;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class to represent a corner of the board,
 * comprising its status and its associated resource
 */
public class Corner implements Serializable {

    private final CornerStatus status;

    /**
     * Resource contained in the corner (null if status is not FULL)
     */
    private final Resource resource;

    /**
     * constructs a new Corner instance
     *
     * @param status   identify the status of the corner
     * @param resource identify resource of the corner
     */
    public Corner(CornerStatus status, Resource resource) {
        this.status = status;
        this.resource = resource;
    }

    /**
     * Verify if the corner is visible or empty
     *
     * @return a boolean that identify the status
     */
    public boolean isVisible() {
        return status.equals(CornerStatus.EMPTY) ||
                status.equals(CornerStatus.FULL);
    }

    /**
     * Verify if the corner is full
     *
     * @return a boolean
     */
    public boolean isFull() {
        return status.equals(CornerStatus.FULL);
    }

    /**
     * Returns corner's resource if the corner is full
     *
     * @return resource or null
     */
    public Resource getResource() {
        return isFull() ? resource : null;
    }

    /**
     * Returns corner status
     *
     * @return status
     */
    public CornerStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Corner corner)) {
            return false;
        }
        return getStatus().equals(corner.getStatus()) &&
                (!isFull() || getResource().equals(corner.getResource()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getResource());
    }
}
