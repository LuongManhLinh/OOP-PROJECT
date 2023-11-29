package classes.data;

import classes.ExperimentGameClasses.BottleImages;
import classes.ShootingGameClasses.ObjectImages;

import java.util.ArrayList;
import java.util.HashMap;

public class GameData {
    private static final HashMap<String, String> easyWords = new HashMap<>();
    private static final ArrayList<String> easyEnglishKeyWords = new ArrayList<>();
    private static final HashMap<String, String> normalWords = new HashMap<>();
    private static final ArrayList<String> normalEnglishKeyWords = new ArrayList<>();

    private static final HashMap<String, ArrayList<String>> shootingGameWords = new HashMap<>();
    private static final ArrayList<String> shootingGameEnglishKeyWords = new ArrayList<>();

    private static boolean loadedExperimentGame = false;
    private static boolean loadedShootingGame = false;

    private static final String EASY_DATA_PATH = "src/main/resources/gameAssets/data/easy.txt";
    private static final String NORMAL_DATA_PATH = "src/main/resources/gameAssets/data/normal.txt";
    private static final String SHOOTING_GAME_DATA_PATH = "src/main/resources/gameAssets/data/shootingGame.txt";


    public static void loadExperimentGameData() {
        if (!loadedExperimentGame) {
            DictionaryFileWork.importAllWords(EASY_DATA_PATH, easyWords, easyEnglishKeyWords);
            DictionaryFileWork.importAllWords(NORMAL_DATA_PATH, normalWords, normalEnglishKeyWords);
            BottleImages.loadData();
            loadedExperimentGame = true;
        }
    }

    public static void loadShootingGameData() {
        if (!loadedShootingGame) {
            DictionaryFileWork.importAllWordsForShootingGame(SHOOTING_GAME_DATA_PATH, shootingGameWords, shootingGameEnglishKeyWords);
            ObjectImages.loadData();
            loadedShootingGame = true;
        }
    }

    public static HashMap<String, String> getEasyWords() {
        return easyWords;
    }

    public static ArrayList<String> getEasyEnglishKeyWords() {
        return easyEnglishKeyWords;
    }

    public static HashMap<String, String> getNormalWords() {
        return normalWords;
    }

    public static ArrayList<String> getNormalEnglishKeyWords() {
        return normalEnglishKeyWords;
    }

    public static HashMap<String, ArrayList<String>> getShootingGameWords() {
        return shootingGameWords;
    }

    public static ArrayList<String> getShootingGameEnglishKeyWords() {
        return shootingGameEnglishKeyWords;
    }
}
