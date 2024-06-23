package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.IOException;

/**
 * Controller for Menu FXML scene.
 */
public class MenuController {
    private GuiView guiView;

    @FXML
    private Button createNewGameButton;

    @FXML
    private Button joinGameButton;

    @FXML
    private Button rulesButton;

    @FXML
    private Button quitButton;

    private int gameChoice;


    public ObservableList<String> gameItems2 = FXCollections.observableArrayList();

    /**
     * method that initialize menu controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
    }

    /**
     * method that shows create game view
     * @throws IOException when an input/output error occurs
     */
    public void showCreateGameView() throws IOException {
        Stage stage = (Stage) createNewGameButton.getScene().getWindow();
        FXMLLoader createGameLoader = new FXMLLoader(getClass().getResource(ScenePath.CREATEGAME.getPath()));
        Parent createGameRoot = createGameLoader.load();
        Scene createGameScene = new Scene(createGameRoot);
        createGameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(createGameScene);
    }

    /**
     * method that shows join game view
     * @throws IOException when an input/output error occurs
     */
    public void showJoinGameView() throws IOException {
        Stage stage = (Stage) joinGameButton.getScene().getWindow();
        FXMLLoader joinGameLoader = new FXMLLoader(getClass().getResource(ScenePath.JOINGAME.getPath()));
        Parent joinGameRoot = joinGameLoader.load();
        Scene joinGameScene = new Scene(joinGameRoot);
        joinGameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(joinGameScene);
    }

    /**
     * method that shows rules view
     * @throws IOException when an input/output error occurs
     */
    public void showRulesView() throws IOException {
        Stage stage = (Stage) rulesButton.getScene().getWindow();
        FXMLLoader rulesLoader = new FXMLLoader(getClass().getResource(ScenePath.RULES.getPath()));
        Parent rulesRoot = rulesLoader.load();
        Scene rulesScene = new Scene(rulesRoot);
        rulesScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(rulesScene);
    }

    /**
     * method that create new game button
     * @param event             instance of action event
     * @throws IOException      when an input/output error occurs
     */
    @FXML
    public void handleCreateNewGameButton(ActionEvent event) throws IOException {
        gameChoice = 1;
        showCreateGameView();
        guiView.setSubmittedGameChoice(gameChoice);
        guiView.resumeExecution();
    }

    /**
     * method that handle join game button
     * @param event         instance of action event
     * @throws IOException  when an input/output error occurs
     */
    @FXML
    public void handleJoinGameButton(ActionEvent event) throws IOException {
        gameChoice = 2;
        guiView.setSubmittedGameChoice(gameChoice);
        guiView.resumeExecution();
        showJoinGameView();
    }

    /**
     * method that handle rules button
     * @param event         instance of action event
     * @throws IOException  when an input/output error occurs
     */
    @FXML
    public void handleRulesButton(ActionEvent event) throws IOException {
        showRulesView();
    }
    /**
     * method that handle quit button
     * @param event         instance of action event
     */
    @FXML
    public void handleQuitButton(ActionEvent event) {
        gameChoice = 3;
        guiView.setSubmittedGameChoice(gameChoice);
        guiView.resumeExecution();
        closeApp();
    }

    /**
     * method that close the application
     */
    public void closeApp() {
        Stage stage;
        stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
        stage.setOnCloseRequest(e -> System.exit(0));
    }

    /**
     * @return game choice (GUI/TUI)
     */
    public int getGameChoice() {
        return gameChoice;
    }
}
