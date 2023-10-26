
package controllers.dictionaryjavafx;
import classes.ExperimentGameClasses.BottleImages;
import classes.googlework.GgTranslateAPI;
import classes.googlework.GgTranslateTextToSpeech;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
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

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
//        textTranslate.setTextFormatter();
        imageSpeaker.setImage(BottleImages.speakerText);
    }
    public void translateFunc(ActionEvent event) throws IOException {
        String paragraph = textTranslate.getText();
        GgTranslateAPI ggTranslateAPI = new GgTranslateAPI();
        String translateParagraph = ggTranslateAPI.translate("en", "vi", paragraph);
        label.setText(translateParagraph);
    }
    public void speakerFunc(MouseEvent event) throws IOException, JavaLayerException {
        String paragraph = textTranslate.getText();
        GgTranslateTextToSpeech ggTranslateTextToSpeech = new GgTranslateTextToSpeech();
        ggTranslateTextToSpeech.play(paragraph, "en");
    }
}
