package classes;

import classes.ExperimentGameClasses.BottleImages;
import classes.dictionarycommandline.WordWork;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameData {
    private static HashMap<String, String> easyWords = new HashMap<>();
    private static ArrayList<String> easyEnglishKeyWords = new ArrayList<>();
    private static HashMap<String, String> normalWords = new HashMap<>();
    private static ArrayList<String> normalEnglishKeyWords = new ArrayList<>();

    public static final String EASY_DATA_PATH = "src/main/resources/gameAssets/data/easy.txt";
    public static final String NORMAL_DATA_PATH = "src/main/resources/gameAssets/data/normal.txt";


    public static void loadData(){
        loadData(EASY_DATA_PATH, easyWords, easyEnglishKeyWords);
        loadData(NORMAL_DATA_PATH, normalWords, normalEnglishKeyWords);
        BottleImages.loadData();
    }

    private static void loadData(String filePath, HashMap<String, String> words, ArrayList<String> englishKeyWords){
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line, key, meaning;
        while (fileScanner.hasNext()) {
            line = fileScanner.nextLine();
            key = WordWork.decodeForm(line, true);
            meaning = WordWork.decodeForm(line, false);
            words.put(key, meaning);
            englishKeyWords.add(key);
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
