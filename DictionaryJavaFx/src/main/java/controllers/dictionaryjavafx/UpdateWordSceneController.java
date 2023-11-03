package controllers.dictionaryjavafx;

import classes.Dictionary;
import classes.DictionaryManagementForApp;
import classes.EnViDictionary;
import classes.FXMLFiles;
import classes.data.WordWork;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateWordSceneController implements Initializable {
    @FXML
    private TextField wordEnteringField;
    @FXML private ListView<String> searchingResultList;
    @FXML private WebView meaningWebView;
    @FXML private Button backToMainUIButton;
    @FXML private Button FixMeaningButton;
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    @FXML private Button backToSelectWordButton;
    @FXML private TextField editWordField;
    @FXML private TextArea editMeaningArea;
    @FXML private Label noteLabel;
    private String oldKeyWord = "";
    private String selectedWord = "";
    private String meaning = "";
    private Dictionary.Type searchingType = Dictionary.Type.EN_VI;

    private static UpdateWordSceneController instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        backToSelectWordButton.setVisible(false);
        editWordField.setVisible(false);
        editMeaningArea.setVisible(false);
        saveButton.setVisible(false);
        noteLabel.setVisible(false);
        removeButton.setVisible(false);

        // khung nhìn hiển thị được tối đa 10 kết quả, nếu nhiều hơn phải cuộn xuống để xem
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
                String selectedWord = searchingResultList.getSelectionModel().getSelectedItem();
                if(selectedWord != null) {
                    FixMeaningButton.setVisible(true);
                    String meanings = DictionaryManagementForApp.getMeaning(selectedWord, searchingType);
                    WebEngine webEngine = meaningWebView.getEngine();
                    webEngine.loadContent(WordWork.toHTMLMeaningStyle(meanings));
                }
            }
        });
        handleSearching();
    }
    public static UpdateWordSceneController getInstance() {
        return instance;
    }

    private void handleSearching(){
        // tạo ra vòng lặp để tìm kiếm
        AnimationTimer wordEnteringTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String keyWord = wordEnteringField.getText();
                oldKeyWord = keyWord;
                selectedWord = keyWord;
                ArrayList<String> searchingResult = DictionaryManagementForApp.lookUp(keyWord, searchingType);
                if (!searchingResult.isEmpty()) {
                    searchingResultList.setVisible(true);
                    searchingResultList.getItems().setAll(searchingResult);
                    searchingResultList.getSelectionModel().select(0);
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
                    selectedWord = wordEnteringField.getText();
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
                }
            }
        };

        wordEnteringTimer.start();
        searchingManagementTimer.start();
    }

    public void updateWord() {
        //nếu ấn chọn từ ở listView thì sẽ sửa nghĩa của từ đó còn không thì sửa nghĩa của từ ở textField
        if(searchingResultList.getSelectionModel().getSelectedItem() != null) {
            selectedWord = searchingResultList.getSelectionModel().getSelectedItem();
        }
        if(selectedWord.isEmpty() || !EnViDictionary.getInstance().getKeyWords().contains(selectedWord)) {
            showInvalidOldWordAlert();
        }

        else {
            meaningWebView.setVisible(false);
            wordEnteringField.setVisible(false);
            FixMeaningButton.setVisible(false);
            backToMainUIButton.setVisible(false);
            searchingResultList.setVisible(false);

            editMeaningArea.clear();
            editWordField.setVisible(true);
            editMeaningArea.setVisible(true);
            noteLabel.setVisible(true);
            backToSelectWordButton.setVisible(true);
            saveButton.setVisible(true);
            removeButton.setVisible(true);

            meaning = EnViDictionary.getInstance().getWords().get(selectedWord);
            meaning = meaning.replace("<br />", "\n");

            editWordField.setText(selectedWord);
            editMeaningArea.setText(meaning);
        }
    }

    public void saveNewMeaning() {
        String newWord = editWordField.getText();
        String newMeaning = editMeaningArea.getText();
        newMeaning = newMeaning.replace("\n", "<br />");
        showSaveNewMeaningAlert(newWord, newMeaning);
    }

    public void removeWordFunc() {
        String word = editWordField.getText();
        word = word.toLowerCase().trim();
        if (EnViDictionary.getInstance().getKeyWords().contains(word)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xóa từ");
            alert.setHeaderText("Bạn có muốn xóa từ này không?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                EnViDictionary.getInstance().getKeyWords().remove(word);
                EnViDictionary.getInstance().getWords().remove(word);
                editWordField.clear();
                editMeaningArea.clear();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không tìm thấy từ");
            alert.setHeaderText("Từ bạn nhập không hợp lệ, Vui lòng nhập lại");
            alert.show();
        }
    }

    public void backToSelectWord() {

        meaningWebView.setVisible(true);
        wordEnteringField.clear();
        wordEnteringField.setVisible(true);
        FixMeaningButton.setVisible(true);
        backToMainUIButton.setVisible(true);
        searchingResultList.setVisible(true);
        searchingResultList.getSelectionModel().clearSelection();

        WebEngine webEngine = meaningWebView.getEngine();
        webEngine.loadContent("");

        noteLabel.setVisible(false);
        backToSelectWordButton.setVisible(false);
        editMeaningArea.setVisible(false);
        saveButton.setVisible(false);
        removeButton.setVisible(false);
        backToSelectWordButton.setVisible(false);
        editWordField.setVisible(false);
    }

    private void showInvalidOldWordAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText("Từ bạn chọn không hợp lệ hoặc không nằm trong từ điển");
        alert.setContentText("Vui lòng nhập từ hợp lệ");
        WebEngine webEngine = meaningWebView.getEngine();
        webEngine.loadContent("");
        alert.showAndWait();
    }

    private void showSaveNewMeaningAlert(String newWord, String newMeaning) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Bạn có muốn lưu nghĩa vừa sửa không?");
        alert.setContentText("Ấn 'OK' để lưu nghĩa hoặc ấn 'Cancel' để tiếp tục chỉnh sửa" );
        ButtonType userChoice = alert.showAndWait().get();
        if(userChoice == ButtonType.OK) {
            EnViDictionary.getInstance().getWords().put(newWord, newMeaning);
            EnViDictionary.getInstance().getKeyWords().add(newWord);
        }
    }

    public void backToMainUIScene() {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }

    public void updateWordFromMainUI(String selectedWordUI) {
        meaningWebView.setVisible(false);
        wordEnteringField.setVisible(false);
        FixMeaningButton.setVisible(false);
        searchingResultList.setVisible(false);

        editMeaningArea.clear();
        editWordField.setVisible(true);
        editMeaningArea.setVisible(true);
        noteLabel.setVisible(true);
        saveButton.setVisible(true);
        removeButton.setVisible(true);
        backToMainUIButton.setVisible(true);

        String meaningUI = EnViDictionary.getInstance().getWords().get(selectedWordUI);
        meaningUI = meaningUI.replace("<br />", "\n");

        editWordField.setText(selectedWordUI);
        editMeaningArea.setText(meaningUI);
    }


}




