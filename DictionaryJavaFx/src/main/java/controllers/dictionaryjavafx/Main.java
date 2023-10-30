package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Dictionary");
        SceneLoaderController.setStage(stage);
        SceneLoaderController. loadScene(FXMLFiles.LOADING_SCENE);
        stage.show();

        LoadingSceneController.onLaunch();
    }

    public static void main(String[] args) {
        launch();
    }
}