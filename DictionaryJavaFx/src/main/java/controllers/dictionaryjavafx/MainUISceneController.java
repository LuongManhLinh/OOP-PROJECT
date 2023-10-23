package controllers.dictionaryjavafx;

import classes.DictionaryManagementForApp;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static classes.Dictionary.Words;

public class MainUISceneController implements Initializable {
    @FXML private TextField wordEnteringField;
    @FXML private ListView<String> searchingResultList;
    @FXML private ChoiceBox<String> myChoiceBox;
    @FXML WebView webView;

    private String[] functions = {"Show All Words", "Look Up", "Insert Words", "Play Game"};

    private String oldKeyWord = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(functions);
        myChoiceBox.setOnAction(this::selectFunctions);

        searchingResultList.setVisible(false);
        webView.setVisible(false);
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
                oldKeyWord = keyWord;
//                System.out.println(oldKeyWord);

                ArrayList<String> searchingResult = DictionaryManagementForApp.lookUp(keyWord);
                if (!searchingResult.isEmpty()) {
                    searchingResultList.setVisible(true);
                    searchingResultList.getItems().setAll(searchingResult);
                    // điều chỉnh khung nhìn chỉ đủ để hiển thị kết quả
                    // nếu số kết quả lớn hơn 10 thì cũng chỉ hiển thị 10
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
                    System.out.println("else");
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
                searchingResultList.setOnMouseClicked(mouseEvent ->
                {
                    if(mouseEvent.getClickCount() == 2) {
                        webView.setVisible(true);
                        String selectedWord = searchingResultList.getSelectionModel().getSelectedItem();
                        if(selectedWord != null) {
                            String meanings = Words.get(selectedWord);
                            WebEngine webEngine = webView.getEngine();
                            webEngine.loadContent(meanings);
                        }
                    }
                });
            }
        };

        wordEnteringTimer.start();
        searchingManagementTimer.start();

    }
    public void selectFunctions(ActionEvent event) {
        String myChoice = myChoiceBox.getValue();
        if(myChoice.equals("Insert Words")) {
            try {
                SceneLoaderController.loadScene(event, "InsertScene.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(myChoice.equals("Play Game")) {
            try {
                SceneLoaderController.loadScene(event, "ExperimentGameScene.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
