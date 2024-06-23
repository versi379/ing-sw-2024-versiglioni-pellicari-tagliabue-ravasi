package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserController {
    private GuiView guiView;

    @FXML
    private TextField playerNickname;

    @FXML
    private Button menuButton;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
    }

    @FXML
    private void handleMenuButton(ActionEvent event) {
        String submittedPlayerNickname = playerNickname.getText();

        guiView.setSubmittedPlayerNickname(submittedPlayerNickname);
        guiView.resumeExecution();
    }

    /*
    public void showMenuView() throws Exception{
        Stage stage = (Stage) menuButton.getScene().getWindow();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource(ScenePath.MENU.getPath()));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot);
        menuScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(menuScene);
    }

     */
}
