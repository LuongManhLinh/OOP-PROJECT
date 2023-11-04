package controllers.dictionaryjavafx;

import classes.Dictionary;
import classes.EnViDictionary;
import classes.FXMLFiles;
import classes.ViEnDictionary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InsertWordsSceneController implements Initializable {
    @FXML private Button BacktoMainUIButton;
    @FXML private Button AddWordButton;
    @FXML private Button addingTypeButton;
    @FXML private TextField enterWordField;
    @FXML private TextArea enterMeaningArea;

    private Dictionary.Type addingType = Dictionary.Type.EN_VI;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                    EnViDictionary.getInstance().getKeyWords().add(word);
                    EnViDictionary.getInstance().getWords().put(word, wordMeaning);
                    showConfirmationAlert(word, event);
                }
            }
            else {
                if(ViEnDictionary.getInstance().getKeyWords().contains(word)) {
                    showContainAlert();
                }
                else {
                    ViEnDictionary.getInstance().getKeyWords().add(word);
                    ViEnDictionary.getInstance().getWords().put(word, wordMeaning);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xong!");
        alert.setHeaderText("Bạn đã thêm '" + word + "' vào từ điển");
        alert.setContentText("Vui lòng nhấn OK để tiếp tục thêm từ \n hoặc nhấn Cancel để quay lại tìm kiếm!");

        ButtonType selectedButton = alert.showAndWait().get();

        if(selectedButton == ButtonType.CANCEL) {
            backToMainUIScene(event);
        }
        else if(selectedButton == ButtonType.OK) {
            enterWordField.clear();
            enterMeaningArea.clear();
        }
    }

    public void onAddingTypeChanged(ActionEvent event) {
        if (addingType == Dictionary.Type.EN_VI) {
            addingType = Dictionary.Type.VI_EN;
            addingTypeButton.setText("VIỆT-ANH");
        } else if (addingType == Dictionary.Type.VI_EN) {
            addingType = Dictionary.Type.EN_VI;
            addingTypeButton.setText("ANH-VIỆT");
        }
    }

    public void backToMainUIScene(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }
}