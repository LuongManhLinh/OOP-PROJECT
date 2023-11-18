package classes.ShootingGameClasses;

import javafx.scene.image.Image;

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

    public void loadView(Color color) {
        String path;
        if(color == Color.RED) {
            path = "src/main/resources/gameAssets/ShootingGame/target_red.png";
        }
        else if(color == Color.BLUE) {
            path = "src/main/resources/gameAssets/ShootingGame/target_blue.png";
        }
        else {
            path = "src/main/resources/gameAssets/ShootingGame/target_green.png";
        }
        try {
            Image bulletImage = new Image(path);
            objectView.object.setImage(bulletImage);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("Error loading target image: " + path);
        }
    }
}
