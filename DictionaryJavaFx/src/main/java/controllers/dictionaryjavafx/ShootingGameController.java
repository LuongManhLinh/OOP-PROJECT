package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import classes.ShootingGameClasses.*;
import classes.data.GameData;
import classes.makerandom.MakeRandom;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private static final int MAX_SHOWING_BULLET_SIZE = 6;


    private static ShootingGameController instance;

    private final Queue<Bullet> waitingBullets = new LinkedList<>();
    private final Queue<Target> waitingTargets = new LinkedList<>();

    private final List<Bullet> showingBullets = new ArrayList<>();
    private final List<Target> showingTargets = new ArrayList<>();

    private int round = 1;
    private int maxRound = 10;

    private int selectingBullet = 0;

    public static ShootingGameController getInstance() {
        return instance;
    }

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
            } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
                selectBullet(selectingBullet - 1);
            } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                selectBullet(selectingBullet + 1);
            }
        });

        GameData.loadShootingGameData();
        changeScene(Status.START_GAME);
    }

    public void addBullet(Bullet bullet) {
        waitingBullets.add(bullet);
    }

    public void addBullet(ArrayList<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            addBullet(bullet);
        }
    }

    public void addTarget(Target target) {
        showingTargets.add(target);
        gamePane.getChildren().add(0, target.getObjectView().getShowText());
    }

    public void addWaitingTarget(Target target) {
        waitingTargets.add(target);
    }


    public void removeBullet(Bullet rBullet) {
        if(showingBullets.contains(rBullet)) {
            showingBullets.remove(rBullet);
            bulletContainer.getChildren().remove(rBullet.getObjectView().getShowText());
        }
    }

    public void removeTarget(Target target) {
        if (showingTargets.contains(target)) {
            showingTargets.remove(target);
            gamePane.getChildren().remove(target.getObjectView().getShowText());
        }
    }

    public void reload() {
        if (!waitingBullets.isEmpty()) {
            if (showingBullets.size() < MAX_SHOWING_BULLET_SIZE) {
                while (showingBullets.size() < MAX_SHOWING_BULLET_SIZE) {
                    if (!waitingBullets.isEmpty()) {
                        Bullet bullet = waitingBullets.poll();
                        bulletContainer.getChildren().add(bullet.getObjectView().getShowText());
                        showingBullets.add(bullet);
                    } else {
                        return;
                    }
                }
            } else {
                for (int i = 0; i < MAX_SHOWING_BULLET_SIZE; i++) {
                    waitingBullets.add(showingBullets.get(i));
                }
                showingBullets.clear();
                bulletContainer.getChildren().clear();
                for (int i = 0; i < MAX_SHOWING_BULLET_SIZE; i++) {
                    if (!waitingBullets.isEmpty()) {
                        Bullet bullet = waitingBullets.poll();
                        bulletContainer.getChildren().add(bullet.getObjectView().getShowText());
                        showingBullets.add(bullet);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void selectBullet(int pos) {
        if (!showingBullets.isEmpty()) {
            if (pos >= showingBullets.size()) {
                pos = 0;
            } else if (pos < 0) {
                pos = showingBullets.size() - 1;
            }
            showingBullets.get(selectingBullet).getObjectView().getShowText().setStyle(null);
            showingBullets.get(pos).getObjectView().getShowText().setStyle("-fx-background-color: rgba(255,255,0,0.3)");
            selectingBullet = pos;
        }
    }
    private void clickToShoot() {
        gamePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) { //click chuột trái
                GameObject.Color bulletColor = showingBullets.get(showingBullets.size() - 1).getColor();
                shootBullet(bulletColor);
            }
        });
    }

    private void shootBullet(GameObject.Color color) {
        if(showingBullets.isEmpty()) return;
        Bullet currentBullet = showingBullets.get(showingBullets.size() - 1);
        removeBullet(currentBullet);
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
                if (countStep < numSteps && newBullet.getLayoutX() <= 1300 && newBullet.getLayoutX() >= 0
                    && newBullet.getLayoutY() <= 700 && newBullet.getLayoutY() >= 0) {
                    newBullet.setLayoutX(newBullet.getLayoutX() + stepX);
                    newBullet.setLayoutY(newBullet.getLayoutY() + stepY);
                    countStep++;
                    for(Target t : showingTargets) {
                        if(checkCollision(newBullet, t)) {
                            countStep = numSteps;
                            if(currentBullet.getKeyText().equalsIgnoreCase(t.getShowText())) {
                                removeTarget(t);
                            }
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
//        if(numberShowingBullet == 0 || numberShowingTarget == 0) {
//            System.out.println("this is round" + round);
//            switch (round) {
//                case 1: setRound(2);
//                case 2: setRound(3);
//                case 3: setRound(4);
//                case 4: setRound(5);
//                case 5: setRound(6);
//                case 6: setRound(7);
//                case 7: changeScene(Status.START_GAME);
//            }
//        }
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
                changeRoundAutomatically();

            }

            case END_GAME -> {

            }
        }
    }

    private void changeRoundAutomatically() {
        if (round < 7) {
            // round đầu
            setRound(round);
            round++;
            Timeline timeline1 = new Timeline(new KeyFrame(
                    Duration.seconds(5),
                    event -> {
                        startRound2();
                    }
            ));
            timeline1.play();
        }
    }
    private void startRound2() {
        setRound(round);
        round++;
        Timeline timeline2 = new Timeline(new KeyFrame(
                Duration.seconds(55),
                event -> {
                    startRound3();
                }
        ));
        timeline2.play();
    }
    private void startRound3() {
        setRound(round);
        round++;
        Timeline timeline3 = new Timeline(new KeyFrame(
                Duration.seconds(5),
                event -> {
                    startRound4();
                }
        ));
        timeline3.play();
    }
    private void startRound4() {
        setRound(round);
        round++;
        Timeline timeline4 = new Timeline(new KeyFrame(
                Duration.seconds(5),
                event -> {
                    startRound5();
                }
        ));
        timeline4.play();
    }
    private void startRound5() {
        setRound(round);
        round++;
        Timeline timeline5 = new Timeline(new KeyFrame(
                Duration.seconds(5),
                event -> {
                    startRound6();
                }
        ));
        timeline5.play();
    }
    private void startRound6() {
        setRound(round);
        round++;
        Timeline timeline6 = new Timeline(new KeyFrame(
                Duration.seconds(5),
                event -> {
                    startRound7();
                }
        ));
        timeline6.play();
    }
    private void startRound7() {
        setRound(round);
        round++;
        Timeline timeline7 = new Timeline(new KeyFrame(
                Duration.seconds(5),
                event -> {
                    System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                }
        ));
        timeline7.play();
    }

    private void setRound(int round) {
        clear();
        RoundGenerator.generateEnBullet_ViTarget(round);
        for (Target target : showingTargets) {
            target.move();
        }
        reload();
        selectBullet(0);
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

        return pointInsideCircle(bulletHeadX, bulletHeadY, targetCenterX, targetCenterY, radius);
    }

    private boolean pointInsideCircle(double pointX, double pointY, double centerX, double centerY, double radius) {
        double distance = Math.sqrt((centerX - pointX) * (centerX - pointX) + (centerY - pointY) * (centerY - pointY));
        return distance <= radius;
    }

    public void clear() {
        bulletContainer.getChildren().clear();
        showingBullets.clear();
        waitingBullets.clear();
        waitingTargets.clear();
        for (Target target : showingTargets) {
            gamePane.getChildren().remove(target.getObjectView().getShowText());
        }
        showingTargets.clear();
    }
}
