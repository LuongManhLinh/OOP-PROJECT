package com.example.dictionaryjavafxtest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectTypeSceneController {
    @FXML private Button useCommandlineButton;
    @FXML private Button useAppButton;

    @FXML private AnchorPane pane;

    public static boolean isUsingCommandline = false;

    public void useCommandline(ActionEvent event) {
        SceneLoaderController.exitApp(event);
        isUsingCommandline = true;
    }

    public void useApp(ActionEvent event) throws IOException {
        SceneLoaderController.loadScene(event, "MainUIScene.fxml");
        isUsingCommandline = false;
    }
}
