package classes.ShootingGameClasses;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Target extends GameObject {
    private int score;

    public Target(Color color, String showText, int score) {
        super(color, showText);
        this.score = score;
    }

    public abstract void move();

    public int takeDamage() {
        return score;
    }

    public void loadView() {
        switch (color) {
            case RED -> objectView.setObject(new ImageView(ObjectImages.redTarget));
            case BLUE -> objectView.setObject(new ImageView(ObjectImages.blueTarget));
            case GREEN -> objectView.setObject(new ImageView(ObjectImages.greenTarget));
        }
    }
}
