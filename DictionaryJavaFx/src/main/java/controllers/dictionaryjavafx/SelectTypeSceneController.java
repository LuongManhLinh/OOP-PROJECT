package controllers.dictionaryjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class SelectTypeSceneController {
    @FXML private Button useCommandlineButton;
    @FXML private Button useAppButton;

    @FXML private AnchorPane pane;

    public static boolean isUsingCommandline = false;

    public static void setIsUsingCommandline(boolean isUsingCommandline) {
        SelectTypeSceneController.isUsingCommandline = isUsingCommandline;
    }

    public void useApp(ActionEvent event) throws IOException {
        SceneLoaderController.loadScene(event, "MainUIScene.fxml");
        isUsingCommandline = false;
    }
}
