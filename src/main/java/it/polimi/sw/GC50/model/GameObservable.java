package it.polimi.sw.GC50.model;

import it.polimi.sw.GC50.model.lobby.Player;
import it.polimi.sw.GC50.net.messages.Message;
import it.polimi.sw.GC50.net.messages.Notify;
import it.polimi.sw.GC50.view.GameObserver;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class GameObservable {
    private boolean changed = false;
    private final Map<GameObserver, Player> obs;

    /**
     * Construct an Observable with zero Observers.
     */
    public GameObservable() {
        obs = new HashMap<>();
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @throws NullPointerException if the parameter o is null.
     */
    public synchronized void addObserver(GameObserver o, Player player) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.containsKey(o)) {
            obs.put(o, player);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing {@code null} to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    public synchronized void removeObserver(GameObserver o) {
        obs.remove(o);
    }

    /**
     * If this object has changed, as indicated by the
     * {@code hasChanged} method, then notify all of its observers
     * and then call the {@code clearChanged} method to indicate
     * that this object has no longer changed.
     * <p>
     * Each observer has its {@code update} method called with two
     * arguments: this observable object and the {@code arg} argument.
     */
    public void notifyObservers(Notify notify, Message message) {

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */

            if (!hasChanged())
                return;
            clearChanged();
        }

        System.out.println(notify.toString());
        synchronized (obs) {
            for (GameObserver o : obs.keySet()) {
                try {
                    o.update(notify, message);
                } catch (RemoteException e) {
                    System.out.println("Error in notifyObservers");
                }
            }
        }
    }

    /**
     * Marks this {@code Observable} object as having been changed; the
     * {@code hasChanged} method will now return {@code true}.
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the {@code hasChanged} method will now return {@code false}.
     * This method is called automatically by the
     * {@code notifyObservers} methods.
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return {@code true} if and only if the {@code setChanged}
     * method has been called more recently than the
     * {@code clearChanged} method on this object;
     * {@code false} otherwise.
     */
    public synchronized boolean hasChanged() {
        return changed;
    }
}
