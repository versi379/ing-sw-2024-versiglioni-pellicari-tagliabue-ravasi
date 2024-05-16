package it.polimi.sw.GC50.view.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class UserConnectionController extends GUIController {

    @FXML
    private TextField playerNickname;

    @FXML
    private ChoiceBox<String> netConnection;

    @FXML
    public void initialize() throws Exception {
        netConnection.getItems().addAll("Socket", "RMI");
    }



}
