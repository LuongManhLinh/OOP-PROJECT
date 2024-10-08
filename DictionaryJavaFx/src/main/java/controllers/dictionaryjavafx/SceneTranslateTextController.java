package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import classes.googlework.GgTranslateAPI;
import classes.googlework.GgTranslateTextToSpeech;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SceneTranslateTextController implements Initializable{
    @FXML private AnchorPane anchorPane;
    @FXML private TextArea textTranslateArea;
    @FXML private TextArea textResultArea;
    @FXML private ChoiceBox<String> langFromChoiceBox;
    @FXML private ChoiceBox<String> langToChoiceBox;
    @FXML private Label errorLabel;
    @FXML private Label EscLabel;
    private Timeline errorTimeLine;

    private String langFrom;
    private String langTo;
    private static SceneTranslateTextController instance;

    public static final String[] languages = {
            "Tiếng Việt",
            "Tiếng Anh",
            "Tiếng Pháp",
            "Tiếng Nhật",
            "Tiếng Đức",
            "Tiếng Nga",
            "Tiếng Tây Ban Nha",
            "Tiếng Trung"
    };

    public static final HashMap<String, String> symbol = new HashMap<>();
    public static SceneTranslateTextController getInstance() {
        return instance;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        setOnKeyPress();
        instance = this;
        symbol.put(languages[0], "vi");
        symbol.put(languages[1], "en");
        symbol.put(languages[2], "fr");
        symbol.put(languages[3], "ja");
        symbol.put(languages[4], "de");
        symbol.put(languages[5], "ru");
        symbol.put(languages[6], "es");
        symbol.put(languages[7], "zh");

        langFromChoiceBox.getItems().setAll(languages);
        langToChoiceBox.getItems().setAll(languages);

        langFromChoiceBox.setOnAction(event -> {
            langFrom = symbol.get(langFromChoiceBox.getValue());
        });

        langToChoiceBox.setOnAction(event -> {
            langTo = symbol.get(langToChoiceBox.getValue());
        });

        langFromChoiceBox.getSelectionModel().select(1);
        langToChoiceBox.getSelectionModel().select(0);

        errorTimeLine = new Timeline(
                new KeyFrame(Duration.millis(1000), event -> {
                    errorLabel.setVisible(false);
                })
        );
    }

    private void setOnKeyPress() {
        anchorPane.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ESCAPE) backToMainUIScene();
        });
    }

    public void translate() {
        String paragraph = textTranslateArea.getText();
        if (!paragraph.isEmpty()) {
            if (langFrom.equals(langTo)) {
                textResultArea.setText(paragraph);
            } else {
                String translateParagraph = GgTranslateAPI.translate(langFrom, langTo, paragraph);
                if (translateParagraph.equals(GgTranslateAPI.errorString)) {
                    logError();
                } else {
                    textResultArea.setText(translateParagraph);
                }
            }
        }
    }

    public void speakLangFromText() {
        String paragraph = textTranslateArea.getText();
        if (paragraph != null && !paragraph.isEmpty()) {
            String response = GgTranslateTextToSpeech.play(paragraph, langFrom);
            if (response.equals(GgTranslateTextToSpeech.errorString)) {
                logError();
            }
        }
    }

    public void speakLangToText() {
        String paragraph = textResultArea.getText();
        if (paragraph != null && !paragraph.isEmpty()) {
            String response = GgTranslateTextToSpeech.play(paragraph, langTo);
            if (response.equals(GgTranslateTextToSpeech.errorString)) {
                logError();
            }
        }
    }

    public void backToMainUIScene() {
        GgTranslateTextToSpeech.stop();
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }

    public void setTextAndLang(String text, String langFrom, String langTo) {
        textTranslateArea.setText(text);
        langFromChoiceBox.getSelectionModel().select(langFrom);
        langToChoiceBox.getSelectionModel().select(langTo);
    }

    private void logError() {
        errorLabel.setVisible(true);
        errorTimeLine.play();
    }

    public void showEsc() {
        EscLabel.setVisible(true);
    }
    public void hideEsc() {
        EscLabel.setVisible(false);
    }
}
