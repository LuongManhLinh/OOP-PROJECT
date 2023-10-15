package controllers.dictionaryjavafx;

import classes.Dictionary;
import classes.dictionarycommandline.DictionaryExecution;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SelectTypeScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Dictionary.Awake();
        launch();
        if (SelectTypeSceneController.isUsingCommandline) {
            DictionaryExecution.Run();
        }

    }
}