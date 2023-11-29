package controllers.dictionaryjavafx;

import classes.*;
import classes.data.WordWork;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateWordSceneController implements Initializable {
    @FXML AnchorPane searchingPane;
    @FXML AnchorPane editingPane;
    @FXML private TextField wordEnteringField;
    @FXML private ListView<String> searchingResultList;
    @FXML private WebView meaningWebView;
    @FXML private Button FixMeaningButton;
    @FXML private Button searchingTypeButton;
    @FXML private TextField editWordField;
    @FXML private TextArea editMeaningArea;
    @FXML private Label savedLabel;
    @FXML private Button backToMainUIButton;
    @FXML private Label EscLabel;
    @FXML private Label EnterLabel;
    @FXML private Label EscLabelInEdit;
    @FXML private Label CtrlELabel;
    private String oldKeyWord = "";
    private String selectedWord = "";
    private String firstWord;
    private String firstMeaning;
    private String lastSavedWord;
    private String lastSavedMeaning;
    private String oldMeaning = "";
    private Timeline savedTimeLine;
    private Dictionary.Type searchingType = Dictionary.Type.EN_VI;
    private boolean removed = false;
    private boolean saved = false;
    private static UpdateWordSceneController instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setOnKeyPress();
        instance = this;

        searchingPane.setVisible(true);
        editingPane.setVisible(false);
        savedTimeLine = new Timeline(
                new KeyFrame(Duration.millis(1000), event -> {
                    savedLabel.setVisible(false);
                })
        );

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
                } else {
                    wordEnteringTimer.start();
                }
            }
        };

        wordEnteringTimer.start();
        searchingManagementTimer.start();
    }

    private void setOnKeyPress() {
        searchingPane.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE) backToMainUISceneFromSearchingPane();
            if(keyEvent.getCode() == KeyCode.ENTER) updateWord();
        });

        editingPane.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.E && keyEvent.isControlDown()) backToMainUISceneFromEditingPane();
            if(keyEvent.getCode() == KeyCode.ESCAPE) backToSelectWord();
        });

        wordEnteringField.setOnKeyPressed(keyEvent -> {
            if (!searchingResultList.getItems().isEmpty()) {
                if (keyEvent.getCode() == KeyCode.DOWN) {
                    searchingResultList.getSelectionModel().select(0);
                    searchingResultList.requestFocus();
                } else if (keyEvent.getCode() == KeyCode.ENTER) {
                    updateWord();
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
                    updateWord();
                } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    backToMainUISceneFromSearchingPane();
                }
            }
        });
    }


    public void updateWord() {
        //nếu ấn chọn từ ở listView thì sẽ sửa nghĩa của từ đó còn không thì sửa nghĩa của từ ở textField
        if(searchingResultList.getSelectionModel().getSelectedItem() != null) {
            selectedWord = searchingResultList.getSelectionModel().getSelectedItem();
        }

        if(selectedWord.isEmpty() || (searchingType == Dictionary.Type.EN_VI && !EnViDictionary.getInstance().getKeyWords().contains(selectedWord))) {
            showInvalidOldWordAlert();
        }
        else if(searchingType == Dictionary.Type.VI_EN && !ViEnDictionary.getInstance().getKeyWords().contains(selectedWord)) {
            showInvalidOldWordAlert();
        } else {
            searchingPane.setVisible(false);
            editingPane.setVisible(true);
            savedLabel.setVisible(false);

            String meaning = DictionaryManagementForApp.getMeaning(selectedWord, searchingType);
            meaning = WordWork.getRidOfHTMLForm(meaning);

            editWordField.setText(selectedWord);
            editMeaningArea.setText(meaning);
            firstWord = selectedWord;
            firstMeaning = meaning;

            lastSavedWord = selectedWord;
            lastSavedMeaning = meaning;

            checkDifferenceInMeaning();
        }
    }

    public void updateWordFromMainUI(String selectedWordUI, Dictionary.Type Type) {
        String meanings = DictionaryManagementForApp.getMeaning(selectedWordUI, Type);

        searchingPane.setVisible(false);
        editingPane.setVisible(true);
        savedLabel.setVisible(false);

        editWordField.setText(selectedWordUI);
        editMeaningArea.setText(WordWork.getRidOfHTMLForm(meanings));
        firstWord = selectedWordUI;
        firstMeaning = WordWork.getRidOfHTMLForm(meanings);

        lastSavedWord = firstWord;
        lastSavedMeaning = firstMeaning;
        checkDifferenceInMeaning();
    }

    public void saveNewMeaning() {
        String newWord = editWordField.getText();
        String newMeaning = editMeaningArea.getText();
        lastSavedWord = newWord;
        lastSavedMeaning = newMeaning;
        newMeaning = WordWork.toHTMLForm(newMeaning);

        if(searchingType == Dictionary.Type.EN_VI) {
            DictionaryManagementForApp.remove(selectedWord, Dictionary.Type.EN_VI);
            DictionaryManagementForApp.insert(newWord, newMeaning, Dictionary.Type.EN_VI);
        }
        else {
            DictionaryManagementForApp.remove(selectedWord, Dictionary.Type.VI_EN);
            DictionaryManagementForApp.insert(newWord, newMeaning, Dictionary.Type.VI_EN);
        }
        saved = true;
        savedLabel.setVisible(true);
        savedTimeLine.play();
    }

    public void removeWordFunc() {
        String word = editWordField.getText();
        if ((searchingType == Dictionary.Type.EN_VI && EnViDictionary.getInstance().getKeyWords().contains(word))
            || (searchingType == Dictionary.Type.VI_EN && ViEnDictionary.getInstance().getKeyWords().contains(word))) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xóa từ");
            alert.setHeaderText("Bạn có muốn xóa từ này không?");
            ButtonType ok = new ButtonType("Có");
            ButtonType cancel = new ButtonType("Không");
            alert.getButtonTypes().setAll(ok, cancel);
            if (alert.showAndWait().get() == ok) {
                if(searchingType == Dictionary.Type.EN_VI) {
                    DictionaryManagementForApp.remove(word, Dictionary.Type.EN_VI);
                }
                else {
                    DictionaryManagementForApp.remove(word, Dictionary.Type.VI_EN);
                }
                editWordField.clear();
                editMeaningArea.clear();
                meaningWebView.getEngine().loadContent("");
                removed = true;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không tìm thấy từ");
            alert.setHeaderText("Từ bạn nhập không hợp lệ, vui lòng nhập lại");
            alert.show();
        }
    }

    public void backToSelectWord() {
        String newWord = editWordField.getText();
        String newMeaning = editMeaningArea.getText();
        if((!lastSavedWord.equals(newWord) || !lastSavedMeaning.equals(newMeaning)) && !removed) {
            showSaveNewMeaningAlert(newWord, newMeaning);
        }
        wordEnteringField.clear();
        searchingPane.setVisible(true);
        searchingResultList.getSelectionModel().clearSelection();
        editingPane.setVisible(false);
        removed = false;
        saved = false;
    }

    public void backToMainUISceneFromEditingPane() {
        String newWord = editWordField.getText();
        String newMeaning = editMeaningArea.getText();
        if((!lastSavedWord.equals(newWord) || !lastSavedMeaning.equals(newMeaning)) && !removed) {
            showSaveNewMeaningAlert(newWord, newMeaning);
        }
        removed = false;
        saved = false;
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }
    public void backToMainUISceneFromSearchingPane() {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
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
        alert.setTitle("Các chỉnh sửa của bạn chưa được lưu");
        alert.setHeaderText("Bạn có muốn lưu các chỉnh sửa không?");
        ButtonType ok = new ButtonType("Lưu");
        ButtonType cancel = new ButtonType("Không lưu");
        alert.getButtonTypes().setAll(ok, cancel);
        ButtonType userChoice = alert.showAndWait().get();
        if(userChoice == ok) {
            if(searchingType == Dictionary.Type.EN_VI) {
                EnViDictionary.getInstance().getWords().remove(selectedWord);
                EnViDictionary.getInstance().getKeyWords().remove(selectedWord);
                EnViDictionary.getInstance().getWords().put(newWord, newMeaning);
                EnViDictionary.getInstance().getKeyWords().add(newWord);
            }
            else {
                ViEnDictionary.getInstance().getWords().remove(selectedWord);
                ViEnDictionary.getInstance().getKeyWords().remove(selectedWord);
                ViEnDictionary.getInstance().getWords().put(newWord, newMeaning);
                ViEnDictionary.getInstance().getKeyWords().add(newWord);
            }
        }
    }


    private void checkDifferenceInMeaning() {
        AnimationTimer scrollTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                meaningWebView.getEngine().executeScript("document.body.scrollTop=" + editMeaningArea.scrollTopProperty().get() + ";");
            }
        };

        AnimationTimer checkTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!oldMeaning.equals(editMeaningArea.getText())) {
                    oldMeaning = editMeaningArea.getText();
                    meaningWebView.getEngine().loadContent(WordWork.toHTMLMeaningStyle(WordWork.toHTMLForm(oldMeaning)));
                    scrollTimer.start();
                } else {
                    scrollTimer.stop();
                }
            }
        };

        checkTimer.start();
    }

    public void onSearchingTypeChanged() {
        oldKeyWord = "";
        searchingResultList.setVisible(false);
        if (searchingType == Dictionary.Type.EN_VI) {
            searchingType = Dictionary.Type.VI_EN;
            searchingTypeButton.setText("VIỆT - ANH");
        } else if (searchingType == Dictionary.Type.VI_EN) {
            searchingType = Dictionary.Type.EN_VI;
            searchingTypeButton.setText("ANH - VIỆT");
        }
    }

    public void recoverMeaning() {
        editWordField.setText(firstWord);
        editMeaningArea.setText(firstMeaning);
    }

    public void showEsc() {
        EscLabel.setVisible(true);
    }
    public void hideEsc() {
        EscLabel.setVisible(false);
    }
    public void showEnter() {
        EnterLabel.setVisible(true);
    }
    public void hideEnter() {
        EnterLabel.setVisible(false);
    }
    public void showEscInEdit() {
        EscLabelInEdit.setVisible(true);
    }
    public void hideEscInEdit() {
        EscLabelInEdit.setVisible(false);
    }
    public void showCtrlE() {
        CtrlELabel.setVisible(true);
    }
    public void hideCtrlE() {
        CtrlELabel.setVisible(false);
    }
}