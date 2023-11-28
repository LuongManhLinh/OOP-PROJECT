package controllers.dictionaryjavafx;

import classes.*;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class InsertWordsSceneController implements Initializable {
    @FXML private AnchorPane anchorPane;
    @FXML private Button backToMainUIButton;
    @FXML private Button AddWordButton;
    @FXML private Label EscLabel;
    @FXML private Button addingTypeButton;
    @FXML private TextField enterWordField;
    @FXML private TextArea enterMeaningArea;
    @FXML private Label warningLabel;

    private Dictionary.Type addingType = Dictionary.Type.EN_VI;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EscLabel.setVisible(false);
        setOnKeyPress();

        enterWordField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.DOWN) {
                enterMeaningArea.requestFocus();
            }
        });

        AnimationTimer checkTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (enterWordField.isFocused()) {
                    String text = enterWordField.getText().trim();
                    if (!text.isEmpty()) {
                        if (addingType == Dictionary.Type.EN_VI) {
                            if (EnViDictionary.getInstance().getKeyWords().contains(text)) {
                                startWarning();
                            } else {
                                stopWarning();
                            }
                        } else if (addingType == Dictionary.Type.VI_EN) {
                            if (ViEnDictionary.getInstance().getKeyWords().contains(text)) {
                                startWarning();
                            } else {
                                stopWarning();
                            }
                        }
                    } else {
                        stopWarning();
                    }
                }

            }
        };

        checkTimer.start();
    }

    private void setOnKeyPress() {
        final KeyCodeCombination backToMainUI = new KeyCodeCombination(KeyCode.ESCAPE);
        anchorPane.setOnKeyPressed(keyEvent -> {
            if(backToMainUI.match(keyEvent)) backToMainUIButton.fire();
        });
    }


    public void enterWordAndMeaning(ActionEvent event) {
        String word = enterWordField.getText();
        String wordMeaning = enterMeaningArea.getText();
        if(word.isEmpty() || wordMeaning.isEmpty()) {
            if(word.isEmpty()) showWordIsEmptyAlert();
            else showMeaningIsEmptyAlert(word);
        }
        else {
            if(addingType == Dictionary.Type.EN_VI) {
                if(EnViDictionary.getInstance().getKeyWords().contains(word)) {
                    showContainAlert();
                }
                else {
                    DictionaryManagementForApp.insert(word, wordMeaning, Dictionary.Type.EN_VI);
                    showConfirmationAlert(word, event);
                }
            }
            else {
                if(ViEnDictionary.getInstance().getKeyWords().contains(word)) {
                    showContainAlert();
                }
                else {
                    DictionaryManagementForApp.insert(word, wordMeaning, Dictionary.Type.VI_EN);
                    showConfirmationAlert(word, event);
                }
            }
        }
    }

    private void showWordIsEmptyAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText("Phần từ không được để rỗng");
        alert.setContentText("Vui lòng nhập từ bạn muốn thêm vào!");
        alert.showAndWait();
    }

    private void showMeaningIsEmptyAlert(String word) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText("Phần nghĩa không được để rỗng");
        alert.setContentText("Vui lòng nhập nghĩa cho từ '" + word + "'!");
        alert.showAndWait();
    }

    private void showContainAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText("Từ đã tồn tại trong từ điển");
        alert.setContentText("Vui lòng nhập từ khác!");
        alert.showAndWait();
    }

    private void showConfirmationAlert(String word, ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Xong!");
        alert.setHeaderText("Bạn đã thêm '" + word + "' vào từ điển");
        alert.setContentText("Vui lòng nhấn OK để tiếp tục thêm từ");

        alert.showAndWait();

        enterWordField.clear();
        enterMeaningArea.clear();

    }

    public void onAddingTypeChanged(ActionEvent event) {
        if (addingType == Dictionary.Type.EN_VI) {
            addingType = Dictionary.Type.VI_EN;
            addingTypeButton.setText("VIỆT - ANH");
        } else if (addingType == Dictionary.Type.VI_EN) {
            addingType = Dictionary.Type.EN_VI;
            addingTypeButton.setText("ANH - VIỆT");
        }
    }

    public void showEsc() {
        EscLabel.setVisible(true);
    }
    public void hideEsc() {
        EscLabel.setVisible(false);
    }

    public void backToMainUIScene(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }

    public void startWarning() {
        warningLabel.setVisible(true);
        enterWordField.setStyle("-fx-border-color:red;");
    }

    private void stopWarning() {
        warningLabel.setVisible(false);
        enterWordField.setStyle(null);
    }
}