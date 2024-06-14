package it.polimi.sw.GC50.net.socket;

import it.polimi.sw.GC50.controller.GameControllerRemote;
import it.polimi.sw.GC50.model.lobby.Lobby;
import it.polimi.sw.GC50.net.Messages.FreeGamesMex;
import it.polimi.sw.GC50.net.Messages.StringMex;
import it.polimi.sw.GC50.net.Requests.ChatMessageRequest;
import it.polimi.sw.GC50.net.Requests.CreateGameRequest;
import it.polimi.sw.GC50.net.Messages.Message;
import it.polimi.sw.GC50.net.Requests.PlaceCardRequest;
import it.polimi.sw.GC50.net.util.*;

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

    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final ExecutorService executorService;

    /////////////////////////////////////////////////////////////////
    private final Lobby lobby;
    private GameControllerRemote gameController;

    /**
     * method that creates a new client handler
     *
     * @param socketClient is the socket of the client
     * @param lobby        is the lobby of the server
     */
    public ClientHandler(Socket socketClient, Lobby lobby) {
        this.lobby = lobby;
        executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            output = new ObjectOutputStream(socketClient.getOutputStream());
            input = new ObjectInputStream(socketClient.getInputStream());
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
    @Override
    public void run() {
        executorService.execute(() -> {
            while (true) {
                try {
                    CommandMessage message = (CommandMessage) input.readObject();
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
    private synchronized void switchMex(CommandMessage message) {
        switch (message.getCommand()) {

            case SET_PLAYER -> {
                setPlayer((String) message.getContent());
            }

            case RESET_PLAYER -> {
                resetPlayer();
            }

            case CREATE_GAME -> {
                CreateGameRequest game = (CreateGameRequest) message.getContent();
                createGame(game.getGameId(), game.getNumPlayers(), game.getEndScore());
            }

            case JOIN_GAME -> {
                joinGame((String) message.getContent());
            }

            case LIST_FREE_GAMES -> {
                getFreeGames();
            }

            case CHOOSE_OBJECTIVE -> {
                selectSecretObjective((int) message.getContent());
            }

            case CHOOSE_STARTER_FACE -> {
                selectStarterFace((int) message.getContent());
            }

            case PLACE_CARD -> {
                placeCard((PlaceCardRequest) message.getContent());
            }

            case DRAW_CARD -> {
                drawCard((int) message.getContent());
            }

            case CHAT -> {
                sendChatMessage((ChatMessageRequest) message.getContent());
            }

            case Command.LEAVE -> {
                leaveGame();
            }
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
        nickname = lobby.addPlayer(this, nickname);
        setMessageOut(new NotifyMessage(Notify.NOTIFY_NAME_SET, new StringMex(nickname)));
    }

    private void resetPlayer() {
        lobby.removePlayer(this);
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
        gameController = lobby.createGame(this, gameId, numPlayers, endScore);
        if (gameController != null) {
            setMessageOut(new NotifyMessage(Notify.NOTIFY_GAME_CREATED, new StringMex(gameId)));
        } else {
            setMessageOut(new NotifyMessage(Notify.NOTIFY_GAME_CREATED, new StringMex(null)));
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
            setMessageOut(new NotifyMessage(Notify.NOTIFY_GAME_JOINED, new StringMex(gameId)));
        } else {
            setMessageOut(new NotifyMessage(Notify.NOTIFY_GAME_JOINED, new StringMex(null)));
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
        Map<String, List<String>> freeGames = lobby.getFreeGames();
        setMessageOut(new NotifyMessage(Notify.NOTIFY_FREE_GAMES, new FreeGamesMex(freeGames)));
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
        } catch (RemoteException ignored) {
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
        } catch (RemoteException ignored) {
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
        } catch (RemoteException ignored) {
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
        } catch (RemoteException ignored) {
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
        } catch (RemoteException ignored) {
        }
    }

    private void leaveGame() {
        try {
            gameController.leaveGame(this);
        } catch (RemoteException ignored) {
        }
    }

    @Override
    public void ping() throws RemoteException {

    }

    // OBSERVER ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * method that updates the client handler
     * it calls the setMessageOut method
     *
     * @param notify  is the notify that has to be sent
     * @param message is the message that has to be sent
     */
    @Override
    public void update(Notify notify, Message message) {
        setMessageOut(new NotifyMessage(notify, message));
    }

    /**
     * Method that sends a message to the client socket
     *
     * @param messageOut is the message that has to be sent to the client
     */
    synchronized private void setMessageOut(NotifyMessage messageOut) {
        try {
            if (messageOut != null) {
                output.writeObject(messageOut);
                output.flush();
                output.reset();
            }
        } catch (IOException e) {
            System.out.println("Connection error");
        }
    }
}
