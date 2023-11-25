package classes.ShootingGameClasses;

import controllers.dictionaryjavafx.ShootingGameController;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Target extends GameObject {
    private final double score;

    public Target(Color color, String showText, double score) {
        super(color, showText);
        this.score = score;
    }

    public abstract void move();

    public double takeDamage(Bullet bullet) {
        if (bullet.getKeyText().equals(showText)) {
            if (bullet.getColorToBeCountered() == color) {
                return 2 * score;
            } else if (bullet.getColorToCounter() == color) {
                return -0.5 * score;
            } else {
                return score;
            }
        } else {
            if (bullet.getColorToCounter() == color) {
                return -score;
            } else {
                return 0;
            }
        }
    }

    public String getShowText() {
        return showText;
    }

    public void loadView() {
        switch (color) {
            case RED -> objectView = new ObjectView(showText, ObjectImages.redTarget);
            case BLUE -> objectView = new ObjectView(showText, ObjectImages.blueTarget);
            case GREEN -> objectView = new ObjectView(showText, ObjectImages.greenTarget);
        }
    }
}
