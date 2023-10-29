package controllers.dictionaryjavafx;
import classes.Dictionary;
import classes.DictionaryManagementForApp;
import classes.EnViDictionary;
import classes.FXMLFiles;
import classes.data.DictionaryData;
import classes.dictionarycommandline.DictionaryExecution;
import classes.googlework.GgTranslateTextToSpeech;
import com.almasb.fxgl.entity.action.Action;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainUISceneController implements Initializable {
    @FXML private TextField wordEnteringField;
    @FXML private ListView<String> searchingResultList;
    @FXML private WebView webView;
    @FXML private MenuBar menuBar;
    @FXML private ImageView imageSpeaker;
    private String oldKeyWord = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchingResultList.setVisible(false);
        webView.setVisible(false);
        imageSpeaker.setVisible(false);
        // khung nhìn hiển thị được tối đa 10 kết quả, nếu nhiều hơn phải cuộn xuống để xem
        searchingResultList.setMaxHeight(10 * searchingResultList.getFixedCellSize());

        searchingResultList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //cài đặt hiển thị nghĩa
            }
        });

        handleSearching();

        searchingResultList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //cài đặt hiển thị nghĩa
                imageSpeaker.setVisible(true);
                webView.setVisible(true);
                String selectedWord = searchingResultList.getSelectionModel().getSelectedItem();
                oldKeyWord = selectedWord;
                wordEnteringField.setText(selectedWord);
                if(selectedWord != null) {
                    String meanings = DictionaryManagementForApp.getMeaning(selectedWord, Dictionary.Type.EN_VI);
                    WebEngine webEngine = webView.getEngine();
                    webEngine.loadContent(meanings);
                }
            }
        });
    }

    private void handleSearching(){
        // tạo ra vòng lặp để tìm kiếm
        AnimationTimer wordEnteringTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String keyWord = wordEnteringField.getText();
                oldKeyWord = keyWord;

                ArrayList<String> searchingResult = DictionaryManagementForApp.lookUp(keyWord, Dictionary.Type.EN_VI);
                if (!searchingResult.isEmpty()) {
                    searchingResultList.setVisible(true);
                    searchingResultList.getItems().setAll(searchingResult);
                    // điều chỉnh khung nhìn chỉ đủ để hiển thị kết quả
                    // nếu số kết quả lớn hơn 10 thì cũng chỉ hiển thị 10
                    searchingResultList.setPrefHeight(searchingResult.size() * searchingResultList.getFixedCellSize());
                } else {
                    searchingResultList.setVisible(false);
                }
            }
        };

        // vòng lặp để quản lí sự tìm kiếm của vòng lặp trên
        AnimationTimer searchingManagementTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                // nếu đang tương tác với kết quả hay không thay đổi chuỗi tìm kiếm thì sẽ dừng tìm kiếm
                if (searchingResultList.isFocused() || oldKeyWord.equals(wordEnteringField.getText())) {
                    wordEnteringTimer.stop();
//                    System.out.println("if");
                } else {
                    wordEnteringTimer.start();

                    // xử lí để khi nhấn enter thì nhảy xuống danh sách kết quả
                    if (wordEnteringField.isFocused()) {
//                        System.out.println("f");
                        Scene scene = wordEnteringField.getScene();
                        scene.setOnKeyPressed(event -> {
                            if (event.getCode() == KeyCode.ENTER ) {
//                                System.out.println("else");
                                if (!searchingResultList.getItems().isEmpty()) {
                                    searchingResultList.requestFocus();
                                    searchingResultList.getSelectionModel().select(0);
                                }
                            }
                        });
                    }
                }
            }
        };

        wordEnteringTimer.start();
        searchingManagementTimer.start();

    }
    public void speakerFunc(MouseEvent event) {
        String paragraph = oldKeyWord;
        GgTranslateTextToSpeech.play(paragraph, "en");
    }

    public void selectInsertWordFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.INSERT_WORDS_SCENE);
    }

    public void selectFixMeaningFunc(ActionEvent event) {

    }

    public void selectAddMeaningFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.ADD_MEANING_SCENE);
    }

    public void selectFixWordFunc(ActionEvent event) {

    }

    public void selectRemoveWordFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.REMOVE_WORD_SCENE);
    }

    public void translateText(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.TRANSLATE_TEXT_SCENE);
    }
    public void selectExperimentGameFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.EXPERIMENT_GAME_SCENE);
    }

    public void selectCommandline(ActionEvent event) {
        SceneLoaderController.exitApp();
        DictionaryExecution.Run();
    }

    public void selectExitApp(ActionEvent event) {
        DictionaryData.writeData(Dictionary.Type.EN_VI, EnViDictionary.getInstance().getWords(), EnViDictionary.getInstance().getKeyWords());
        SceneLoaderController.exitApp();
        SceneLoaderController.exitApp();
    }
}
