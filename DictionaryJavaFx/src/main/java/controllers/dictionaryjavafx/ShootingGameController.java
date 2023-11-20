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

            // Lấy tọa độ của cannon trong hệ tọa độ của scene
            double imageViewX = CannonImage.localToScene(CannonImage.getBoundsInLocal()).getMinX() + 90;
            double imageViewY = CannonImage.localToScene(CannonImage.getBoundsInLocal()).getMinY() + 37.5;

            // Tính toán góc xoay dựa trên sự khác biệt giữa vị trí di chuột và vị trí của cannon
            double angleRadians = Math.atan2(mouseY - imageViewY, mouseX - imageViewX);
            double angleDegrees = Math.toDegrees(angleRadians);

            // Điều chỉnh góc nếu nó vượt quá giới hạn
            double adjustedAngle = Math.floorMod((int) angleDegrees, 360);

            CannonImage.setRotate(adjustedAngle);

            Bounds bounds = CannonImage.getBoundsInParent();

            // Lấy trung điểm giữa topRight và bottomRight của CannonImage
            double topRightX = bounds.getMaxX();
            double topRightY = bounds.getMinY();

            double bottomRightX = bounds.getMaxX();
            double bottomRightY = bounds.getMaxY();

            double centerX = (topRightX + bottomRightX) / 2;
            double centerY = (topRightY + bottomRightY) / 2;

            line.setStartX(centerX);
            line.setStartY(centerY);
            line.setEndX(mouseX);
            line.setEndY(mouseY);

            // Hiển thị đoạn thẳng nét đứt
            line.getStrokeDashArray().addAll(5d, 5d);
        });
    }
}
