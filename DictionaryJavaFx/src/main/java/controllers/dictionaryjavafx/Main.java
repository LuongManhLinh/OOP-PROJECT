package controllers.dictionaryjavafx;

import classes.Dictionary;
import classes.GameData;
import classes.dictionarycommandline.DictionaryExecution;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ExperimentGameScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Dictionary.Awake();
        GameData.loadData();
        launch();
        if (SelectTypeSceneController.isUsingCommandline) {
            DictionaryExecution.Run();
        }

    }
}