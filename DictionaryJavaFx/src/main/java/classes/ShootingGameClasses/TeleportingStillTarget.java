package classes.ShootingGameClasses;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class TeleportingStillTarget extends StillTarget {
    private List<Vector> positions = new ArrayList<>();
    private double timeToJump = 0;
    private int nextPosition = 0;
    private long timeBefore = 0;
    private double timeProgress = 0;

    public TeleportingStillTarget(Color color, String showText, double score) {
        super(color, showText, score);
    }

    @Override
    public void move() {
        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long timeNow) {
                if (timeBefore != 0) {
                    timeProgress += (double) (timeNow - timeBefore) / 1000000;
                    if (timeProgress >= timeToJump) {
                        objectView.setPosition(positions.get(nextPosition));
                        nextPosition++;
                        if (nextPosition >= positions.size()) {
                            nextPosition = 0;
                        }
                        timeProgress = 0;
                    }
                }
                timeBefore = timeNow;
            }
        };

        moveTimer.start();
    }

    public void addPosition(Vector position) {
        if (positions.isEmpty()) {
            objectView.setPosition(position);
        } else if (positions.size() == 1) {
            nextPosition = 1;
        }
        positions.add(position);
    }

    public void setTimeToJump(double timeMiliSecond) {
        timeToJump = timeMiliSecond;
    }

}
