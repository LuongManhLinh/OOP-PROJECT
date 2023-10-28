package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import classes.dictionarycommandline.DictionaryExecution;
import classes.dictionarycommandline.DictionaryManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneRemoveWordController implements Initializable {
    @FXML
    private TextField enterWordField;
    @FXML
    private Button removeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void removeWordFunc(ActionEvent event) {
        String word = enterWordField.getText();
        word = word.toLowerCase().trim();
        if (DictionaryManagement.keyWords.contains(word)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove word");
            alert.setHeaderText("Are you sure you want to delete this word?");
            if(alert.showAndWait().get() == ButtonType.OK) {
                DictionaryManagement.keyWords.remove(word);
                DictionaryManagement.words.remove(word);
                enterWordField.setText("");
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found");
            alert.setHeaderText("The word you entered is invalid, please try again");
            if(alert.showAndWait().get() == ButtonType.OK) {

            }
        }
    }

    public void backToMainUIScene(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }
}
