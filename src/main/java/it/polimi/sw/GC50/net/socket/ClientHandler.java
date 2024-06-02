package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.model.lobby.Lobby;
import it.polimi.sw.GC50.net.Messages.CreateGameMessage;
import it.polimi.sw.GC50.net.Messages.Message;
import it.polimi.sw.GC50.net.Messages.ObjectMessage;
import it.polimi.sw.GC50.net.Messages.SocketMessage;
import it.polimi.sw.GC50.net.util.*;
import trash.Message1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements Runnable, ClientInterface {
    private final Socket socketClient;
    private final ServerSCK serverSCK;
    //////////////////////////////////////////
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    //////////////////////////////////////////
    private boolean alive;
    //////////////////////////////////////////
    //////////////////////////////////////////
    private final Lobby lobby;
    private GameControllerRemote gameController;
    private String nickname;
    //////////////////////////////////////////
    private final ExecutorService executorService;
    //////////////////////////////////////////


    public ClientHandler(Socket socketClient, ServerSCK serverSCK, Lobby lobby) {
        this.socketClient = socketClient;
        this.serverSCK = serverSCK;
        this.alive = true;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.lobby = lobby;
        try {
            this.output = new ObjectOutputStream(socketClient.getOutputStream());
            this.input = new ObjectInputStream(socketClient.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void inputThread() {
        System.out.println("server socket listener client");
        executorService.execute(() -> {
            while (true) {
                try {
                    SocketMessage message = (SocketMessage) input.readObject();
                    System.out.println(message.getCommand());
                    switchMex(message);
                } catch (IOException | ClassNotFoundException e) {

                }
            }
        });
    }

    private synchronized void switchMex(SocketMessage message) {

        if (message.getCommand() != null) {
            switch (message.getCommand()) {
                case CHOOSE_OBJECTIVE -> {
                    ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                    this.selectSecretObjective((int) objectMessage.getObject());
                }
                case CHOOSE_STARTER_FACE -> {
                    ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                    this.selectStarterFace((int) objectMessage.getObject());
                }
                case PLACE_CARD -> {
                    ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                    PlaceCardRequest placeCardRequest = objectMessage.getPlaceCardRequest();
                    this.placeCard(placeCardRequest);
                }
                case DRAW_CARD -> {
                    ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                    this.drawCard((int) objectMessage.getObject());
                }
                case CHAT, CHAT_PRIVATE -> {
                    ChatMessageRequest chatMessageRequest = (ChatMessageRequest) message.getMessage();
                    this.sendChatMessage(chatMessageRequest);
                }
                case HELP -> {

                }
                case NOT_A_COMMAND -> {

                }
            }
        } else if (message.getLobbyCommand() != null) {
            switch (message.getLobbyCommand()) {
                case JOIN_GAME -> {
                    ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                    this.joinGame((String) objectMessage.getObject());
                }
                case CREATE_GAME -> {
                    CreateGameMessage game = (CreateGameMessage) message.getMessage();
                    this.createGame(game.getGameId(), game.getNumPlayers(), game.getEndScore());
                }
                case LIST_FREE_GAMES -> {
                    this.getFreeGames();
                }
                case SET_PLAYER_NAME -> {
                    ObjectMessage objectMessage = (ObjectMessage) message.getMessage();
                    this.setPlayer((String) objectMessage.getObject());
                }
                case RESET_PLAYER -> {
                    lobby.removePlayer(this);
                }
            }
        }
    }

    synchronized private void setMessageout(SocketMessage messageout) {
        try {
            if (messageout != null) {
                output.writeObject(messageout);
                output.flush();
                output.reset();
            }
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    // LOBBY ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setPlayer(String nickname) {
        String name = lobby.addPlayer(this, nickname);
        setMessageout(new SocketMessage(Notify.NOTIFY_NAME_SET, new ObjectMessage(name)));
    }

    private void createGame(String gameId, int numPlayers, int endScore) {
        try {
            gameController = lobby.createGame(this, gameId, numPlayers, endScore);
            if (gameController != null) {
                setMessageout(new SocketMessage(Notify.NOTIFY_GAME_CREATED, new ObjectMessage(gameId)));
                System.out.println("Game " + gameId + " created");
            } else {
                setMessageout(new SocketMessage(Notify.NOTIFY_GAME_CREATED, new ObjectMessage(null)));
                System.out.println("Game " + gameId + " not created");
            }
        } catch (RemoteException e) {

        }
    }

    private void joinGame(String gameId) {
        gameController = lobby.joinGame(this, gameId);
        if (gameController != null) {
            setMessageout(new SocketMessage(Notify.NOTIFY_GAME_JOINED, new ObjectMessage(gameId)));
        } else {
            setMessageout(new SocketMessage(Notify.NOTIFY_GAME_JOINED, new ObjectMessage(null)));
        }
    }

    private void getFreeGames() {
        Map<String, List<String>> freeGame = lobby.getFreeGames();
        setMessageout(new SocketMessage(Notify.NOTIFY_FREE_GAMES, new ObjectMessage(freeGame)));
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void selectSecretObjective(int index) {
        try {
            gameController.selectSecretObjective(this, index);
        } catch (RemoteException e) {

        }
    }

    private void selectStarterFace(int face) {
        try {
            gameController.selectStarterFace(this, face);
        } catch (RemoteException e) {

        }
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void placeCard(PlaceCardRequest placeCardRequest) {
        try {
            gameController.placeCard(this, placeCardRequest);
        } catch (RemoteException e) {

        }
    }



    private void drawCard(int position) {
        try {
            gameController.drawCard(this, position);
        } catch (RemoteException e) {

        }
    }

    private void sendChatMessage(ChatMessageRequest message) {
        try {
            gameController.sendChatMessage(this, message);
        } catch (RemoteException e) {

        }
    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void run() {
        Thread thread1 = new Thread(() -> {
            inputThread();
        });
        thread1.start();
    }

    //////////////////////////////////////////
    //OBSERVER
    ///////////////////////////////////////////
    @Override
    public void update(Notify notify, Message message) {
        setMessageout(new SocketMessage(notify, message));
    }
}
