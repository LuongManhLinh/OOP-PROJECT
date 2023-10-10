package com.example.dictionaryjavafxtest;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainUISceneController implements Initializable {
    @FXML private TextField wordEnteringField;
    @FXML private ListView<String> searchingResultList;

    private String oldKeyWord = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchingResultList.setVisible(false);
        // khung nhìn hiển thị được tối đa 10 kết quả, nếu nhiều hơn phải cuộn xuống để xem
        searchingResultList.setMaxHeight(10 * searchingResultList.getFixedCellSize());

        handleSearching();
    }

    private void handleSearching() {
        // tạo ra vòng lặp để tìm kiếm
        AnimationTimer wordEnteringTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String keyWord = wordEnteringField.getText();
                ArrayList<String> searchingResult = DictionaryManagementForApp.lookUp(keyWord);
                if (!searchingResult.isEmpty()) {
                    searchingResultList.setVisible(true);
                    searchingResultList.getItems().setAll(searchingResult);
                    oldKeyWord = keyWord;

                    // điều chỉnh khung nhìn chỉ đủ để hiển thị kết quả
                    // nếu số kết quả lớn hơn 10 thì cũng chỉ hiển thị 10
                    searchingResultList.setPrefHeight(searchingResult.size() * searchingResultList.getFixedCellSize());
                } else {
                    searchingResultList.setVisible(false);
                }
            }
        };

        // vòng lặp để quản lí sự tìm kiếm của vòng lặp trên
        AnimationTimer searchManagementTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                // nếu đang tương tác với kết quả hay không thay đổi chuỗi tìm kiếm thì sẽ dừng tìm kiếm
                if (searchingResultList.isFocused() || oldKeyWord.equals(wordEnteringField.getText())) {
                    wordEnteringTimer.stop();
                } else {
                    wordEnteringTimer.start();

                    // xử lí để khi nhấn enter thì nhảy xuống danh sách kết quả
                    if (wordEnteringField.isFocused()) {
                        Scene scene = wordEnteringField.getScene();
                        scene.setOnKeyPressed(event -> {
                            if (event.getCode() == KeyCode.ENTER) {
                                if (!searchingResultList.getItems().isEmpty()) {
                                    searchingResultList.requestFocus();
                                    searchingResultList.getSelectionModel().select(0);
                                }
                            }
                        });
                    }
                }
            }
        };

        wordEnteringTimer.start();
        searchManagementTimer.start();
    }
}
