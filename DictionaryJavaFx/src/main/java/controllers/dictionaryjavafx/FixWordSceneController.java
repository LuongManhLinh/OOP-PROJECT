package controllers.dictionaryjavafx;

import classes.Dictionary;
import classes.DictionaryManagementForApp;
import classes.EnViDictionary;
import classes.FXMLFiles;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class FixWordSceneController implements Initializable {
    @FXML private Button backToMainUIButton;
    @FXML private Button backToSelectWordButton;
    @FXML private Button fixWordButton;
    @FXML private Button saveButton;
    @FXML private TextField enterNewWordField;
    @FXML private TextField enterWordField;
    @FXML private ListView<String> searchingResultList;
    @FXML private Label label;

    private String oldKeyWord = "";
    private String oldWord = "";
    private String originalLabel = "Từ cần sửa là: ";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        saveButton.setVisible(false);
        enterNewWordField.setVisible(false);
        backToSelectWordButton.setVisible(false);
        label.setVisible(false);

        // khung nhìn hiển thị được tối đa 10 kết quả, nếu nhiều hơn phải cuộn xuống để xem
        searchingResultList.setMaxHeight(10 * searchingResultList.getFixedCellSize());
        handleSearching();
    }

    private void handleSearching() {
        // tạo ra vòng lặp để tìm kiếm
        AnimationTimer wordEnteringTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                String keyWord = enterWordField.getText();
                oldKeyWord = keyWord;
                oldWord = keyWord;

                ArrayList<String> searchingResult = DictionaryManagementForApp.lookUp(keyWord, Dictionary.Type.EN_VI);
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
                if (searchingResultList.isFocused() || oldKeyWord.equals(enterWordField.getText())) {
                    wordEnteringTimer.stop();
                } else {
                    oldWord = enterWordField.getText();
                    wordEnteringTimer.start();

                    // xử lí để khi nhấn enter thì nhảy xuống danh sách kết quả
                    if (enterWordField.isFocused()) {
                        Scene scene = enterWordField.getScene();
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
        searchingManagementTimer.start();
    }

    //chọn từ cần sửa
    public void getOldWord(ActionEvent event) {
        //nếu không chọn từ ở phần ListView thì oldWord sẽ lấy ở phần TextField
        if(searchingResultList.getSelectionModel().getSelectedItem() != null) {
            oldWord = searchingResultList.getSelectionModel().getSelectedItem();
        }
        if(oldWord.isEmpty() || !EnViDictionary.getInstance().getKeyWords().contains(oldWord)) {
            showInvalidOldWordAlert();
        }
        //chuyển sang khu vực sửa từ
        else {
            backToMainUIButton.setVisible(false);
            backToSelectWordButton.setVisible(true);

            enterWordField.setVisible(false);
            fixWordButton.setVisible(false);
            searchingResultList.setVisible(false);

            enterNewWordField.setVisible(true);
            saveButton.setVisible(true);
            label.setText(originalLabel + oldWord);
            label.setVisible(true);
        }
    }

    public void saveNewWord(ActionEvent event) {
        String newWord = enterNewWordField.getText();
        if(newWord.isEmpty()) {
            showNewWordIsEmptyAlert();
        }
        else if(EnViDictionary.getInstance().getKeyWords().contains(newWord)) {
            showNewWordAlreadyExistsAlert();
        }

        else {
            //sửa trong phần hiển thị nghĩa
            String meaning = EnViDictionary.getInstance().getWords().get(oldWord);
            String newMeaning = meaning.replace("@" + oldWord, "@" + newWord);

            EnViDictionary.getInstance().getKeyWords().remove(oldWord);
            EnViDictionary.getInstance().getWords().remove(oldWord);

            EnViDictionary.getInstance().getKeyWords().add(newWord);
            EnViDictionary.getInstance().getWords().put(newWord, newMeaning);
            showWordIsSavedAlert(newWord);
        }
    }

    //ấn nút quay lại ở khu vực sửa từ sẽ quay lại khu vực chọn từ
    public void setBackToSelectWordButton() {
        backToMainUIButton.setVisible(true);
        backToSelectWordButton.setVisible(false);

        enterWordField.clear();
        enterWordField.setVisible(true);
        fixWordButton.setVisible(true);
        searchingResultList.setVisible(false);

        enterNewWordField.setVisible(false);
        saveButton.setVisible(false);
        label.setText(originalLabel);
        label.setVisible(false);
    }

    private void showInvalidOldWordAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText("Từ bạn chọn không hợp lệ hoặc không nằm trong từ điển");
        alert.setContentText("Vui lòng nhập từ hợp lệ");
        alert.showAndWait();
    }

    private void showNewWordIsEmptyAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText("Không được để rỗng");
        alert.setContentText("Vui lòng nhập từ mới");
        alert.showAndWait();
    }

    private void showNewWordAlreadyExistsAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText("Từ đã tồn tại trong từ điển");
        alert.setContentText("Vui lòng nhập từ mới");
        alert.showAndWait();
    }

    private void showWordIsSavedAlert(String newWord) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Đã lưu");
        alert.setHeaderText("Bạn đã sửa thành công từ '" + oldWord + "' thành '" + newWord + "'!");
        alert.showAndWait();
    }


    public void backToMainUIScene() {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }
}
