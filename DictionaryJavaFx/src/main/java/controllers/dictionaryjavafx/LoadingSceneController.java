package controllers.dictionaryjavafx;

import classes.EnViBookMark;
import classes.FXMLFiles;
import classes.ViEnBookMark;
import classes.data.DictionaryData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class LoadingSceneController {
    public static void onLaunch() {
        Timeline waitTimeLine = new Timeline(
                new KeyFrame(Duration.millis(1), event -> {
                    DictionaryData.loadData();
                    EnViBookMark.loadBookMark();
                    ViEnBookMark.loadBookMark();
                    SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
                })
        );
        waitTimeLine.play();
    }
}
