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
        menuButton.setOnAction(event -> {

            nameSetted = true;
            try {
                showMenuView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void showMenuView() throws Exception{
        Stage stage = (Stage) menuButton.getScene().getWindow();
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.MENU.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        stage.setScene(userScene);
    }

//    @FXML
//    public void handleMenuButton(ActionEvent event) throws Exception {
//        nameSetted = true;
//        showMenuView();
//    }

    public String getPlayerNickname() {
        return playerNickname.getText();
    }

    public boolean isNameSetted() {
        return nameSetted;
    }

    public Button getMenuButton() {
        return menuButton;
    }
}
