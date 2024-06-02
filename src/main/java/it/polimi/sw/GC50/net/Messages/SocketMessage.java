package it.polimi.sw.GC50.net.Messages;

import it.polimi.sw.GC50.net.util.Notify;
import it.polimi.sw.GC50.view.Command;

import java.io.Serializable;

public class SocketMessage implements Serializable {
    private Message message;
    private Notify notify;
    private Command command;
    private LobbyCommand lobbyCommand;

    public SocketMessage(Message message, LobbyCommand lobbyCommand) {
        this.message = message;
        this.lobbyCommand = lobbyCommand;
    }

    public SocketMessage(Notify notify, Message message) {
        this.notify = notify;
        this.message = message;
    }

    public SocketMessage(Message message, Command command) {
        this.message = message;
        this.command = command;
    }

    public LobbyCommand getLobbyCommand() {
        return lobbyCommand;
    }

    public Message getMessage() {
        return message;
    }

    public Notify getNotify() {
        return notify;
    }

    public Command getCommand() {
        return command;
    }
}
