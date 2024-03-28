package it.polimi.sw.gianpaolocugola50.view.TUI;

import it.polimi.sw.gianpaolocugola50.net.ConnectionFromClientToServer;
import it.polimi.sw.gianpaolocugola50.net.rMi.RmiServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Tui {
    public void start() {
        try {
            RmiServerInterface serverInterface = ConnectionFromClientToServer.createRmiConnection(1099, "localhost");
        } catch (RemoteException e) {

        } catch (NotBoundException e) {

        }
    }
}
