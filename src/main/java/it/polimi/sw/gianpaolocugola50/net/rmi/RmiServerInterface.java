package it.polimi.sw.gianpaolocugola50.net.rmi;

import it.polimi.sw.gianpaolocugola50.net.ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerInterface extends Remote, ServerInterface {

    //public void gameCreation(String idGame, String nickname, int numOfPlayers) throws RemoteException;

}
