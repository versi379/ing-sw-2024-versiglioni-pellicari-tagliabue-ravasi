package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class UserController {

    @FXML
    private TextField playerNickname;

    @FXML
    private Button menuButton;

    private boolean nameSetted;

    @FXML
    public void initialize() throws Exception {
        nameSetted = false;
        // qui il nome non è null
        menuButton.setOnAction(event -> {
            nameSetted = true;
        });
    }

    @FXML
    public void handleMenuButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) menuButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.MENU.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public String getPlayerNickname() {
        return playerNickname.getText();
    }

    public boolean isNameSetted() {
        return nameSetted;
    }

}
