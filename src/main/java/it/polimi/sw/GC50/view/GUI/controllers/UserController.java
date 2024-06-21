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

    @FXML
    private TextField playerNickname;

    @FXML
    private Button menuButton;

    private GuiView guiView;

    @FXML
    public void initialize() throws Exception {
        guiView = (GuiView) AppClient.getView();
    }

//    public void showMenuView() throws Exception{
//        Stage stage = (Stage) menuButton.getScene().getWindow();
//        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource(ScenePath.MENU.getPath()));
//        Parent menuRoot = menuLoader.load();
//        Scene menuScene = new Scene(menuRoot);
//        menuScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
//        stage.setScene(menuScene);
//    }

    @FXML
    public void handleMenuButton(ActionEvent event) throws Exception {
        String submittedPlayerName = playerNickname.getText();
        guiView.setSubmittedPlayerNickname(submittedPlayerName);
        guiView.resumeExecution();
        System.out.println("Riprendo client Thread");
    }

}
