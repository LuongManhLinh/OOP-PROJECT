package controllers.dictionaryjavafx;

import classes.ShootingGameClasses.*;
import classes.makerandom.MakeRandom;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

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
    @FXML private ImageView shootBulletRed;
    @FXML private ImageView shootBulletBlue;
    @FXML private ImageView shootBulletGreen;

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
                shootBullet(GameObject.Color.GREEN, event.getSceneX(), event.getSceneY());
            }
        });
    }

    private void shootBullet(GameObject.Color color, double targetX, double targetY) {
        //tạo 1 viên đạn mới
        ImageView newBullet;
        if(color == GameObject.Color.RED) {
            newBullet = new ImageView(shootBulletRed.getImage());
        }
        else if(color == GameObject.Color.BLUE) {
            newBullet = new ImageView(shootBulletBlue.getImage());
        }
        else {
            newBullet = new ImageView(shootBulletGreen.getImage());
        }
        gamePane.getChildren().add(newBullet);
        newBullet.setLayoutX(shootBulletBlue.getLayoutX());
        newBullet.setLayoutY(shootBulletBlue.getLayoutY());
        newBullet.setFitWidth(shootBulletBlue.getFitWidth());
        newBullet.setFitHeight(shootBulletBlue.getFitHeight());
        newBullet.setRotate(shootBulletBlue.getRotate());
        newBullet.toBack();

        double vectorX = line.getEndX() - 18 - newBullet.getLayoutX();
        double vectorY = line.getEndY() - 4 - newBullet.getLayoutY();
        double distance = Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        double stepDistance = 7.0;

        int numSteps = (int) (distance / stepDistance);

        // khoảng cách di chuyển cho mỗi step
        double stepX = vectorX / numSteps;
        double stepY = vectorY / numSteps;

        AnimationTimer animationTimer = new AnimationTimer() {
            private int countStep = 0;

            @Override
            public void handle(long now) {
                if (countStep < numSteps) {
                    newBullet.setLayoutX(newBullet.getLayoutX() + stepX);
                    newBullet.setLayoutY(newBullet.getLayoutY() + stepY);
                    countStep++;
                } else {
                    gamePane.getChildren().remove(newBullet);
                    this.stop();
                }
            }
        };

        animationTimer.start();
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

            shootBulletBlue.setLayoutX(vectorX + SPIN_POINT_X - 18);
            shootBulletBlue.setLayoutY(vectorY + SPIN_POINT_Y - 4);
            shootBulletBlue.setRotate(angleDegrees);

            // Hiển thị đoạn thẳng nét đứt
            line.getStrokeDashArray().addAll(7d, 7d);
        });
    }

    public Pane getPane() {
        return gamePane;
    }
    public VBox getBulletContainer() {
        return bulletContainer;
    }
}
