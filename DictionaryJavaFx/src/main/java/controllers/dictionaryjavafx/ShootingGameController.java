package controllers.dictionaryjavafx;

import classes.ShootingGameClasses.*;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ShootingGameController implements Initializable {
    @FXML private AnchorPane gamePane;
    @FXML private ImageView CannonImage;
    @FXML private Line line;
    @FXML private ImageView bullet;

    private static final int SPIN_POINT_X = 30;
    private static final int SPIN_POINT_Y = 671;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObjectImages.loadData();
        setCannonRotation();
        clickToShoot();
    }

    private void clickToShoot() {
        gamePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) { //click chuột trái
                shootBullet(event.getSceneX(), event.getSceneY() - bullet.getFitHeight() / 2);
            }
        });
    }

    private void shootBullet(double targetX, double targetY) {
        // Tạo một ImageView mới cho viên đạn
        ImageView newBullet = new Bullet(GameObject.Color.BLUE, "", "").getObjectView().getObject();
        newBullet.setLayoutX(bullet.getLayoutX());
        newBullet.setLayoutY(bullet.getLayoutY());
        newBullet.setFitWidth(bullet.getFitWidth());
        newBullet.setFitHeight(bullet.getFitHeight());
        gamePane.getChildren().add(newBullet);
        newBullet.toBack();

        double vectorX = targetX - SPIN_POINT_X;
        double vectorY = targetY - SPIN_POINT_Y;

        double length = Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        double rate = 90 / length;

        vectorX *= rate;
        vectorY *= rate;

        double angleRadians = Math.atan2(vectorY, vectorX);
        double angleDegrees = Math.toDegrees(angleRadians);

        newBullet.setRotate(angleDegrees);

        //điểm start là trung điểm topleft và bottomleft của đạn
        double startX = vectorX + SPIN_POINT_X;
        double startY = vectorY + SPIN_POINT_Y - newBullet.getFitHeight() / 2;

        // Tính toán hướng từ vị trí hiện tại đến vị trí click chuột
        double directionX = targetX - startX;
        double directionY = targetY - startY;

        // Chuẩn hóa hướng để vector có chiều dài là 1
        directionX /= length;
        directionY /= length;

        // cho điểm đích xa ra khỏi scene
        double distantTargetX = startX + directionX * 2000;
        double distantTargetY = startY + directionY * 2000;

        // thời gian di chuyển đến distantTarget
        double duration = 5.0;

        //đích
        TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), newBullet);
        transition.setToX(distantTargetX - startX);
        transition.setToY(distantTargetY - startY);

        transition.setOnFinished(e -> gamePane.getChildren().remove(newBullet));

        transition.play();
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
