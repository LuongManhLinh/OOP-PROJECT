package classes.data;

import classes.ExperimentGameClasses.BottleImages;
import classes.ShootingGameClasses.ObjectImages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    private static final int LONGEST_WORD_LENGTH = 30;


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


    //These used for write data
    private static ArrayList<String> separateByComma(String meaning) {
        ArrayList<String> result = new ArrayList<>();
        String eachWord = "";
        for (int i = 0; i < meaning.length(); i++) {
            char checkingChar = meaning.charAt(i);
            if (checkingChar == ',' || checkingChar == ';') {
                eachWord = eachWord.trim();
                if (eachWord.length() <= LONGEST_WORD_LENGTH) {
                    result.add(eachWord);
                }
                eachWord = "";
            } else {
                eachWord += checkingChar;
                if (checkingChar == '(') {
                    while (meaning.charAt(i) != ')') {
                        i++;
                        eachWord += meaning.charAt(i);
                    }
                }
            }
        }
        eachWord = eachWord.trim();
        if (!eachWord.isEmpty() && eachWord.length() < LONGEST_WORD_LENGTH) {
            result.add(eachWord);
        }
        return result;
    }

    private static HashMap<String, String> getAllWords() {
        HashMap<String, String> result = new HashMap<>(easyWords);
        result.putAll(normalWords);
        return result;
    }

    private static ArrayList<String> getAllEnglishKeyWords() {
        ArrayList<String> result = new ArrayList<>(easyEnglishKeyWords);
        result.addAll(normalEnglishKeyWords);
        return result;
    }

    public static void main(String[] args) {
        loadExperimentGameData();
        HashMap<String, String> words = getAllWords();
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("D:\\Java_IntelliJ\\OOP-PROJECT\\OOP-PROJECT\\DictionaryJavaFx\\src\\main\\resources\\gameAssets\\data\\shootingGame.txt"))) {
            for (String eachKey : getAllEnglishKeyWords()) {
                String key = WordWork.encodeForm(eachKey, true);
                String meaning = "";
                for (String eachMeaning : separateByComma(words.get(eachKey))) {
                    meaning += eachMeaning.replace(" ", "-") + " ";
                }
                meaning = WordWork.encodeForm(meaning.trim(), false);
                bw.write(key + " " + meaning + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
