package classes.ShootingGameClasses;

import javafx.scene.image.Image;

public class Bullet extends GameObject{
    private String keyText;

    public Bullet(Color color, String showText, String keyText) {
        super(color, showText);
        this.keyText = keyText;
    }

    @Override
    public void loadView(Color color) {
        String path;
        if(color == Color.RED) {
            path = "src/main/resources/gameAssets/ShootingGame/bullet_red.png";
        }
        else if(color == Color.BLUE) {
            path = "src/main/resources/gameAssets/ShootingGame/bullet_blue.png";
        }
        else {
            path = "src/main/resources/gameAssets/ShootingGame/bullet_green.png";
        }
        try {
            Image bulletImage = new Image(path);
            objectView.object.setImage(bulletImage);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("Error loading bullet image: " + path);
        }
    }
}
