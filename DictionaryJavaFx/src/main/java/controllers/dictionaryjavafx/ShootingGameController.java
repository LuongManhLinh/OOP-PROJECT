package controllers.dictionaryjavafx;

import classes.ShootingGameClasses.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class ShootingGameController implements Initializable {
    @FXML
    private AnchorPane gamePane;
    @FXML
    private ImageView CannonImage;
    @FXML
    private Line line;

    private static final int SPIN_POINT_X = 30;
    private static final int SPIN_POINT_Y = 672;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObjectImages.loadData();
        setCannonRotation();
    }

    public void setCannonRotation() {
        gamePane.setOnMouseMoved(event -> {
            //vị trí của chuột
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            double vectorX = mouseX - SPIN_POINT_X;
            double vectorY = mouseY - SPIN_POINT_Y;

            double length = Math.sqrt(vectorX * vectorX + vectorY * vectorY);
            double rate = 90 / length;

            vectorX *= rate;
            vectorY *= rate;

            double angleRadians = Math.atan2(vectorY, vectorX);
            double angleDegrees = Math.toDegrees(angleRadians);

            CannonImage.setRotate(angleDegrees);

            line.setStartX(vectorX + SPIN_POINT_X);
            line.setStartY(vectorY + SPIN_POINT_Y);
            line.setEndX(mouseX);
            line.setEndY(mouseY);

            // Hiển thị đoạn thẳng nét đứt
            line.getStrokeDashArray().addAll(5d, 5d);
        });
    }
}
