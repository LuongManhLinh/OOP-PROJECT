package classes.data;

import classes.ExperimentGameClasses.BottleImages;

import java.util.ArrayList;
import java.util.HashMap;

public class GameData {
    private static HashMap<String, String> easyWords = new HashMap<>();
    private static ArrayList<String> easyEnglishKeyWords = new ArrayList<>();
    private static HashMap<String, String> normalWords = new HashMap<>();
    private static ArrayList<String> normalEnglishKeyWords = new ArrayList<>();

    public static final String EASY_DATA_PATH = "src/main/resources/gameAssets/data/easy.txt";
    public static final String NORMAL_DATA_PATH = "src/main/resources/gameAssets/data/normal.txt";


    public static void loadData() {
        DictionaryFileWork.importAllWords(EASY_DATA_PATH, easyWords, easyEnglishKeyWords);
        DictionaryFileWork.importAllWords(NORMAL_DATA_PATH, normalWords, normalEnglishKeyWords);
        BottleImages.loadData();
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

    public static HashMap<String, String> getAllWords() {
        HashMap<String, String> result = new HashMap<>(easyWords);
        result.putAll(normalWords);
        return result;
    }

    public static ArrayList<String> getAllEnglishKeyWords() {
        ArrayList<String> result = new ArrayList<>(easyEnglishKeyWords);
        result.addAll(normalEnglishKeyWords);
        return result;
    }
}
