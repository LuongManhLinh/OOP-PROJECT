package classes.ExperimentGameClasses;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BottleImages {
    public static final InputStream bottleInput;
    public static final InputStream blueBottleInput;
    public static final InputStream brownBottleInput;
    public static final InputStream grayBottleInput;
    public static final InputStream greenBottleInput;
    public static final InputStream pinkBottleInput;
    public static final InputStream purpleBottleInput;
    public static final InputStream redBottleInput;
    public static final InputStream whiteBottleInput;
    public static final InputStream yellowBottleInput;
    static {
        try {
            bottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/bottle.png");
            blueBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/blueBottle.png");
            brownBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/brownBottle.png");
            grayBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/grayBottle.png");
            greenBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/greenBottle.png");
            pinkBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/pinkBottle.png");
            purpleBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/purpleBottle.png");
            redBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/redBottle.png");
            whiteBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/whiteBottle.png");
            yellowBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/yellowBottle.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public static final Image bottle = new Image(bottleInput);
    public static final Image blueBottle = new Image(blueBottleInput);
    public static final Image brownBottle = new Image(brownBottleInput);
    public static final Image grayBottle = new Image(grayBottleInput);
    public static final Image greenBottle = new Image(greenBottleInput);
    public static final Image pinkBottle = new Image(pinkBottleInput);
    public static final Image purpleBottle = new Image(purpleBottleInput);
    public static final Image redBottle = new Image(redBottleInput);
    public static final Image whiteBottle = new Image(whiteBottleInput);
    public static final Image yellowBottle = new Image(yellowBottleInput);

    public static final Image[] containingBottles = {
            blueBottle,
            brownBottle,
            grayBottle,
            greenBottle,
            pinkBottle,
            purpleBottle,
            redBottle,
            whiteBottle,
            yellowBottle
    };
    public static final String blueRGB = "rgb(77,109,243)";
    public static final String brownRGB = "rgb(97,74,10)";
    public static final String grayRGB = "rgb(180,180,180)";
    public static final String greenRGB = "rgb(168,230,29)";
    public static final String pinkRGB = "rgb(255,192,203)";
    public static final String purpleRGB = "rgb(111,49,152)";
    public static final String redRGB = "rgb(255,0,0)";
    public static final String whiteRGB = "rgb(255,255,255)";
    public static final String yellowRGB = "rgb(245,228,156)";

}
