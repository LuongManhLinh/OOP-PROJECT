package classes.ExperimentGameClasses;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class BottleImages {
    public static ArrayList<InputStream> blueBottleInputs = new ArrayList<>();
    public static ArrayList<InputStream> brownBottleInputs = new ArrayList<>();
    public static ArrayList<InputStream> grayBottleInputs = new ArrayList<>();
    public static ArrayList<InputStream> greenBottleInputs = new ArrayList<>();
    public static ArrayList<InputStream> pinkBottleInputs = new ArrayList<>();
    public static ArrayList<InputStream> purpleBottleInputs = new ArrayList<>();
    public static ArrayList<InputStream> redBottleInputs = new ArrayList<>();
    public static ArrayList<InputStream> whiteBottleInputs = new ArrayList<>();
    public static ArrayList<InputStream> yellowBottleInputs = new ArrayList<>();
    public static InputStream  speakerTextInputs;
    public static InputStream mainBottleInput;
    public static InputStream failedInput;
    public static InputStream quiteSuccessInput;
    public static InputStream successInput;


    static {
        try {
            speakerTextInputs = new FileInputStream("src/main/resources/image/speaker.png");

            blueBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/blueBottle.png"));
            blueBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/blue-45.png"));
            blueBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/blue-90.png"));
            blueBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/blue-95.png"));

            brownBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/brownBottle.png"));
            brownBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/brown-45.png"));
            brownBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/brown-90.png"));
            brownBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/brown-95.png"));

            grayBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/grayBottle.png"));
            grayBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/gray-45.png"));
            grayBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/gray-90.png"));
            grayBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/gray-95.png"));

            greenBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/greenBottle.png"));
            greenBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/green-45.png"));
            greenBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/green-90.png"));
            greenBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/green-95.png"));

            pinkBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/pinkBottle.png"));
            pinkBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/pink-45.png"));
            pinkBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/pink-90.png"));
            pinkBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/pink-95.png"));

            purpleBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/purpleBottle.png"));
            purpleBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/purple-45.png"));
            purpleBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/purple-90.png"));
            purpleBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/purple-95.png"));

            redBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/redBottle.png"));
            redBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/red-45.png"));
            redBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/red-90.png"));
            redBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/red-95.png"));

            whiteBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/whiteBottle.png"));
            whiteBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/white-45.png"));
            whiteBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/white-90.png"));
            whiteBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/white-95.png"));

            yellowBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/yellowBottle.png"));
            yellowBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/yellow-45.png"));
            yellowBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/yellow-90.png"));
            yellowBottleInputs.add(new FileInputStream("src/main/resources/gameAssets/ExperimentGame/yellow-95.png"));

            mainBottleInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/mainBottle.png");
            failedInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/failed.png");
            quiteSuccessInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/quiteSuccess.png");
            successInput = new FileInputStream("src/main/resources/gameAssets/ExperimentGame/success.png");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Image speakerText = new Image(speakerTextInputs);
    public static ArrayList<Image> blueBottles = new ArrayList<>();
    public static ArrayList<Image> brownBottles = new ArrayList<>();
    public static ArrayList<Image> grayBottles = new ArrayList<>();
    public static ArrayList<Image> greenBottles = new ArrayList<>();
    public static ArrayList<Image> pinkBottles = new ArrayList<>();
    public static ArrayList<Image> purpleBottles = new ArrayList<>();
    public static ArrayList<Image> redBottles = new ArrayList<>();
    public static ArrayList<Image> whiteBottles = new ArrayList<>();
    public static ArrayList<Image> yellowBottles = new ArrayList<>();
    public static ArrayList<ArrayList<Image>> allBottles = new ArrayList<>();

    public static Image mainBottle = new Image(mainBottleInput);
    public static Image failed = new Image(failedInput);
    public static Image quiteSuccess = new Image(quiteSuccessInput);
    public static Image success = new Image(successInput);

    public static void loadData() {
        for (InputStream inputStream : blueBottleInputs) {
            blueBottles.add(new Image(inputStream));
        }
        for (InputStream inputStream : brownBottleInputs) {
            brownBottles.add(new Image(inputStream));
        }
        for (InputStream inputStream : grayBottleInputs) {
            grayBottles.add(new Image(inputStream));
        }
        for (InputStream inputStream : greenBottleInputs) {
            greenBottles.add(new Image(inputStream));
        }
        for (InputStream inputStream : pinkBottleInputs) {
            pinkBottles.add(new Image(inputStream));
        }
        for (InputStream inputStream : purpleBottleInputs) {
            purpleBottles.add(new Image(inputStream));
        }
        for (InputStream inputStream : redBottleInputs) {
            redBottles.add(new Image(inputStream));
        }
        for (InputStream inputStream : whiteBottleInputs) {
            whiteBottles.add(new Image(inputStream));
        }
        for (InputStream inputStream : yellowBottleInputs) {
            yellowBottles.add(new Image(inputStream));
        }
        allBottles.add(blueBottles);
        allBottles.add(brownBottles);
        allBottles.add(grayBottles);
        allBottles.add(greenBottles);
        allBottles.add(pinkBottles);
        allBottles.add(purpleBottles);
        allBottles.add(redBottles);
        allBottles.add(whiteBottles);
        allBottles.add(yellowBottles);
    }
}
