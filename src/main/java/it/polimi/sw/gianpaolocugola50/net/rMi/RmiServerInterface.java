package it.polimi.sw.gianpaolocugola50.net.rMi;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RmiServerInterface extends Remote {
     void sendMessage(String a) throws RemoteException;
     void joinGame(String nickName) throws RemoteException;
     void quitGame(String nickName)throws RemoteException;
     void createGame(String nickName,int numOfPlayer)throws RemoteException;
     void placeCard()throws RemoteException;
     void drawCard() throws RemoteException;
}
