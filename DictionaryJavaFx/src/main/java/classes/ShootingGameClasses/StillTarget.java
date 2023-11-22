package classes.ShootingGameClasses;

public class StillTarget extends Target {
    public StillTarget(Color color, String showText, double score, Vector spawnPoint) {
        super(color, showText, score);
        objectView.setPosition(spawnPoint);
    }

    @Override
    public void move() {

    }
}
