
package controllers.dictionaryjavafx;

import classes.googlework.GgTranslateAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

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
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
//        textTranslate.setTextFormatter();
    }
    public void translateFunc(ActionEvent event) throws IOException {
        String paragraph = textTranslate.getText();
        GgTranslateAPI ggTranslateAPI = new GgTranslateAPI();
        String translateParagraph = ggTranslateAPI.translate("en", "vi", paragraph);
        label.setText(translateParagraph);
    }
}
