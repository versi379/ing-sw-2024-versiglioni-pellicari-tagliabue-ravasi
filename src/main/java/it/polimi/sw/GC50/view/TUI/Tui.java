package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.net.ClientInterface;
import it.polimi.sw.GC50.net.ConnectionFromClientToServer;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.socket.ClientSCKAdaptor;
import it.polimi.sw.GC50.view.View;


import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Tui {
    public void start()  {
     try{
           ClientInterface client = new ClientRmi("localhost");
           ((ClientRmi) client).connect();
           ((ClientRmi) client).lobby();

       }catch (RemoteException e){

       }
        /*
        View view= new View();
        ClientSCKAdaptor clientSCKAdaptor= new ClientSCKAdaptor(view);

        try{
            clientSCKAdaptor.connect("localhost",2012);
            clientSCKAdaptor.lobby();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
*/



    }
}
