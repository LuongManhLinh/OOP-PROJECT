package classes.ShootingGameClasses;

import controllers.dictionaryjavafx.ShootingGameController;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

public class ObjectView {
    private Label showText;
    private ImageView object;

    public ObjectView(String showTextString, Image objectImage) {
        this.showText = new Label(showTextString);
        this.object = new ImageView(objectImage);

        showText.setGraphic(object);
        showText.setContentDisplay(ContentDisplay.BOTTOM);
        showText.setTextAlignment(TextAlignment.CENTER);
        showText.setWrapText(true);
        showText.setMaxWidth(100);

        object.setPreserveRatio(true);
        object.setFitWidth(100);
    }

    public Label getShowText() {
        return showText;
    }

    public void setShowText(Label showText) {
        this.showText = showText;
    }

    public ImageView getObject() {
        return object;
    }

    public void setObject(ImageView object) {
        this.object = object;
    }

    public void setPosition(Vector position) {
        showText.setLayoutX(position.getX());
        showText.setLayoutY(position.getY());
    }

    public Vector getPosition() {
        return new Vector(showText.getLayoutX(), showText.getLayoutY());
    }
}
