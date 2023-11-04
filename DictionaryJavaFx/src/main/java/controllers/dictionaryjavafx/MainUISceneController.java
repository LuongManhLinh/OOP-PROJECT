package controllers.dictionaryjavafx;
import classes.Dictionary;
import classes.DictionaryManagementForApp;
import classes.FXMLFiles;
import classes.data.WordWork;
import classes.dictionarycommandline.DictionaryExecution;
import classes.googlework.GgTranslateTextToSpeech;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainUISceneController implements Initializable {
    @FXML private TextField wordEnteringField;
    @FXML private ListView<String> searchingResultList;
    @FXML private WebView meaningWebView;
    @FXML private Button searchingTypeButton;
    @FXML private Button speakButton;
    @FXML private Button editWordButton;
    @FXML private Label errorLabel;
    private String oldKeyWord = "";
    private String selectedWord = "";
    private Dictionary.Type searchingType = Dictionary.Type.EN_VI;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hide();

        wordEnteringField.setOnKeyPressed(keyEvent -> {
            if (!searchingResultList.getItems().isEmpty()) {
                if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.DOWN) {
                    searchingResultList.getSelectionModel().select(0);
                    searchingResultList.requestFocus();
                }
            }

            if (!searchingResultList.isVisible()){
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String text = wordEnteringField.getText();
                    if (text != null && !text.isEmpty()) {
                        SceneLoaderController.loadScene(FXMLFiles.TRANSLATE_TEXT_SCENE);
                        if (searchingType == Dictionary.Type.EN_VI) {
                            SceneTranslateTextController.getInstance().setTextAndLang(text,
                                    SceneTranslateTextController.languages[1], SceneTranslateTextController.languages[0]);
                        } else if (searchingType == Dictionary.Type.VI_EN) {
                            SceneTranslateTextController.getInstance().setTextAndLang(text,
                                    SceneTranslateTextController.languages[0], SceneTranslateTextController.languages[1]);
                        }
                    }
                }
            }
        });

        searchingResultList.setOnKeyPressed(keyEvent -> {
            if (!searchingResultList.getItems().isEmpty()) {
                if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    wordEnteringField.requestFocus();
                } else if (keyEvent.getCode() == KeyCode.UP) {
                    if (searchingResultList.getSelectionModel().getSelectedIndex() == 0) {
                        wordEnteringField.requestFocus();
                    }
                }
            }
        });

        // khung nhìn hiển thị được tối đa 20 kết quả, nếu nhiều hơn phải cuộn xuống để xem
        searchingResultList.setMaxHeight(20 * searchingResultList.getFixedCellSize());
        searchingResultList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //cài đặt hiển thị nghĩa
                selectedWord = searchingResultList.getSelectionModel().getSelectedItem();
                if(selectedWord != null) {
                    editWordButton.setVisible(true);
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
                    searchingResultList.getSelectionModel().select(0);
                    // điều chỉnh khung nhìn chỉ đủ để hiển thị kết quả
                    // nếu số kết quả lớn hơn 20 thì cũng chỉ hiển thị 20
                    searchingResultList.setPrefHeight(searchingResult.size() * searchingResultList.getFixedCellSize());
                } else {
                    hide();
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
                } else {
                    wordEnteringTimer.start();
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
        editWordButton.setVisible(false);
    }

    public void show() {
        meaningWebView.setVisible(true);
        speakButton.setVisible(true);
    }

    public void onSearchingTypeChanged(ActionEvent event) {
        oldKeyWord = "";
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
            String response = "";
            if (searchingType == Dictionary.Type.EN_VI) {
                response = GgTranslateTextToSpeech.play(paragraph, "en");
            } else if (searchingType == Dictionary.Type.VI_EN) {
                response = GgTranslateTextToSpeech.play(paragraph, "vi");
            }

            if (response.equals(GgTranslateTextToSpeech.errorString)) {
                errorLabel.setVisible(true);
                Timeline errorTime = new Timeline(
                        new KeyFrame(Duration.millis(1000), waitEvent -> {
                            errorLabel.setVisible(false);
                        })
                );
                errorTime.play();
            }
        }
    }

    public void selectInsertWordFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.INSERT_WORDS_SCENE);
    }

    //chọn từ trước khi sửa hoặc xóa
    public void selectUpdateWordFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.UPDATE_WORD_SCENE);
    }

    //nhảy thẳng vào sửa hoặc xóa từ
    public void selectUpdateWordFromMainUIFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.UPDATE_WORD_SCENE);
        UpdateWordSceneController.getInstance().updateWordFromMainUI(selectedWord, searchingType);
    }

    public void translateText(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.TRANSLATE_TEXT_SCENE);
    }
    public void selectExperimentGameFunc(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.EXPERIMENT_GAME_SCENE);
    }

    public void selectCommandline(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bạn có muốn sử dụng bản commandline?");
        alert.setHeaderText("Sử dụng sẽ thoát ứng dụng từ điển");
        ButtonType ok = new ButtonType("Tiếp tục dùng bản commandline");
        ButtonType cancel = new ButtonType("Hủy");
        alert.getButtonTypes().setAll(ok, cancel);

        if (alert.showAndWait().get() == ok) {
            SceneLoaderController.exitApp();
            DictionaryExecution.Run();
        }
    }

    public void selectExitApp(ActionEvent event) {
        SceneLoaderController.exitApp();
    }


}
