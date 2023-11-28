package controllers.dictionaryjavafx;

import classes.*;
import classes.data.WordWork;
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
import javafx.scene.image.Image;
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

public class BookMarkSceneController implements Initializable {
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField searchWord;
    @FXML
    private ListView<String> listWordInSearch;
    @FXML
    private WebView meaningWord;
    @FXML
    private Label engImage, viImage;
    @FXML
    private ImageView speakerIn, speakerOut;
    @FXML
    private ImageView unmarked, marked;
    @FXML
    private Label errorLabel;
    @FXML
    private Button backToMainUIButton;
    @FXML
    private Button changeLanguageButton;
    @FXML
    private Label CtrlDLabel;
    @FXML
    private Label EscLabel;
    private final double FLAG_POS_X_LEFT = 15;
    private final double FLAG_POS_X_RIGHT = 150;

    private String oldKeyWord = "";
    private String selectedWord = "";
    private Dictionary.Type searchingType = Dictionary.Type.EN_VI;
    private boolean isSwitchingLang = false;
    private ArrayList<String> searchingResult = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hide();
        searchWord.setOnKeyPressed(keyEvent -> {
            if (!listWordInSearch.getItems().isEmpty()) {
                if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.DOWN) {
                    listWordInSearch.getSelectionModel().select(0);
                    listWordInSearch.requestFocus();
                }
            }

            if (!listWordInSearch.isVisible()) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String text = searchWord.getText();
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

        listWordInSearch.setOnKeyPressed(keyEvent -> {
            if (!listWordInSearch.getItems().isEmpty()) {
                if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    searchWord.requestFocus();
                } else if (keyEvent.getCode() == KeyCode.UP) {
                    if (listWordInSearch.getSelectionModel().getSelectedIndex() == 0) {
                        searchWord.requestFocus();
                    }
                } else if (keyEvent.getCode() == KeyCode.ENTER) {
                    int selectedIndex = listWordInSearch.getSelectionModel().getSelectedIndex();
                    if (selectedIndex < listWordInSearch.getItems().size() - 1) {
                        listWordInSearch.getSelectionModel().selectNext();
                        if (selectedIndex >= 19) {
                            listWordInSearch.scrollTo(selectedIndex + 1);
                        }
                    }
                }
            }
        });

        // khung nhìn hiển thị được tối đa 20 kết quả, nếu nhiều hơn phải cuộn xuống để xem
        listWordInSearch.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //cài đặt hiển thị nghĩa
                selectedWord = listWordInSearch.getSelectionModel().getSelectedItem();
                if (selectedWord != null) {
                    show();
                    String meanings = DictionaryManagementForApp.getMeaning(selectedWord, searchingType);
                    WebEngine webEngine = meaningWord.getEngine();
                    webEngine.loadContent(WordWork.toHTMLMeaningStyle(meanings));
                }
            }
        });

        handleSearching();
        setOnKeyPressed();
    }

    private void setOnKeyPressed() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                backToMainUI();
            }
        });
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                backToMainUI();
            }
        });
        final KeyCodeCombination backToMainUi = new KeyCodeCombination(KeyCode.ESCAPE);
        final KeyCodeCombination changeLanguage = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
        pane.setOnKeyPressed(keyEvent -> {
            if (backToMainUi.match(keyEvent)) backToMainUIButton.fire();
            if (changeLanguage.match(keyEvent)) changeLanguageButton.fire();
        });
    }

    private void handleSearching() {
        // tạo ra vòng lặp để tìm kiếm
        AnimationTimer wordEnteringTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String keyWord = searchWord.getText();
                oldKeyWord = keyWord;
                if (oldKeyWord.isEmpty()) {
                    if (searchingType == Dictionary.Type.EN_VI) {
                        searchingResult = EnViBookMark.getEngKey();
                        if (!searchingResult.isEmpty()) {
                            listWordInSearch.setVisible(true);
                            listWordInSearch.getItems().setAll(searchingResult);
                            listWordInSearch.setPrefHeight(searchingResult.size() * listWordInSearch.getFixedCellSize());
                        } else {
                            hide();
                        }
                    } else {
                        searchingResult = ViEnBookMark.getVieKey();
                        if (!searchingResult.isEmpty()) {
                            listWordInSearch.setVisible(true);
                            listWordInSearch.getItems().setAll(searchingResult);
                            listWordInSearch.setPrefHeight(searchingResult.size() * listWordInSearch.getFixedCellSize());
                        } else {
                            hide();
                        }
                    }
                } else {
                    if (searchingType == Dictionary.Type.EN_VI) {
                        searchingResult = EnViBookMark.lookUp(keyWord);
                    } else {
                        searchingResult = ViEnBookMark.lookUp(keyWord);
                    }
                    if (!searchingResult.isEmpty()) {
                        listWordInSearch.setVisible(true);
                        listWordInSearch.getItems().setAll(searchingResult);
                        listWordInSearch.getSelectionModel().select(0);
                        listWordInSearch.setPrefHeight(searchingResult.size() * listWordInSearch.getFixedCellSize());
                    } else {
                        hide();
                    }
                }
            }
        };

        // vòng lặp để quản lí sự tìm kiếm của vòng lặp trên
        AnimationTimer searchingManagementTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                // nếu đang tương tác với kết quả hay không thay đổi chuỗi tìm kiếm thì sẽ dừng tìm kiếm
                if (listWordInSearch.isFocused() || oldKeyWord.equals(searchWord.getText()) || isSwitchingLang) {
                    wordEnteringTimer.stop();
                } else {
                    wordEnteringTimer.start();
                }

                if (searchWord.getText().isEmpty() && selectedWord == null) {
                    hide();
                    if(!searchingResult.isEmpty()){
                        listWordInSearch.setVisible(true);
                        listWordInSearch.setPrefHeight(searchingResult.size() * listWordInSearch.getFixedCellSize());
                    }
                }
            }
        };

        wordEnteringTimer.start();
        searchingManagementTimer.start();
    }

    public void hide() {
        meaningWord.setVisible(false);
        listWordInSearch.setVisible(false);
        speakerIn.setVisible(false);
        speakerOut.setVisible(false);
        unmarked.setVisible(false);
        marked.setVisible(false);
    }

    public void show() {
        meaningWord.setVisible(true);
        speakerOut.setVisible(true);
        if (searchingType == Dictionary.Type.EN_VI) {
            if (EnViBookMark.checkInBookMark(selectedWord)) {
                marked.setVisible(true);
                unmarked.setVisible(false);
            } else {
                unmarked.setVisible(true);
                marked.setVisible(false);
            }
        } else {
            if (ViEnBookMark.checkInBookMark(selectedWord)) {
                marked.setVisible(true);
                unmarked.setVisible(false);
            } else {
                unmarked.setVisible(true);
                marked.setVisible(false);
            }
        }
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
                        searchingResult = ViEnBookMark.getVieKey();
                        if(!searchingResult.isEmpty()) {
                            listWordInSearch.setVisible(true);
                            listWordInSearch.getItems().setAll(searchingResult);
                            listWordInSearch.setPrefHeight(searchingResult.size() * listWordInSearch.getFixedCellSize());
                        }
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
                        searchingResult = EnViBookMark.getEngKey();
                        if(!searchingResult.isEmpty()) {
                            listWordInSearch.setVisible(true);
                            listWordInSearch.getItems().setAll(searchingResult);
                            listWordInSearch.setPrefHeight(searchingResult.size() * listWordInSearch.getFixedCellSize());
                        }
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

    public void unmarkedWord(MouseEvent event) {
        if (searchingType == Dictionary.Type.EN_VI) {
            EnViBookMark.insertToBookMark(selectedWord);
        } else {
            ViEnBookMark.insertToBookMark(selectedWord);
        }
        show();
    }

    public void markedWord(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xoá từ đã lưu");
        alert.setHeaderText("Bạn muốn xoá từ này khỏi danh sách từ đã lưu?");
        if(alert.showAndWait().get() == ButtonType.OK) {
            if (searchingType == Dictionary.Type.EN_VI) {
                EnViBookMark.remove(selectedWord);
                searchingResult = EnViBookMark.getEngKey();
                listWordInSearch.getItems().setAll(searchingResult);
                listWordInSearch.setPrefHeight(searchingResult.size() * listWordInSearch.getFixedCellSize());
            } else {
                ViEnBookMark.remove(selectedWord);
                searchingResult = ViEnBookMark.getVieKey();
                listWordInSearch.getItems().setAll(searchingResult);
                listWordInSearch.setPrefHeight(searchingResult.size() * listWordInSearch.getFixedCellSize());
            }
        }
        show();
    }

    public void removeAll(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xoá");
        alert.setHeaderText("Bạn có muốn xóa tất cả từ đã lưu không?");
        if(alert.showAndWait().get() == ButtonType.OK) {
            if (searchingType == Dictionary.Type.EN_VI) {
                EnViBookMark.removeAll();
                hide();
                searchingResult = EnViBookMark.getEngKey();
            } else {
                ViEnBookMark.removeAll();
                hide();
                searchingResult = ViEnBookMark.getVieKey();
            }
        }
    }
    public void moveIn(){
        speakerIn.setVisible(true);
        speakerOut.setVisible(false);
    }

    public void moveOut(){
        speakerIn.setVisible(false);
        speakerOut.setVisible(true);
    }
    public void speakerFunc() {
        String paragraph = listWordInSearch.getSelectionModel().getSelectedItem();
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

    public void showCtrlD() {
        CtrlDLabel.setVisible(true);
    }

    public void hideCtrlD() {
        CtrlDLabel.setVisible(false);
    }

    public void showEsc() {
        EscLabel.setVisible(true);
    }

    public void hideEsc() {
        EscLabel.setVisible(false);
    }

    public void backToMainUI() {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }
}


