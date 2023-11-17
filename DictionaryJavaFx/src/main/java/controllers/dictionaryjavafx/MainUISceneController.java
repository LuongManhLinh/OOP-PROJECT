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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainUISceneController implements Initializable {
    @FXML private AnchorPane anchorPane;
    @FXML private TextField wordEnteringField;
    @FXML private ListView<String> searchingResultList;
    @FXML private WebView meaningWebView;
    @FXML private Label engImage, viImage;
    @FXML private Button speaker;

    @FXML private Button editWordButton;
    @FXML private Label errorLabel;
    @FXML private Label F1Label;
    @FXML private Label F2Label;
    @FXML private Label F3Label;
    @FXML private Label F4Label;
    @FXML private Label F5Label;
    @FXML private Label EscLabel;
    @FXML private Label CtrlULabel;
    @FXML private Button insertWordButton;
    @FXML private Button updateWordButton;
    @FXML private Button translateTextButton;
    @FXML private Button experimentGameButton;
    @FXML private Button commandlineButton;
    @FXML private Button exitAppButton;

    private final double FLAG_POS_X_LEFT = 15;
    private final double FLAG_POS_X_RIGHT = 150;

    private String oldKeyWord = "";
    private String selectedWord = "";
    private Dictionary.Type searchingType = Dictionary.Type.EN_VI;
    private boolean isSwitchingLang = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideLabel();
        hide();
        setOnKeyPress();
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
                } else if (keyEvent.getCode() == KeyCode.ENTER) {
                    int selectedIndex = searchingResultList.getSelectionModel().getSelectedIndex();
                    if (selectedIndex < searchingResultList.getItems().size() - 1) {
                        searchingResultList.getSelectionModel().selectNext();
                        if (selectedIndex >= 19) {
                            searchingResultList.scrollTo(selectedIndex + 1);
                        }
                    }
                }
            }
        });

        // khung nhìn hiển thị được tối đa 20 kết quả, nếu nhiều hơn phải cuộn xuống để xem
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
                if (searchingResultList.isFocused() || oldKeyWord.equals(wordEnteringField.getText()) || isSwitchingLang) {
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
        editWordButton.setVisible(false);
        speaker.setVisible(false);
    }

    public void hideLabel() {
        F1Label.setVisible(false);
        F2Label.setVisible(false);
        F3Label.setVisible(false);
        F4Label.setVisible(false);
        F5Label.setVisible(false);
        EscLabel.setVisible(false);
        CtrlULabel.setVisible(false);
    }

    public void show() {
        meaningWebView.setVisible(true);
        speaker.setVisible(true);
    }

    private void switchLanguage(Dictionary.Type type) {
        double viPosX = viImage.getLayoutX();
        isSwitchingLang = true;

        double mpl = 5;
        AnimationTimer switchTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (type == Dictionary.Type.EN_VI) {
                    engImage.setLayoutX(engImage.getLayoutX() + mpl);
                    viImage.setLayoutX(viImage.getLayoutX() - mpl);
                    if (Math.abs(engImage.getLayoutX() - viPosX) < mpl) {
                        searchingType = Dictionary.Type.VI_EN;
                        isSwitchingLang = false;
                        viImage.setLayoutX(FLAG_POS_X_LEFT);
                        engImage.setLayoutX(FLAG_POS_X_RIGHT);
                        stop();
                    }
                } else if (type == Dictionary.Type.VI_EN) {
                    engImage.setLayoutX(engImage.getLayoutX() - mpl);
                    viImage.setLayoutX(viImage.getLayoutX() + mpl);
                    if (Math.abs(engImage.getLayoutX() - viPosX) < mpl) {
                        searchingType = Dictionary.Type.EN_VI;
                        isSwitchingLang = false;
                        viImage.setLayoutX(FLAG_POS_X_RIGHT);
                        engImage.setLayoutX(FLAG_POS_X_LEFT);
                        stop();
                    }
                }
            }
        };
        switchTimer.start();
    }
    public void onSearchingTypeChanged() {
        if (!isSwitchingLang) {
            oldKeyWord = "";
            hide();
            switchLanguage(this.searchingType);
        }
    }

    public void speakerFunc() {
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

    private void setOnKeyPress() {
        final KeyCodeCombination goToInsertWord = new KeyCodeCombination(KeyCode.F1);
        final KeyCodeCombination goToUpdateWord = new KeyCodeCombination(KeyCode.F2);
        final KeyCodeCombination goToTranslateText = new KeyCodeCombination(KeyCode.F3);
        final KeyCodeCombination goToExperimentGame = new KeyCodeCombination(KeyCode.F4);
        final KeyCodeCombination goToCommandline = new KeyCodeCombination(KeyCode.F5);
        final KeyCodeCombination exitApp = new KeyCodeCombination(KeyCode.ESCAPE);
        final KeyCodeCombination updateWordFromMainUI = new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN);
        anchorPane.setOnKeyPressed(keyEvent -> {
            if (goToInsertWord.match(keyEvent)) insertWordButton.fire();
            if (goToUpdateWord.match(keyEvent)) updateWordButton.fire();
            if (goToTranslateText.match(keyEvent)) translateTextButton.fire();
            if (goToExperimentGame.match(keyEvent)) experimentGameButton.fire();
            if (goToCommandline.match(keyEvent)) commandlineButton.fire();
            if (exitApp.match(keyEvent)) exitAppButton.fire();
            if (updateWordFromMainUI.match(keyEvent)) editWordButton.fire();
        });
    }

    public void selectExitApp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thoát ứng dụng");
        alert.setHeaderText("Bạn có muốn thoát ứng dụng không?");
        ButtonType co = new ButtonType("Có");
        ButtonType khong = new ButtonType("Không");
        alert.getButtonTypes().setAll(co, khong);
        if(alert.showAndWait().get() == co) {
            SceneLoaderController.exitApp();
        }
    }
    public void showF1() {
        F1Label.setVisible(true);
    }
    public void hideF1() {
        F1Label.setVisible(false);
    }

    public void showF2() {
        F2Label.setVisible(true);
    }
    public void hideF2() {
        F2Label.setVisible(false);
    }

    public void showF3() {
        F3Label.setVisible(true);
    }
    public void hideF3() {
        F3Label.setVisible(false);
    }

    public void showF4() {
        F4Label.setVisible(true);
    }
    public void hideF4() {
        F4Label.setVisible(false);
    }

    public void showF5() {
        F5Label.setVisible(true);
    }
    public void hideF5() {
        F5Label.setVisible(false);
    }
    public void showEsc() {
        EscLabel.setVisible(true);
    }
    public void hideEsc() {
        EscLabel.setVisible(false);
    }

    public void showCtrlU() {
        CtrlULabel.setVisible(true);
    }
    public void hideCtrlU() {
        CtrlULabel.setVisible(false);
    }
}
