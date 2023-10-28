
package controllers.dictionaryjavafx;
import classes.FXMLFiles;
import classes.googlework.GgTranslateAPI;
import classes.googlework.GgTranslateTextToSpeech;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class SceneTranslateTextController implements Initializable{
    @FXML
    private Label label;
    @FXML
    private TextArea textTranslate;
    @FXML
    private Pane pane;
    @FXML
    private Button translate;
    @FXML
    private ImageView imageSpeaker;
    @FXML private Button backButton;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
    }
    public void translateFunc(ActionEvent event) {
        String paragraph = textTranslate.getText();
        String translateParagraph = GgTranslateAPI.translate("en", "vi", paragraph);
        label.setText(translateParagraph);
    }
    public void speakerFunc(MouseEvent event) {
        String paragraph = textTranslate.getText();
        GgTranslateTextToSpeech.play(paragraph, "en");
    }

    public void backToMainUIScene(ActionEvent event) {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }
}
