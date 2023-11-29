package controllers.dictionaryjavafx;

import classes.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoaderController {
    private static Stage stage;

    public static void setStage(Stage stage) {
        stage.setResizable(false);
        SceneLoaderController.stage = stage;
        stage.setOnCloseRequest(event -> {
            EnViBookMark.writeBookMark();
            ViEnBookMark.writeBookMark();
            EnViDictionary.getInstance().exit();
            ViEnDictionary.getInstance().exit();
        });
    }

    public static Stage getStage() {
        return stage;
    }

    public static void exitApp() {
        EnViBookMark.writeBookMark();
        ViEnBookMark.writeBookMark();
        EnViDictionary.getInstance().exit();
        ViEnDictionary.getInstance().exit();
        stage.close();
    }

    public static void loadScene(String dotFxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(dotFxmlFile));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}
