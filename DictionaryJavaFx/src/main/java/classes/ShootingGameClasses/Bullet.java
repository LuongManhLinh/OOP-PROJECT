package classes.ShootingGameClasses;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bullet extends GameObject{
    private String keyText;

    public Bullet(Color color, String showText, String keyText) {
        super(color, showText);
        this.keyText = keyText;
    }

    @Override
    public void loadView() {
        switch (color) {
            case RED -> objectView.setObject(new ImageView(ObjectImages.redBullet));
            case BLUE -> objectView.setObject(new ImageView(ObjectImages.blueBullet));
            case GREEN -> objectView.setObject(new ImageView(ObjectImages.greenBullet));
        }
    }
}
