package controllers.dictionaryjavafx;

import classes.ShootingGameClasses.*;
import classes.makerandom.MakeRandom;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShootingGameController implements Initializable {
    @FXML private AnchorPane gamePane;
    @FXML private ImageView CannonImage;
    @FXML private Line line;
    @FXML private ImageView bullet;
    @FXML private VBox bulletContainer;

    private static final int SPIN_POINT_X = 30;
    private static final int SPIN_POINT_Y = 671;

    private static ShootingGameController instance;
    public static ShootingGameController getInstance() {
        return instance;
    }

    private static final List<Bullet> bullets = new ArrayList<>();
    private static final List<Target> targets = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        ObjectImages.loadData();
        setCannonRotation();
        clickToShoot();

        bulletContainer.getChildren().clear();

        // Test một cái di chuyển
        StraightMovingTarget t = new StraightMovingTarget(GameObject.Color.RED, "di chuyển", 1);
        gamePane.getChildren().add(0, t.getObjectView().getShowText());
        t.addDestination(new Vector(0, 0));
        t.addDestination(new Vector(0, 500));
        t.addDestination(new Vector(300, 200));
        t.addDestination(new Vector(1300, 700));
        t.setVelocity(500);
        t.move();

        RoundMovingTarget roundMovingTarget = new RoundMovingTarget(GameObject.Color.BLUE, "round", 1, new Vector(0, 0), 100);
        gamePane.getChildren().add(0, roundMovingTarget.getObjectView().getShowText());
        roundMovingTarget.setRoundVelocity(120);
        roundMovingTarget.setMoveVelocity(40);
        roundMovingTarget.setMoveDirection(new Vector(1, 1));
        roundMovingTarget.move();
    }

    public void addBullet() {
        if (bullets.size() < 8) {
            Bullet newBullet = new Bullet(GameObject.Color.RED, "haha", "hehe");
            bulletContainer.getChildren().add(newBullet.getObjectView().getShowText());
            bullets.add(newBullet);
        }
    }

    public void addTarget() {
        Target newTarget = new StillTarget(GameObject.Color.RED, "hehe", 1, new Vector(MakeRandom.random(0, 1300), MakeRandom.random(0, 700)));
        gamePane.getChildren().add(newTarget.getObjectView().getShowText());
        targets.add(newTarget);
    }

    public void removeBullet() {
        if (!bullets.isEmpty()) {
            bullets.remove(0);
            bulletContainer.getChildren().remove(0);
        }
    }

    public void removeTarget() {
        if (!targets.isEmpty()) {
            gamePane.getChildren().remove(targets.get(0).getObjectView().getShowText());
            targets.remove(0);
        }
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

            double rateForLine = 1500 / length;
            double lineEndX = rateForLine * vectorX;
            double lineEndY = rateForLine * vectorY;

            vectorX *= rate;
            vectorY *= rate;

            double angleRadians = Math.atan2(vectorY, vectorX);
            double angleDegrees = Math.toDegrees(angleRadians);

            CannonImage.setRotate(angleDegrees);



            line.setStartX(vectorX + SPIN_POINT_X);
            line.setStartY(vectorY + SPIN_POINT_Y);
            line.setEndX(lineEndX + SPIN_POINT_X);
            line.setEndY(lineEndY + SPIN_POINT_Y);

            // Hiển thị đoạn thẳng nét đứt
            line.getStrokeDashArray().addAll(5d, 5d);
        });
    }

    public Pane getPane() {
        return gamePane;
    }
    public VBox getBulletContainer() {
        return bulletContainer;
    }
}
