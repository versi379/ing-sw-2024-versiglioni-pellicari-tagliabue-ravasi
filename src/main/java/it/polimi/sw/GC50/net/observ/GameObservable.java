package it.polimi.sw.GC50.net.observ;

import it.polimi.sw.GC50.net.util.Message;
import it.polimi.sw.GC50.net.util.Request;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameObservable {
    private boolean changed = false;
    private final List<GameObserver> obs;

    /**
     * Construct an Observable with zero Observers.
     */

    public GameObservable() {
        obs = new ArrayList<>();
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @throws NullPointerException if the parameter o is null.
     */
    public synchronized void addObserver(GameObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.add(o);
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

    /*
    public synchronized void notifyPlayerJoined(Player player) {
        for (GameObserver o : obs) {
            o.playerJoined(player.getNickname());
        }
    }

    public synchronized void notifyPlayerLeft(Player player) {
        for (GameObserver o : obs) {
            o.playerLeft(player.getNickname());
        }
    }

    public synchronized void notifyGameSetup() {
        for (GameObserver o : obs) {
            o.gameSetup();
        }
    }

    public synchronized void notifyPlayerReady(Player player) {
        for (GameObserver o : obs) {
            o.playerReady(player.getNickname());
        }
    }

    public synchronized void notifyGameStarted() {
        for (GameObserver o : obs) {
            o.gameStarted();
        }
    }

    public synchronized void notifyCardAdded(Player player, PhysicalCard card) {
        for (GameObserver o : obs) {
            if (o.getNickname().equals(player.getNickname())) {
                o.cardAdded(player.getNickname(), card);
            }
        }
    }

    public synchronized void notifyCardRemoved(Player player, int index) {
        for (GameObserver o : obs) {
            if (o.getNickname().equals(player.getNickname())) {
                o.cardRemoved(player.getNickname(), index);
            }
        }
    }

    public synchronized void notifyCardPlaced(Player player, PlayableCard card, int x, int y) {
        for (GameObserver o : obs) {
            o.cardPlaced(player.getNickname(), card, x, y);
        }
    }

    public synchronized void notifyCardDrawn(DrawingPosition drawingPosition) {
        for (GameObserver o : obs) {
            o.cardDrawn(drawingPosition);
        }
    }

    public synchronized void notifyGameEnd(List<Player> winnerList, int totalScore, int objectivesScore) {
        for (GameObserver o : obs) {
            o.gameEnd(winnerList.stream().map(Player::getNickname).toList(), totalScore, objectivesScore);
        }
    }

    public synchronized void notifyChatMessage(Player player, String message) {
        for (GameObserver o : obs) {
            o.chatMessage(player.getNickname(), message);
        }
    }


    /**
     * If this object has changed, as indicated by the
     * {@code hasChanged} method, then notify all of its observers
     * and then call the {@code clearChanged} method to indicate
     * that this object has no longer changed.
     * <p>
     * Each observer has its {@code update} method called with two
     * arguments: this observable object and the {@code arg} argument.
     *
     * @param arg any object.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#hasChanged()
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void notifyObservers(Request request, Object arg) {

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

            if (!changed)
                return;
            clearChanged();
        }

        System.out.println(request.toString());
        for (GameObserver o : obs) {
            try {
                // ((GameObserver) arrLocal[i]).update(this, request, arg);
                o.onUpdate(new Message(request, arg));
            } catch (RemoteException e) {
                System.out.println("Error in notifyObservers");
            }
        }
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        obs.clear();
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
     *
     * @see java.util.Observable#notifyObservers()
     * @see java.util.Observable#notifyObservers(java.lang.Object)
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
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers of this {@code Observable} object.
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return obs.size();
    }
}
