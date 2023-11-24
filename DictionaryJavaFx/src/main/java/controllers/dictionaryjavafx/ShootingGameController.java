package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import classes.ShootingGameClasses.*;
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

        for (int i = 0; i < 12; i++) {
            int colorRandom = MakeRandom.random(1, 3);
            GameObject.Color color = GameObject.Color.RED;
            switch (colorRandom) {
                case 1 -> color = GameObject.Color.RED;
                case 2 -> color = GameObject.Color.BLUE;
                case 3 -> color = GameObject.Color.GREEN;
            }
            StraightMovingTarget target = new StraightMovingTarget(color, "test", 1);

            int numberDis = MakeRandom.random(2, 5);
            for (int j = 0; j < numberDis; j++) {
                target.addDestination(new Vector(MakeRandom.random(0, 1200), MakeRandom.random(0, 600)));
            }
            target.setVelocity(MakeRandom.random(50, 500));
            addTarget(target);
        }


        gamePane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                changeScene(Status.START_GAME);
            }
        });

        changeScene(Status.START_GAME);
    }

    public void addBullet(Bullet bullet) {
        if (bullets.size() < 8) {
            bulletContainer.getChildren().add(bullet.getObjectView().getShowText());
            bullets.add(bullet);
        }
    }

    public void addTarget(Target target) {
        gamePane.getChildren().add(0, target.getObjectView().getShowText());
        targets.add(target);
    }

    public void removeBullet(Bullet bullet) {
        if (bullets.contains(bullet)) {
            bullets.remove(bullet);
            bulletContainer.getChildren().remove(bullet.getObjectView().getShowText());
        }
    }

    public void removeTarget(Target target) {
        if (targets.contains(target)) {
            targets.remove(target);
            gamePane.getChildren().remove(target.getObjectView().getShowText());
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
                for (Target target : targets) {
                    target.move();
                }
            }
            case END_GAME -> {

            }
        }
    }

}
