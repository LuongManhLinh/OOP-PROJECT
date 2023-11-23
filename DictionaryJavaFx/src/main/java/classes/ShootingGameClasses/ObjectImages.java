package classes.ShootingGameClasses;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ObjectImages {
    public static Image redBullet;
    public static Image blueBullet;
    public static Image greenBullet;

    public static Image redTarget;
    public static Image blueTarget;
    public static Image greenTarget;
    public static void loadData() {
        InputStream bulletRed;
        InputStream bulletGreen;
        InputStream bulletBlue;
        InputStream targetRed;
        InputStream targetGreen;
        InputStream targetBlue;
        InputStream shootBullet;
        try {
            bulletRed = new FileInputStream("src/main/resources/gameAssets/ShootingGame/bullet_red.png");
            bulletGreen = new FileInputStream("src/main/resources/gameAssets/ShootingGame/bullet_green.png");
            bulletBlue = new FileInputStream("src/main/resources/gameAssets/ShootingGame/bullet_blue.png");

            targetRed = new FileInputStream("src/main/resources/gameAssets/ShootingGame/target_red.png");
            targetGreen = new FileInputStream("src/main/resources/gameAssets/ShootingGame/target_green.png");
            targetBlue = new FileInputStream("src/main/resources/gameAssets/ShootingGame/target_blue.png");

            redBullet = new Image(bulletRed);
            blueBullet = new Image(bulletBlue);
            greenBullet = new Image(bulletGreen);

            redTarget = new Image(targetRed);
            blueTarget = new Image(targetBlue);
            greenTarget = new Image(targetGreen);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
