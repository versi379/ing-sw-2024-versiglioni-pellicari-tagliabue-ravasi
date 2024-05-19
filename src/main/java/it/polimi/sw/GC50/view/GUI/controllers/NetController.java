package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

public class NetController extends GUIController {


    @FXML
    private Button enterLobbyButton;

    private static NetController instance;
    public NetController() {
        instance = this;
    }
    public static NetController getController() {
        return instance;
    }

    @FXML
    public void initialize() throws Exception {
        netConnection.getItems().addAll("Socket", "RMI");
        netConnection.setOnAction((event) -> {
            netChoice = netConnection.getSelectionModel().getSelectedIndex();
        });
    }

    @FXML
    public void handleEnterLobbyButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) enterLobbyButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.MENU.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public String getPlayerNickname() {
        return playerNickname.getText();
    }

    public int getNetChoice() {
        return netChoice;
    }

}
