package it.polimi.sw.GC50.view.GUI.controllers;

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

public class UserConnectionController extends GUIController {

    @FXML
    private TextField playerNickname;

    @FXML
    private ChoiceBox<String> netConnection;

    @FXML
    private Button enterLobbyButton;

    @FXML
    public void initialize() throws Exception {
        netConnection.getItems().addAll("Socket", "RMI");
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

    public int getNetChoice() {
        return netConnection.getSelectionModel().getSelectedItem().equals("Socket") ? 1 : 2;
    }

    public String getPlayerNickname() {
        return playerNickname.getText();
    }
}
