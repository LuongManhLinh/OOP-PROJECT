package controllers.dictionaryjavafx;
import classes.Dictionary;
import classes.DictionaryManagementForApp;
import classes.EnViDictionary;
import classes.FXMLFiles;
import classes.data.DictionaryData;
import classes.data.WordWork;
import classes.dictionarycommandline.DictionaryExecution;
import classes.googlework.GgTranslateTextToSpeech;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainUISceneController implements Initializable {
    @FXML private TextField wordEnteringField;
    @FXML private ListView<String> searchingResultList;
    @FXML private WebView meaningWebView;
    @FXML private MenuBar menuBar;
    @FXML private Button searchingTypeButton;
    @FXML private Button speakButton;
    private String oldKeyWord = "";
    private Dictionary.Type searchingType = Dictionary.Type.EN_VI;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hide();
        // khung nhìn hiển thị được tối đa 10 kết quả, nếu nhiều hơn phải cuộn xuống để xem
        searchingResultList.setMaxHeight(20 * searchingResultList.getFixedCellSize());
        searchingResultList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //cài đặt hiển thị nghĩa
                String selectedWord = searchingResultList.getSelectionModel().getSelectedItem();
                if(selectedWord != null) {
                    show();
                    String meanings = DictionaryManagementForApp.getMeaning(selectedWord, searchingType);
                    WebEngine webEngine = meaningWebView.getEngine();
                    webEngine.loadContent(WordWork.toHTMLMeaningStyle(meanings));
                }
            }
        });
        handleSearching();
    }

    private void handleSearching(){
        // tạo ra vòng lặp để tìm kiếm
        AnimationTimer wordEnteringTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String keyWord = wordEnteringField.getText();
                oldKeyWord = keyWord;

                ArrayList<String> searchingResult = DictionaryManagementForApp.lookUp(keyWord, searchingType);
                if (!searchingResult.isEmpty()) {
                    searchingResultList.setVisible(true);
                    searchingResultList.getItems().setAll(searchingResult);
                    // điều chỉnh khung nhìn chỉ đủ để hiển thị kết quả
                    // nếu số kết quả lớn hơn 20 thì cũng chỉ hiển thị 20
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

                if (wordEnteringField.getText().isEmpty()) {
                    hide();
                }
            }
        };

        wordEnteringTimer.start();
        searchingManagementTimer.start();

    }
    
    public void hide() {
        meaningWebView.setVisible(false);
        searchingResultList.setVisible(false);
        speakButton.setVisible(false);
    }

    public void show() {
        meaningWebView.setVisible(true);
        speakButton.setVisible(true);
    }

    public void onSearchingTypeChanged(ActionEvent event) {
        hide();
        if (searchingType == Dictionary.Type.EN_VI) {
            searchingType = Dictionary.Type.VI_EN;
            searchingTypeButton.setText("VIỆT-ANH");
        } else if (searchingType == Dictionary.Type.VI_EN) {
            searchingType = Dictionary.Type.EN_VI;
            searchingTypeButton.setText("ANH-VIỆT");
        }
    }

    public void speakerFunc(ActionEvent event) {
        String paragraph = searchingResultList.getSelectionModel().getSelectedItem();
        if (paragraph != null && !paragraph.isEmpty()) {
            if (searchingType == Dictionary.Type.EN_VI) {
                GgTranslateTextToSpeech.play(paragraph, "en");
            } else if (searchingType == Dictionary.Type.VI_EN) {
                GgTranslateTextToSpeech.play(paragraph, "vi");
            }
        }
    }

    public void selectInsertWordFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.INSERT_WORDS_SCENE);
    }

    public void selectFixMeaningFunc(ActionEvent event) {

    }

    public void selectAddMeaningFunc(ActionEvent event) {

    }

    public void selectFixWordFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.FIX_WORD_SCENE);
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
