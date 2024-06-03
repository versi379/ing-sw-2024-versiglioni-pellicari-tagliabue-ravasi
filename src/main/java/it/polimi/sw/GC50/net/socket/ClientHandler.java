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

/**
 * class that represents the client handler
 * it is the class that handles the client socket
 * it implements the runnable interface
 * it implements the client interface
 */
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

    /**
     * method that creates a new client handler
     *
     * @param socketClient is the socket of the client
     * @param serverSCK    is the server socket
     * @param lobby        is the lobby of the server
     */
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

    /**
     * Method that waits for a mesaage from the client socket
     * after it receives the message it calls the switchMex method
     *
     * @throws IOException            if an error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
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

    /**
     * method that switch the message received from the client
     *
     * @param message is the message received from the client
     *                it can be a command or a lobby command
     *                if it is a command it calls the method related to the command
     *                if it is a lobby command it calls the method related to the lobby command
     *                if the message is not a command or a lobby command it does nothing
     *                if an exception is thrown it does nothing
     */
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

    /**
     * Method that sends a message to the client socket
     *
     * @param messageout is the message that has to be sent to the client
     */

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

    /**
     * Method that sets the player in the lobby
     * it calls the addPlayer method of the lobby
     * and sends a message to the client socket
     * with the name of the player
     * if the player is already in the lobby it does nothing
     * if an exception is thrown it does nothing
     *
     * @param nickname is the nickname of the player
     *                 it is the name that has to be set in the lobby
     */
    private void setPlayer(String nickname) {
        String name = lobby.addPlayer(this, nickname);
        setMessageout(new SocketMessage(Notify.NOTIFY_NAME_SET, new ObjectMessage(name)));
    }

    /**
     * method that creates a game in the lobby
     * it calls the createGame method of the lobby
     * and sends a message to the client socket
     * with the id of the game
     * if the game is created it sends a message with the id of the game
     * if the game is not created it sends a message with a null object
     * if an exception is thrown it does nothing
     * if the game is already created it does nothing returns null
     *
     * @param gameId     is the id of the game
     * @param numPlayers is the number of players in the game
     * @param endScore   is the score to reach to end the game
     */
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

    /**
     * method that joins a game in the lobby
     * it calls the joinGame method of the lobby
     * and sends a message to the client socket
     * with the id of the game
     * if the game is joined it sends a message with the id of the game
     * if the game is not joined it sends a message with a null object
     * if an exception is thrown it does nothing
     * if the game is already joined it does nothing returns null
     * if the game is not found it does nothing returns null
     * if the game is full it does nothing returns null
     * if the game is already started it does nothing returns null
     * if the game is not found it does nothing returns null
     *
     * @param gameId is the id of the game
     */
    private void joinGame(String gameId) {
        gameController = lobby.joinGame(this, gameId);
        if (gameController != null) {
            setMessageout(new SocketMessage(Notify.NOTIFY_GAME_JOINED, new ObjectMessage(gameId)));
        } else {
            setMessageout(new SocketMessage(Notify.NOTIFY_GAME_JOINED, new ObjectMessage(null)));
        }
    }

    /**
     * method that gets the free games in the lobby
     * it calls the getFreeGames method of the lobby
     * and sends a message to the client socket
     * with the free games
     * if an exception is thrown it does nothing
     * if the free games are not found it does nothing
     * if the free games are found it sends a message with the free games
     */
    private void getFreeGames() {
        Map<String, List<String>> freeGame = lobby.getFreeGames();
        setMessageout(new SocketMessage(Notify.NOTIFY_FREE_GAMES, new ObjectMessage(freeGame)));
    }

    // SETUP ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that selects the secret objective of the player
     * it calls the selectSecretObjective method of the gameController
     * if an exception is thrown it does nothing
     *
     * @param index is the index of the secret objective
     */
    private void selectSecretObjective(int index) {
        try {
            gameController.selectSecretObjective(this, index);
        } catch (RemoteException e) {

        }
    }

    /**
     * method that selects the starter face of the player
     * it calls the selectStarterFace method of the gameController
     * if an exception is thrown it does nothing
     *
     * @param face is the face of the starter
     */
    private void selectStarterFace(int face) {
        try {
            gameController.selectStarterFace(this, face);
        } catch (RemoteException e) {

        }
    }

    // PLAYING /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that places a card in the game
     * it calls the placeCard method of the gameController
     * if an exception is thrown it does nothing
     *
     * @param placeCardRequest is the request to place the card
     *                         it contains the card and the position
     *                         where the card has to be placed
     */
    private void placeCard(PlaceCardRequest placeCardRequest) {
        try {
            gameController.placeCard(this, placeCardRequest);
        } catch (RemoteException e) {

        }
    }

    /**
     * method that draws a card in the game
     * it calls the drawCard method of the gameController
     * if an exception is thrown it does nothing
     *
     * @param position is the position of the card
     */
    private void drawCard(int position) {
        try {
            gameController.drawCard(this, position);
        } catch (RemoteException e) {

        }
    }

    /**
     * method that sends a chat message in the game
     * it calls the sendChatMessage method of the gameController
     * if an exception is thrown it does nothing
     *
     * @param message is the message that has to be sent
     */
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

    /**
     * method that updates the client handler
     * it calls the setMessageout method
     *
     * @param notify  is the notify that has to be sent
     * @param message is the message that has to be sent
     */
    @Override
    public void update(Notify notify, Message message) {
        setMessageout(new SocketMessage(notify, message));
    }
}
