package controllers.dictionaryjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoaderController {
    public static void exitApp(ActionEvent event)  {
        Stage stage;
        if (event.getSource() instanceof  Node) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else {
            stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        }
        stage.close();
    }

    public static void loadScene(ActionEvent event, String dotFxmlFile) {
        Stage stage;
        if (event.getSource() instanceof  Node) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else {
            stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(dotFxmlFile));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.show();
    }
}
