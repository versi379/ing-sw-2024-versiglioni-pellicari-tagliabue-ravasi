package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class EnterIPController {

    private GuiView guiView;

    @FXML
    public TextField ipTextField;

    @FXML
    private Button ipSubmitButton;

    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
    }

    @FXML
    private void handleIPSubmitButton(ActionEvent event) {
        guiView.setSubmittedIp(ipTextField.getText());
        guiView.resumeExecution();
    }
}
