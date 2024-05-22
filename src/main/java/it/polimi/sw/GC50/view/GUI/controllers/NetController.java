package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.net.RMI.ClientRmi;
import it.polimi.sw.GC50.net.socket.ClientSCK;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import it.polimi.sw.GC50.view.View;
import it.polimi.sw.GC50.view.ViewType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

public class NetController extends GUIController {

    @FXML
    private Button socketButton;

    @FXML
    private Button rmiButton;

    private boolean netSelected;

    @FXML
    public void initialize() throws Exception {
        netSelected = false;
    }

    @FXML
    public void handleSocketButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) socketButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.USER.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        AppClient.setupSocket(AppClient.getView(), AppClient.getViewType());
        netSelected = true;
    }

    @FXML
    public void handleRmiButton(ActionEvent event) throws Exception {
        Stage stage;
        Scene scene;
        Parent root;
        stage = (Stage) rmiButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(ScenePath.USER.getPath()));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        AppClient.setupRMI(AppClient.getView(), AppClient.getViewType());
        netSelected = true;
    }

    public Button getSocketButton() {
        return socketButton;
    }

    public Button getRmiButton() {
        return rmiButton;
    }

    public boolean isNetSelected() {
        return netSelected;
    }
}
