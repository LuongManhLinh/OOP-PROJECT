package classes.ShootingGameClasses;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

public class StraightMovingTarget extends Target {
    private List<Vector> destinations = new ArrayList<>();
    private double velocity = 0;
    private int nextDestination = 0;
    private boolean isIncreasingNext = true;
    private Vector direction;

    private long timeBefore = 0;

    public StraightMovingTarget(Color color, String showText, double score, Vector spawnPoint) {
        super(color, showText, score, spawnPoint);
        destinations.add(spawnPoint);
    }

    public List<Vector> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Vector> destinations) {
        this.destinations = destinations;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void addDestination(Vector destination) {
        destinations.add(destination);
    }

    // t, v
    @Override
    public void move() {
        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long timeNow) {
                if (destinations.size() >= 2) {
                    if (objectView.getPosition().getDistanceAsPointToPoint(destinations.get(nextDestination)) < 1) {
                        objectView.setPosition(destinations.get(nextDestination));
                        if (isIncreasingNext) {
                            nextDestination++;
                            if (nextDestination >= destinations.size()) {
                                isIncreasingNext = false;
                                nextDestination = destinations.size() - 2;
                            }
                        } else {
                            nextDestination--;
                            if (nextDestination < 0) {
                                isIncreasingNext = true;
                                nextDestination = 1;
                            }
                        }

                        Vector now = objectView.getPosition();
                        Vector then = destinations.get(nextDestination);

                        direction = new Vector(then.getX() - now.getX(), then.getY() - now.getY());
                        direction.normalize();
                    }

                    if (timeBefore != 0) {
                        double time = (double) (timeNow - timeBefore) / 1000000000;
                        double x = objectView.getPosition().getX() + velocity * direction.getX() * time;
                        double y = objectView.getPosition().getY() + velocity * direction.getY() * time;
                        objectView.setPosition(new Vector(x, y));

                        System.out.println(time);
                    }

                    timeBefore = timeNow;
                }
            }
        };
        moveTimer.start();
    }

}
