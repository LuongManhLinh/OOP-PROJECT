package controllers.dictionaryjavafx;

import classes.data.DictionaryData;
import classes.data.GameData;
import classes.dictionarycommandline.DictionaryExecution;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainUIScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DictionaryData.loadData();
        GameData.loadData();
        launch();
        if (SelectTypeSceneController.isUsingCommandline) {
            DictionaryExecution.Run();
        }
    }
}