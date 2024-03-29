package it.polimi.sw.gianpaolocugola50.net.rMi;

import it.polimi.sw.gianpaolocugola50.net.ServerInterface;

import java.rmi.RemoteException;

public class RmiContainServerInterface implements ServerInterface {


    RmiServerInterface rmiServerInterface;
    public RmiContainServerInterface(RmiServerInterface rmiServerInterface) {
        this.rmiServerInterface = rmiServerInterface;
    }
    @Override
    public String test(String a) {
        String c;
        try {
             c = rmiServerInterface.test(a);
        } catch (RemoteException e) {
            c="Errror";
            throw new RuntimeException(e);
        }
        return c;
    }

    @Override
    public void sendMessage(String a) {

    }

    @Override
    public void joinGame(String nickName) {

    }

    @Override
    public void quitGame(String nickName) {

    }

    @Override
    public void createGame(String nickName, int numOfPlayer) {

    }

    @Override
    public void placeCard() {

    }

    @Override
    public void drawCard() {

    }
}
