package controllers.dictionaryjavafx;

import classes.Dictionary;
import classes.EnViDictionary;
import classes.ViEnDictionary;
import classes.data.DictionaryData;
import javafx.application.Platform;
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
//            DictionaryData.writeData(Dictionary.Type.EN_VI, EnViDictionary.getInstance().getWords(), EnViDictionary.getInstance().getKeyWords());
//            DictionaryData.writeData(Dictionary.Type.VI_EN, ViEnDictionary.getInstance().getWords(), ViEnDictionary.getInstance().getKeyWords());
        });
    }

    public static Stage getStage() {
        return stage;
    }

    public static void exitApp() {
//        DictionaryData.writeData(Dictionary.Type.EN_VI, EnViDictionary.getInstance().getWords(), EnViDictionary.getInstance().getKeyWords());
//        DictionaryData.writeData(Dictionary.Type.VI_EN, ViEnDictionary.getInstance().getWords(), ViEnDictionary.getInstance().getKeyWords());
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
    }
}
