package classes.ShootingGameClasses;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ObjectView {
    private Label showText;
    private ImageView object;

    public ObjectView(Label showText, ImageView object) {
        this.showText = showText;
        this.object = object;
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
}
