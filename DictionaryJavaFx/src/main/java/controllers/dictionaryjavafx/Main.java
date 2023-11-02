package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("TỪ ĐIỂN HCL");
        SceneLoaderController.setStage(stage);
        SceneLoaderController. loadScene(FXMLFiles.LOADING_SCENE);
        stage.show();

        try {
            InputStream iconInput = new FileInputStream("src/main/resources/image/icon.png");
            Image icon = new Image(iconInput);
            stage.getIcons().add(icon);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LoadingSceneController.onLaunch();
    }

    public static void main(String[] args) {
        launch();
    }
}