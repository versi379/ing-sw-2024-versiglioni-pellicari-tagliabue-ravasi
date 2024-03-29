package it.polimi.sw.gianpaolocugola50.net.rMi;

import it.polimi.sw.gianpaolocugola50.net.ClientInterface;

public class RmiContainClientInterface implements ClientInterface {
    RmiClientInterface rmiClientInterface;

    public RmiContainClientInterface(RmiClientInterface rmiClientInterface) {
        this.rmiClientInterface = rmiClientInterface;
    }
}
