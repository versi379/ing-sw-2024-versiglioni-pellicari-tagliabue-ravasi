package it.polimi.sw.GC50.view.GUI.controllers;

import it.polimi.sw.GC50.app.AppClient;
import it.polimi.sw.GC50.view.GUI.GuiView;
import it.polimi.sw.GC50.view.GUI.scenes.ScenePath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for Net FXML scene.
 */
public class NetController {

    private GuiView guiView;

    @FXML
    private Button socketButton;

    @FXML
    private Button rmiButton;

    @FXML
    private Button quitButton;

    private boolean netSet;
    private int netSelected;

    @FXML
    private TextField serverIpTextField;

    /**
     * method that initialize net controller
     * @throws Exception    if there is some error
     */
    @FXML
    public void initialize() throws Exception {
        guiView = (GuiView) AppClient.getView();
        netSet = false;
        netSelected = 1;
        socketButton.setOnAction(event -> {
            netSet = true;
            setIP();
            netSelected = 1;
            System.out.println("Scelta Socket, carico login view...");
            try {
                showUserView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        rmiButton.setOnAction(event -> {
            netSet = true;
            setIP();
            netSelected = 2;
            System.out.println("Scelta RMI, carico login view...");
            try {
                showUserView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        quitButton.setOnAction(event -> {
            netSet = true;
            netSelected = 3;
            System.out.println("Scelta QUIT");
            try {
                showUserView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * method that sets an IP address
     */
    private void setIP() {
        guiView.setSubmittedIp(serverIpTextField.getText());
        guiView.resumeExecution();
    }

    /**
     * method that shows user view
     * @throws IOException  if an error occurs
     */
    public void showUserView() throws IOException {
        Stage stage = (Stage) socketButton.getScene().getWindow();
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource(ScenePath.USER.getPath()));
        Parent userRoot = userLoader.load();
        Scene userScene = new Scene(userRoot);
        userScene.getStylesheets().addAll(getClass().getResource("/scenes/standard.css").toExternalForm());
        stage.setScene(userScene);
    }

    /**
     * @return socket button
     */
    public Button getSocketButton() {
        return socketButton;
    }
    /**
     * @return RMI button
     */
    public Button getRmiButton() {
        return rmiButton;
    }

    /**
     * @return netSet
     */
    public boolean isNetSet() {
        return netSet;
    }

    /**
     * @return an int that specify the net selected
     */
    public int getNetSelected() {
        return netSelected;
    }
}
