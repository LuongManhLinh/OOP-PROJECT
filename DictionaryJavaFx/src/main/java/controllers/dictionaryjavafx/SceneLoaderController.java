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
//        Node node = (Node) event.getSource();
//        Stage stage = (Stage) node.getScene().getWindow();
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        stage.close();
    }
    public static void loadScene(ActionEvent event, String dotFxmlFile) throws IOException {
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(dotFxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
