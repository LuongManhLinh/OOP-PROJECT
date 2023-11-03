package controllers.dictionaryjavafx;

import classes.Dictionary;
import classes.DictionaryManagementForApp;
import classes.FXMLFiles;
import classes.dictionarycommandline.DictionaryExecution;
import classes.dictionarycommandline.DictionaryManagement;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SceneRemoveWordController implements Initializable {
    @FXML
    private TextField enterWordField;
    @FXML
    private Button removeButton;
    @FXML
    private ListView<String> viewRemoveText;
    private String keyCurrent = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewRemoveText.setVisible(false);
//        viewRemoveText.setMaxHeight(10 * viewRemoveText.getFixedCellSize());

        viewRemoveText.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //cài đặt hiển thị nghĩa
                String selectedWord = viewRemoveText.getSelectionModel().getSelectedItem();
                if (selectedWord == null) {
                    keyCurrent = "";
                } else
                    keyCurrent = selectedWord;
                enterWordField.setText(selectedWord);
            }
        });
        handleSearching();
    }

    private void handleSearching() {
        // tạo ra vòng lặp để tìm kiếm
        AnimationTimer wordEnteringTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String keyWord = enterWordField.getText();
                if (keyWord == null)
                    keyCurrent = "";
                else
                    keyCurrent = keyWord;

                ArrayList<String> searchingResult = DictionaryManagementForApp.lookUp(keyWord, Dictionary.Type.EN_VI);
                if (!searchingResult.isEmpty()) {
                    viewRemoveText.setVisible(true);
                    viewRemoveText.getItems().setAll(searchingResult);
                    // điều chỉnh khung nhìn chỉ đủ để hiển thị kết quả
                    // nếu số kết quả lớn hơn 10 thì cũng chỉ hiển thị 10
//                    viewRemoveText.setPrefHeight(searchingResult.size() * viewRemoveText.getFixedCellSize());
                } else {
                    viewRemoveText.setVisible(false);
                }
            }
        };

        // vòng lặp để quản lí sự tìm kiếm của vòng lặp trên
        AnimationTimer searchingManagementTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                // nếu đang tương tác với kết quả hay không thay đổi chuỗi tìm kiếm thì sẽ dừng tìm kiếm
                if (viewRemoveText.isFocused() || keyCurrent.equals(enterWordField.getText())) {
                    wordEnteringTimer.stop();
                } else {
                    wordEnteringTimer.start();

                    // xử lí để khi nhấn enter thì nhảy xuống danh sách kết quả
                    if (enterWordField.isFocused()) {
                        Scene scene = enterWordField.getScene();
                        scene.setOnKeyPressed(event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                if (!viewRemoveText.getItems().isEmpty()) {
                                    viewRemoveText.requestFocus();
                                    viewRemoveText.getSelectionModel().select(0);
                                }
                            }
                        });
                    }
                }
            }
        };

        wordEnteringTimer.start();
        searchingManagementTimer.start();
    }

    public void removeWordFunc(ActionEvent event) {
        String word = enterWordField.getText();
        word = word.toLowerCase().trim();
        if (DictionaryManagement.keyWords.contains(word)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xóa từ");
            alert.setHeaderText("Bạn có muốn xóa từ này không?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                DictionaryManagement.keyWords.remove(word);
                DictionaryManagement.words.remove(word);
                viewRemoveText.setVisible(false);
                enterWordField.setText("");
                keyCurrent = enterWordField.getText();
                viewRemoveText.getItems().clear();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không tìm thấy từ");
            alert.setHeaderText("Từ bạn nhập không hợp lệ, Vui lòng nhập lại");
            alert.show();
        }
    }

    public void backToMainUIScene(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }
}