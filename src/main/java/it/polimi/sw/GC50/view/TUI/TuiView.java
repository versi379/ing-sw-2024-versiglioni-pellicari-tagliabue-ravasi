package it.polimi.sw.GC50.view.TUI;

import it.polimi.sw.GC50.model.card.PhysicalCard;
import it.polimi.sw.GC50.model.chat.Chat;
import it.polimi.sw.GC50.model.game.DrawingPosition;
import it.polimi.sw.GC50.model.objective.*;
import it.polimi.sw.GC50.net.gameMexNet.PlaceCardMex;
import it.polimi.sw.GC50.view.GameView;
import it.polimi.sw.GC50.view.View;

import java.util.*;

public class TuiView implements View {
    GameView gameView;

    public TuiView() {
    }

    public void start() {
    }

    public void showEndSession() {
        System.out.println();
        System.out.println("Sessione terminata");
    }

    @Override
    public String selectName() {
        return readString("Inserire il nome del giocatore:");
    }

    @Override
    public int selectJoinOrCreate() {
        return readInt("Vuoi creare una nuova partita o entrare in una esistente?" +
                "\n1) crea una partita" +
                "\n2) entra in una partita" +
                "\n3) abbandona",
                1, 3);
    }

    @Override
    public void showFreeGames(Map<String, List<String>> freeGames) {
        if (freeGames.isEmpty()) {
            System.out.println("No free games");
        } else {
            System.out.println("Free games:");
            for (String game : freeGames.keySet()) {
                System.out.print("Game " + game + ", current players:");
                for (String nickname : freeGames.get(game)) {
                    System.out.print(" " + nickname);
                }
                System.out.println();
            }
        }
    }

    @Override
    public String selectGameName() {
        return readString("Inserire il nome della partita:");
    }

    @Override
    public int selectNumberOfPlayers() {
        return readInt("Inserire il numero di giocatori:",
                2, 3);
    }

    @Override
    public void showPlayerJoined(String nickname) {
        System.out.println();
        System.out.println("Giocatore " + nickname + " è entrato in partita");
    }

    @Override
    public void showPlayerLeft(String nickname) {
        System.out.println();
        System.out.println("Giocatore " + nickname + " ha abbandonato la partita");
    }

    @Override
    public void showWaitPlayers() {
        System.out.println();
        System.out.println("In attesa che gli altri giocatori entrino in partita...");
    }

    @Override
    public void showSetup() {
        System.out.println();
        System.out.println("Tutti i giocatori sono entrati, la partita sta iniziando");
        showCommonObjectives();
    }

    @Override
    public void showCommonObjectives() {
        System.out.println();
        System.out.println("Carte obiettivo comuni:");
        List<ObjectiveCard> commonObjectives = gameView.getCommonObjectives();
        for (int i = 0; i < commonObjectives.size(); i++) {
            System.out.println();
            System.out.println((commonObjectives.get(i).toStringTUI()));
        }
    }

    @Override
    public int selectObjectiveCard() {
        System.out.println();
        System.out.println("Selezione della carta obiettivo segreta");
        printSecretObjectiveChoice();

        return readInt("Inserire l'indice della carta obiettivo con cui si vuole giocare:",
                1, gameView.getSecreteObjectivesList().size()) - 1;
    }

    private void printSecretObjectiveChoice() {
        List<ObjectiveCard> objectiveCards = gameView.getSecreteObjectivesList();
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println();
            System.out.println((i + 1) + ") " + objectiveCards.get(i).toStringTUI());
        }
    }

    @Override
    public boolean selectStarterFace() {
        System.out.println();
        System.out.println("Selezione della faccia della carta iniziale");
        printStarterCardChoice(gameView.getStarterCard());

        return readInt("Inserire l'indice della faccia con cui si vuole giocare:",
                1, 2) == 1;
    }

    private void printStarterCardChoice(PhysicalCard card) {
        String[][] cardTUI;

        System.out.println();
        System.out.println("1) Fronte");
        cardTUI = card.getFront().toStringTUI();
        for (int i = cardTUI[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cardTUI.length; j++) {
                System.out.print(cardTUI[j][i]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("2) Retro");
        cardTUI = card.getBack().toStringTUI();
        for (int i = cardTUI[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cardTUI.length; j++) {
                System.out.print(cardTUI[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void showPlayerReady(String nickname) {
        System.out.println();
        System.out.println("Giocatore " + nickname + " pronto");
    }

    @Override
    public PlaceCardMex selectPlaceCard() {
        System.out.println();
        System.out.println("Piazzamento di una carta");
        showPlayerArea(gameView.getPlayerNickname());
        ModelPrinter.printHand(gameView.getHand());
        return new PlaceCardMex(
                readInt("Selezionare l'indice della carta da piazzare:",
                        1, gameView.getHand().size()) - 1,
                readInt("Selezionare la faccia della carta:",
                        1, 2) == 1,
                readInt("Inserire il valore della coordinata x:",
                        1, gameView.getPlayerArea(gameView.getPlayerNickname())
                        .getCardsMatrix().length()) - 1,
                readInt("Inserire il valore della coordinata y:",
                        1,gameView.getPlayerArea(gameView.getPlayerNickname())
                        .getCardsMatrix().length()) - 1);
    }

    @Override
    public void showPlayerArea(String nickname) {
        ModelPrinter.printPlayerArea(nickname, gameView.getPlayerArea(nickname));
    }

    @Override
    public DrawingPosition selectDrawingPosition() {
        System.out.println();
        System.out.println("Pescaggio di una carta");
        showDecks();
        return DrawingPosition.values()[readInt("Selezionare la carta da pescare:",
                1, DrawingPosition.values().length) - 1];
    }

    @Override
    public void showDecks() {
        ModelPrinter.printDecks(gameView.getDecks());
    }

    @Override
    public void showScores() {
        Map<String, Integer> scores = new HashMap<>();
        for (String nickname : gameView.getPlayersList()) {
            scores.put(nickname, gameView.getPlayerArea(nickname).getTotalScore());
        }
        ModelPrinter.printScores(scores);
    }

    @Override
    public void addModel(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void updateChat(Chat chat) {
    }

    @Override
    public int game() {
        return 0;
    }

    @Override
    public void updateBoard() {
    }

    @Override
    public void showError() {
        System.out.println();
        System.out.println("Errore");
    }

    private static String readString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        String read;
        try {
            System.out.print("> ");
            read = scanner.nextLine();
        } catch (InputMismatchException e) {
            read = null;
            scanner.nextLine();
        }
        while (read == null) {
            System.out.println("Invalid input. Please retry");
            try {
                System.out.print("> ");
                read = scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
        return read;
    }

    private static int readInt(String message, int min, int range) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);

        int read;
        try {
            System.out.print("> ");
            read = scanner.nextInt();
        } catch (InputMismatchException e) {
            read = min - 1;
            scanner.nextLine();
        }
        while (read < min || read >= min + range) {
            System.out.println("Invalid input. Please enter a number between " + min + " and " + (min + range - 1));
            try {
                System.out.print("> ");
                read = scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
            }
        }
        return read;
    }

    @Override
    public void showMessage(String message) {
        System.out.println();
        System.out.println(message);
    }
}
