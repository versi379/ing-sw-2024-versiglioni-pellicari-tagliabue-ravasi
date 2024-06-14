package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.util.Command;

import java.io.Serializable;

public class CommandMessage implements Serializable {
    private final Command command;
    private final Object content;

    public CommandMessage(Command command, Object content) {
        this.command = command;
        this.content = content;
    }

    public Command getCommand() {
        return command;
    }

    public Object getContent() {
        return content;
    }
}
