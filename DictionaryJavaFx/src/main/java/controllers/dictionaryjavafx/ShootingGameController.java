package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import classes.ShootingGameClasses.*;
import classes.data.GameData;
import classes.makerandom.MakeRandom;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShootingGameController implements Initializable {
    @FXML private AnchorPane menuPane;
    @FXML private AnchorPane gamePane;
    @FXML private ImageView CannonImage;
    @FXML private Line line;
    @FXML private VBox bulletContainer;
    @FXML private ImageView shootBulletRed;
    @FXML private ImageView shootBulletBlue;
    @FXML private ImageView shootBulletGreen;

    private enum Status {
        START_GAME,
        IN_GAME,
        END_GAME
    }

    private static final int SPIN_POINT_X = 30;
    private static final int SPIN_POINT_Y = 679;

    private static ShootingGameController instance;
    public static ShootingGameController getInstance() {
        return instance;
    }

    private static final List<Bullet> bullets = new ArrayList<>();
    private static final List<Target> targets = new ArrayList<>();

    private int numberShowingBullet = 0;
    private int numberShowingTarget = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        ObjectImages.loadData();
        setCannonRotation();
        clickToShoot();

        bulletContainer.getChildren().clear();

        gamePane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                changeScene(Status.START_GAME);
            }
        });

        GameData.loadShootingGameData();
        changeScene(Status.START_GAME);
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
        if (numberShowingBullet < 7) {
            bulletContainer.getChildren().add(bullet.getObjectView().getShowText());
            numberShowingBullet++;
        }
    }

    public void addTarget(Target target) {
        targets.add(target);
        if (targets.size() < 13) {
            gamePane.getChildren().add(0, target.getObjectView().getShowText());
            numberShowingTarget++;
        }
    }

    public void addBullet(ArrayList<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            addBullet(bullet);
        }
    }

    public void addTarget(ArrayList<Target> targets) {
        for (Target target : targets) {
            addTarget(target);
        }
    }

    public void removeBullet(Bullet bullet) {
        if (bullets.contains(bullet)) {
            bullets.remove(bullet);
            bulletContainer.getChildren().clear();
            int num = 0;
            for (Bullet value : bullets) {
                bulletContainer.getChildren().add(value.getObjectView().getShowText());
                num++;
                if (num >= 7) {
                    break;
                }
            }
            numberShowingBullet--;
        }
    }

    public void removeTarget(Target target) {
        if (targets.contains(target)) {
            targets.remove(target);
            gamePane.getChildren().remove(target.getObjectView().getShowText());
            numberShowingTarget--;
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
        if (color == GameObject.Color.RED) {
            newBullet = new ImageView(shootBulletRed.getImage());
        }
        else if (color == GameObject.Color.BLUE) {
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
                    for(Target t : targets) {
                        if(checkCollision(newBullet, t)) {
                            removeTarget(t);
                            countStep = numSteps;
                            break;
                        }
                    }
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

            double rateForLine = 2560 / length;
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

    public void onExit() {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }

    public void onPlay() {
        changeScene(Status.IN_GAME);
    }

    private void changeScene(Status status) {
        switch (status) {
            case START_GAME -> {
                menuPane.setVisible(true);
                gamePane.setVisible(false);
            }

            case IN_GAME -> {
                menuPane.setVisible(false);
                gamePane.setVisible(true);
                RoundGenerator.refillIndex();
                RoundGenerator.generateEnBullet_ViTarget(1);
                for (Target target : targets) {
                    target.move();
                }
            }

            case END_GAME -> {

            }
        }
    }
    public boolean checkCollision(ImageView bullet, Target target) {
        //hình tròn trong target
        double radius = target.getObjectView().getShowText().getWidth() / 2;
        double targetCenterX = target.getObjectView().getPosition().getX() + radius;
        double targetCenterY = target.getObjectView().getPosition().getY() + target.getObjectView().getShowText().getHeight() - radius;

        double bulletRotation = Math.toRadians(-bullet.getRotate());
        double diagonalLine = bullet.getFitWidth() / 2;
        double bulletHeadX = bullet.getLayoutX() + bullet.getFitWidth() / 2 + Math.cos(bulletRotation) * diagonalLine;
        double bulletHeadY = bullet.getLayoutY() + bullet.getFitHeight() / 2 - Math.sin(bulletRotation) * diagonalLine;
//        Circle bulletHead = new Circle(bulletHeadX, bulletHeadY, 1, Color.RED);
//        Circle targett = new Circle(targetCenterX, targetCenterY, radius, Color.BLACK);

        // Tạo Circle tượng trưng cho trung tâm đối tượng bullet và đặt màu sắc
//        Circle bulletCenter = new Circle(bullet.getLayoutX() + bullet.getFitWidth() / 2, bullet.getLayoutY() + bullet.getFitHeight() / 2, 3, Color.BLUE);
//        gamePane.getChildren().addAll(bulletHead, targett);
        return pointInsideCircle(bulletHeadX, bulletHeadY, targetCenterX, targetCenterY, radius);
    }

    private boolean pointInsideCircle(double pointX, double pointY, double centerX, double centerY, double radius) {
        double distance = Math.sqrt((centerX - pointX) * (centerX - pointX) + (centerY - pointY) * (centerY - pointY));
        return distance <= radius;
    }

    public void clear() {
        bulletContainer.getChildren().clear();
        bullets.clear();
        for (Target target : targets) {
            gamePane.getChildren().remove(target.getObjectView().getShowText());
        }
        targets.clear();
        numberShowingBullet = 0;
        numberShowingTarget = 0;
    }
}
