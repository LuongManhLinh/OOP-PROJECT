package controllers.dictionaryjavafx;

import classes.EnViDictionary;
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
    @FXML private TextField enterWordField;
    @FXML private TextArea enterMeaningArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void enterWordAndMeaning(ActionEvent event) throws IOException {
        String word = enterWordField.getText();
        String wordMeaning = enterMeaningArea.getText();
        if(word.isEmpty() || wordMeaning.isEmpty()) {
            if(word.isEmpty()) showWordIsEmptyAlert();
            else showMeaningIsEmptyAlert(word);
        }
        else {
            if(EnViDictionary.getInstance().getKeyWords().contains(word)) {
                showContainAlert();
            }
            else {
                EnViDictionary.getInstance().getKeyWords().add(word);
                EnViDictionary.getInstance().getWords().put(word, wordMeaning);
                showConfirmationAlert(word, event);
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

    private void showConfirmationAlert(String word, ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xong!");
        alert.setHeaderText("Bạn đã thêm '" + word + "' vào từ điển");
        alert.setContentText("Vui lòng nhấn OK để tiếp tục thêm từ hoặc nhấn Cancel để quay lại tìm kiếm!");

        ButtonType selectedButton = alert.showAndWait().get();

        if(selectedButton == ButtonType.CANCEL) {
            backToMainUIScene(event);
        }
        else if(selectedButton == ButtonType.OK) {
            enterWordField.clear();
            enterMeaningArea.clear();
        }
    }

    public void backToMainUIScene(ActionEvent event) throws IOException {
        SceneLoaderController.loadScene(event, "MainUIScene.fxml");
    }
}