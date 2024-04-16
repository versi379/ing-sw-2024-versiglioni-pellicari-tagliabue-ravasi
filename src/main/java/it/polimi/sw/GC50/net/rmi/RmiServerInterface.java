package it.polimi.sw.GC50.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RmiServerInterface extends Remote {
     String test(String a)throws RemoteException;

     void sendMessage(String a) throws RemoteException;
     void joinGame(String nickName) throws RemoteException;
     void quitGame(String nickName)throws RemoteException;
     void createGame(String nickName,int numOfPlayer)throws RemoteException;
     void placeCard()throws RemoteException;
     void drawCard() throws RemoteException;
}
