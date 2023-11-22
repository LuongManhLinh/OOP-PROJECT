package classes.ShootingGameClasses;

import classes.ShootingGameClasses.Target;
import classes.ShootingGameClasses.Vector;
import javafx.animation.AnimationTimer;

public class RoundMovingTarget extends Target {
    private Vector center;
    private double radius;
    private double degree = 0;
    private double roundVelocity = 0;
    private double moveVelocity = 0;
    private Vector moveDirection = new Vector(0, 0);
    private long timeBefore = 0;
    public RoundMovingTarget(Color color, String showText, double score, Vector center, double radius) {
        super(color, showText, score);
        this.center = center;
        this.radius = radius;
    }

    @Override
    public void move() {
        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long timeNow) {
                if (radius > 0) {
                    double cos = Math.cos(Math.toRadians(degree));
                    double x = radius * cos + center.getX();
                    double y;
                    if (degree > 180) {
                        y = center.getY() + radius * Math.sqrt(1 - cos * cos);
                    } else {
                        y = center.getY() - radius * Math.sqrt(1 - cos * cos);
                    }

                    objectView.setPosition(new Vector(x, y));

                    if (timeBefore != 0) {
                        double time = (double) (timeNow - timeBefore) / 1000000000;
                        degree += time * roundVelocity;
                        center.setX(center.getX() + moveVelocity * time * moveDirection.getX());
                        center.setY(center.getY() + moveVelocity * time * moveDirection.getY());

                        if (degree >= 360) {
                            degree -= 360;
                        }
                    }
                    timeBefore = timeNow;
                }
            }
        };
        moveTimer.start();
    }

    public Vector getCenter() {
        return center;
    }

    public void setCenter(Vector center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getDegree() {
        return degree;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public double getRoundVelocity() {
        return roundVelocity;
    }

    public void setRoundVelocity(double roundVelocity) {
        this.roundVelocity = roundVelocity;
    }

    public double getMoveVelocity() {
        return moveVelocity;
    }

    public void setMoveVelocity(double moveVelocity) {
        this.moveVelocity = moveVelocity;
    }

    public Vector getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(Vector moveDirection) {
        moveDirection.normalize();
        this.moveDirection = moveDirection;
    }
}
