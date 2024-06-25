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

    /**
     * method used to initialize menu controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
    }

    /**
     * method used to handle create new game button
     * @param event an instance of action event
     */
    @FXML
    private void handleCreateNewGameButton(ActionEvent event) {
        guiView.setSubmittedGameChoice(1);
        guiView.resumeExecution();
    }
    /**
     * method used to handle join game button
     * @param event an instance of action event
     */
    @FXML
    private void handleJoinGameButton(ActionEvent event) {
        guiView.setSubmittedGameChoice(2);
        guiView.resumeExecution();
    }
    /**
     * method used to handle quit button
     * @param event an instance of action event
     */
    @FXML
    private void handleQuitButton(ActionEvent event) {
        guiView.setSubmittedGameChoice(3);
        guiView.resumeExecution();
    }
    /**
     * method used to handle rules button
     * @param event an instance of action event
     */
    @FXML
    private void handleRulesButton(ActionEvent event) {
        //showRulesView();
    }

     /*
    public void showCreateGameView() throws IOException {
        Stage stage = (Stage) createNewGameButton.getScene().getWindow();
        FXMLLoader createGameLoader = new FXMLLoader(getClass().getResource(ScenePath.CREATEGAME.getPath()));
        Parent createGameRoot = createGameLoader.load();
        Scene createGameScene = new Scene(createGameRoot);
        createGameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(createGameScene);
    }

    public void showJoinGameView() throws IOException {
        Stage stage = (Stage) joinGameButton.getScene().getWindow();
        FXMLLoader joinGameLoader = new FXMLLoader(getClass().getResource(ScenePath.JOINGAME.getPath()));
        Parent joinGameRoot = joinGameLoader.load();
        Scene joinGameScene = new Scene(joinGameRoot);
        joinGameScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(joinGameScene);
    }

    public void showRulesView() throws IOException {
        Stage stage = (Stage) rulesButton.getScene().getWindow();
        FXMLLoader rulesLoader = new FXMLLoader(getClass().getResource(ScenePath.RULES.getPath()));
        Parent rulesRoot = rulesLoader.load();
        Scene rulesScene = new Scene(rulesRoot);
        rulesScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(rulesScene);
    }

    public void closeApp() {
        Stage stage;
        stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
        stage.setOnCloseRequest(e -> System.exit(0));
    }
     */
}
