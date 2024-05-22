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
    private Button netMenuButton;

    @FXML
    private Button menuButton;

    BlockingQueue<String> userInputQueue = new ArrayBlockingQueue<>(1);

    @FXML
    public void initialize() throws Exception {

    }

    @FXML
    public void handleNetMenuButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) netMenuButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.NET.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
        userInputQueue.offer(playerNickname.getText());
        System.out.println(playerNickname.getText());
    }

    public String getPlayerNickname() {
        return playerNickname.getText();
    }

    // Method to get user input from the queue
    public String getUserInput() throws InterruptedException {
        return userInputQueue.take();
    }

}
