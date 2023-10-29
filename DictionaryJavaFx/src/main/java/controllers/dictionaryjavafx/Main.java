package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import classes.data.DictionaryData;
import classes.data.GameData;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Dictionary");
        SceneLoaderController.setStage(stage);
        SceneLoaderController. loadScene(FXMLFiles.MAIN_UI_SCENE);
        stage.show();

        DictionaryData.loadData();
        GameData.loadData();
    }

    public static void main(String[] args) {
        launch();
    }
}