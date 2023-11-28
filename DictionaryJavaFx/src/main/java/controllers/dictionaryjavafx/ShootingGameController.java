package controllers.dictionaryjavafx;

import classes.FXMLFiles;
import classes.ShootingGameClasses.*;
import classes.data.GameData;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class ShootingGameController implements Initializable {
    @FXML private AnchorPane fatherPane;
    @FXML private AnchorPane menuPane;
    @FXML private ScrollPane guidePane;
    @FXML private AnchorPane gamePane;
    @FXML private AnchorPane endGame;
    @FXML private ImageView CannonImage;
    @FXML private ImageView reloadImage;
    @FXML private Line line;
    @FXML private VBox bulletContainer;
    @FXML private ImageView shootBulletRed;
    @FXML private ImageView shootBulletBlue;
    @FXML private ImageView shootBulletGreen;
    @FXML private Label yourScore;
    @FXML private Label curRound;
    @FXML private Label time;
    @FXML private Label numberWaitingBulletLabel;
    @FXML private Label endGameLabel;

    private enum Status {
        START_GAME,
        IN_GAME,
        GUIDE,
        END_GAME
    }

    private static final int SPIN_POINT_X = 30;
    private static final int SPIN_POINT_Y = 679;

    private static final int MAX_SHOWING_BULLET_SIZE = 6;
    private int MAX_SHOWING_TARGET_SIZE;


    private static ShootingGameController instance;

    private final Queue<Bullet> waitingBullets = new LinkedList<>();
    private final Queue<Target> waitingTargets = new LinkedList<>();

    private final List<Bullet> showingBullets = new ArrayList<>();
    private final List<Target> showingTargets = new ArrayList<>();

    private int round = 1;
    private double curScore = 0;
    private int selectingBullet = 0;
    private int timeRemaining = 15;
    private Timeline timeRemainingTimeline;
    private boolean canChangeRound = true;

    public static ShootingGameController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        ObjectImages.loadData();
        setCannonRotation();
        clickToShoot();
        curRound.setText("Round " + round);
        yourScore.setText("Score: " + curScore);

        bulletContainer.getChildren().clear();

        gamePane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
                selectBullet(selectingBullet - 1);
            } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
                selectBullet(selectingBullet + 1);
            } else if (event.getCode() == KeyCode.R) {
                reload();
            }
        });

        timeRemainingTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> decreaseTimeRemaining())
        );
        timeRemainingTimeline.setCycleCount(Timeline.INDEFINITE);

        fatherPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                changeScene(Status.START_GAME);
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
            reloadAnimation();
            if (selectingBullet >= 0 && selectingBullet < showingBullets.size()) {
                showingBullets.get(selectingBullet).getObjectView().getShowText().setStyle(null);
            }
            if (showingBullets.size() < MAX_SHOWING_BULLET_SIZE) {
                while (showingBullets.size() < MAX_SHOWING_BULLET_SIZE) {
                    if (!waitingBullets.isEmpty()) {
                        Bullet bullet = waitingBullets.poll();
                        bulletContainer.getChildren().add(bullet.getObjectView().getShowText());
                        showingBullets.add(bullet);
                    } else {
                        break;
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
                        break;
                    }
                }
            }
            selectBullet(0);
        }
        numberWaitingBulletLabel.setText(String.valueOf(waitingBullets.size()));
    }
    private void addTargetAutomatically() {
        if(showingTargets.size() < MAX_SHOWING_TARGET_SIZE && !waitingTargets.isEmpty()) {
            showingTargets.add(waitingTargets.poll());
            gamePane.getChildren().add(0, showingTargets.get(showingTargets.size() - 1).getObjectView().getShowText());
        }
    }

    private void reloadAnimation() {
        AnimationTimer reloadTimer = new AnimationTimer() {
            double degree = 0;
            @Override
            public void handle(long l) {
                degree += 6;
                if (degree > 360) {
                    reloadImage.setRotate(0);
                    stop();
                }
                reloadImage.setRotate(degree);
            }
        };
        reloadTimer.start();
    }

    public void selectBullet(int pos) {
        if (!showingBullets.isEmpty()) {
            if (pos >= showingBullets.size()) {
                pos = 0;
            } else if (pos < 0) {
                pos = showingBullets.size() - 1;
            }
            if (selectingBullet >= 0 && selectingBullet < showingBullets.size()) {
                showingBullets.get(selectingBullet).getObjectView().getShowText().setStyle(null);
            }
            showingBullets.get(pos).getObjectView().getShowText().setStyle("-fx-background-color: rgba(255,255,0,0.3)");
            selectingBullet = pos;
        }
    }

    private void clickToShoot() {
        gamePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) { //click chuột trái
                if(!showingBullets.isEmpty()) {
                    GameObject.Color bulletColor = showingBullets.get(selectingBullet).getColor();
                    shootBullet(bulletColor);
                    if (!showingBullets.isEmpty()) {
                        selectBullet(0);
                    }
                }
            }
        });
    }

    private void shootBullet(GameObject.Color color) {
        gamePane.requestFocus();
        canChangeRound = false;
        if(showingBullets.isEmpty()) return;
        Bullet currentBullet = showingBullets.get(selectingBullet);
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

        double stepDistance = 9.0;

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
                    for(Target tar : showingTargets) {
                        if(checkCollision(newBullet, tar)) {
                            countStep = numSteps;
                            curScore += tar.takeDamage(currentBullet);
                            yourScore.setText("Score: " + curScore);
                            if(currentBullet.getKeyText().equalsIgnoreCase(tar.getShowText())) {
                                removeTarget(tar);
                                addTargetAutomatically();
                            }
                            break;
                        }
                    }
                } else {
                    canChangeRound = true;
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

    public void onGuide() {
        changeScene(Status.GUIDE);
    }

    public void goToEndGame() {
        changeScene(Status.END_GAME);
    }
    public void onBackToMenu() {
        changeScene(Status.START_GAME);
    }

    private void changeScene(Status status) {
        switch (status) {
            case START_GAME -> {
                menuPane.setVisible(true);
                gamePane.setVisible(false);
                guidePane.setVisible(false);
                endGame.setVisible(false);
                round = 1;
                curScore = 0;
            }

            case IN_GAME -> {
                menuPane.setVisible(false);
                gamePane.setVisible(true);
                guidePane.setVisible(false);
                endGame.setVisible(false);
                RoundGenerator.refillIndex();
                round = 1;
                setRound(round);
                curScore = 0;
                yourScore.setText("Score: " + curScore);
            }

            case GUIDE -> {
                menuPane.setVisible(false);
                gamePane.setVisible(false);
                guidePane.setVisible(true);
                endGame.setVisible(false);
            }

            case END_GAME -> {
                menuPane.setVisible(false);
                gamePane.setVisible(false);
                guidePane.setVisible(false);
                endGame.setVisible(true);
                if(curScore > 0) {
                    endGameLabel.setText("Congratulations, you got " + curScore + " points");
                }
                else {
                    endGameLabel.setText("Oops, you got " + curScore + " points");
                }
            }
        }
    }

    private void setRound(int round) {
        clear();
        curRound.setText("Round " + round);
        RoundGenerator.generateEnBullet_ViTarget(round);
        for (Target target : showingTargets) {
            target.move();
        }
        MAX_SHOWING_TARGET_SIZE = showingTargets.size();
        reload();
        selectBullet(0);

        switch (round) {
            case 1:
                timeRemaining = 20;
                break;
            case 2, 3:
                timeRemaining = 40;
                break;
            case 4, 5:
                timeRemaining = 50;
                break;
            case 6:
                timeRemaining = 60;
                break;
            case 7:
                timeRemaining = 100;
                break;
            default:
                timeRemaining = 120;
                break;
        }
//        timeRemaining = 10; // Reset time for each round
        timeRemainingTimeline.playFromStart();
        time.setText("Time: " + timeRemaining);
    }
    private void decreaseTimeRemaining() {
        timeRemaining--;
        time.setText("Time: " + timeRemaining);
        if ((timeRemaining <= 0 || showingBullets.size() + waitingBullets.size() == 0 || showingTargets.size() + waitingTargets.size() == 0)
            && canChangeRound) {
            if(showingTargets.size() + waitingTargets.size() == 0) {
                int bonusScore = timeRemaining / 10;
                curScore += bonusScore;
            }
            // Stop counting time and move to the next round
            yourScore.setText("Score: " + curScore);
            timeRemainingTimeline.stop();
            moveToNextRound();
        }
    }

    public void setGiveUpRound() {
        timeRemainingTimeline.stop();
        moveToNextRound();
    }
    private void moveToNextRound() {
        round++;
        setRound(round);
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
