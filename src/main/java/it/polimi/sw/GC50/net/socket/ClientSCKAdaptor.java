package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.view.View;

import java.io.IOException;

public class ClientSCKAdaptor {
    private final View view;
    private ClientSCK clientSCK;

    public ClientSCKAdaptor(View view) {
        this.view = view;
    }

    public void connect(String address, int port) throws IOException {
        this.clientSCK = new ClientSCK(port, address);

    }


    public void lobby() {
     this.clientSCK.lobby();
    }

}
