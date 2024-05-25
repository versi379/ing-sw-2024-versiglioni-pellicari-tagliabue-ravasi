package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.net.util.Client;
import it.polimi.sw.GC50.net.util.GameException;
import it.polimi.sw.GC50.net.util.PlaceCardRequest;
import it.polimi.sw.GC50.net.util.ServerInterface;

import java.util.List;
import java.util.Map;

public class ClientSCK implements ServerInterface {
    private final Client client;
    private final String serverIp;
    private final String serverPort;

    public ClientSCK(Client client, String serverIp, String serverPort) /*throws IOException*/ {
        this.client = client;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        /*
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), 1000);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
         */
    }

    @Override
    public void connect() throws GameException {

    }

    @Override
    public String setPlayer(String nickname) throws GameException {
        return null;
    }

    @Override
    public void resetPlayer() throws GameException {

    }

    @Override
    public boolean createGame(String gameId, int numPlayers, int endScore) throws GameException {
        return false;
    }

    @Override
    public boolean joinGame(String gameId) throws GameException {
        return false;
    }

    @Override
    public Map<String, List<String>> getFreeGames() throws GameException {
        return null;
    }

    @Override
    public void selectSecretObjective(int index) throws GameException {

    }

    @Override
    public void selectStarterFace(int face) throws GameException {

    }

    @Override
    public void placeCard(PlaceCardRequest placeCardRequest) throws GameException {

    }

    @Override
    public void drawCard(int position) throws GameException {

    }

    @Override
    public void sendChatMessage(String message) throws GameException {

    }

    @Override
    public void sendPrivateChatMessage(String receiver, String message) throws GameException {

    }

    //////////////////////////////////////////
    //COMUNICATION WITH SERVER
    ///////////////////////////////////////////

    /*
    private void inputThread() {

        executorService.execute(() -> {
            while (!executorService.isShutdown()) {
                try {
                    Object object = input.readObject();
                    Message1 message1 = (Message1) object;
                    queue.add(message1);
                    notifyMessageFromServer();
                    //System.out.println((boolean) message.getObject());


                } catch (IOException | ClassNotFoundException e) {

                }

            }
        });

    }

    private synchronized void setMessageout(Message1.Message1ClientToServer messageout) {
        try {
            output.writeObject(messageout);
            output.flush();
            output.reset();
        } catch (IOException e) {

        }
    }

     */

    /*
    private String createGame(String matchName, int numberOfPlayer) {
        this.matchName = matchName;
        setMessageout(new Message1.Message1ClientToServer(Request.CREATE_GAME, numberOfPlayer, matchName, nickName));
        waitFirstPhase();
        return this.matchName;

    }

    private String enterGame(String matchName) {
        this.matchName = matchName;
        setMessageout(new Message1.Message1ClientToServer(Request.ENTER_GAME, null, matchName, nickName));
        waitFirstPhase();
        return this.matchName;
    }

    private String setName(String name) {
        this.nickName = name;
        setMessageout(new Message1.Message1ClientToServer(Request.SET_NAME, null, null, name));
        waitFirstPhase();
        return this.nickName;
    }

    private ArrayList<String> getFreeMatch() {
        setMessageout(new Message1.Message1ClientToServer(Request.GET_FREE_MATCH, null, null, null));
        waitFirstPhase();
        return this.freeMatch;
    }


    private void placeCard(PlaceCardRequest placeCardRequest) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.PLACE_CARD, placeCardRequest, this.matchName, this.nickName));
        waitMidPhase();
        if (!error) {
            getModel();
            //view.updateBoard();
        }
        error = false;
    }

    private void sendMessage(String message) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.MEX_CHAT, message, this.matchName, this.nickName));

    }

    private void selectStarterFace(int face) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.SELECT_STARTER_FACE, face, this.matchName, this.nickName));
        waitFirstPhase();
    }


    private void selectObjectiveCard(int index) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.SELECT_OBJECTIVE_CARD, index, this.matchName, this.nickName));
        waitFirstPhase();
    }


    private void drawCard(int position) {
        if (this.matchName == null) {
            return;
        }
        if (this.nickName == null) {
            return;
        }
        setMessageout(new Message1.Message1ClientToServer(Request.DRAW_CARD, position, this.matchName, this.nickName));
        waitMidPhase();
        if (!error) {
            getModel();
            //view.updateBoard();
        }
        error = false;
    }

     */
}
