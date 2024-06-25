package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller for Enter IP FXML scene.
 */
public class EnterIPController {

    private GuiView guiView;

    @FXML
    public TextField ipTextField;

    @FXML
    private Button ipSubmitButton;

    /**
     * method used to initialize controller
     */
    @FXML
    public void initialize() {
        guiView = (GuiView) AppClient.getView();
    }

    /**
     * method used to handle IP submit button
     * @param event an instance of action event
     */
    @FXML
    private void handleIPSubmitButton(ActionEvent event) {
        guiView.setSubmittedIp(ipTextField.getText());
        guiView.resumeExecution();
    }
}
