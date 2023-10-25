package classes.data;

import classes.Dictionary;

import java.util.HashMap;
import java.util.TreeSet;

public class DictionaryData {
    private static HashMap<String, String> viWords = new HashMap<>();
    private static TreeSet<String> engKeyWords = new TreeSet<>();
    private static HashMap<String, String> engWords = new HashMap<>();
    private static TreeSet<String> viKeyWords = new TreeSet<>();

    private static final String EN_VI_PATH = "src/main/resources/anhvietData.txt";
    private static final String VI_EN_PATH = "src/main/resources/vietanhData.txt";



    public static void loadData() {
        DictionaryFileWork.importAllWords(EN_VI_PATH, viWords, engKeyWords);
        DictionaryFileWork.importAllWords(VI_EN_PATH, engWords, viKeyWords);
    }

    public static void writeData(Dictionary.Type type, HashMap<String, String> words, TreeSet<String> keyWords) {
        if (type == Dictionary.Type.EN_VI) {
            DictionaryFileWork.exportAllWords(EN_VI_PATH, words, keyWords);
        } else if (type == Dictionary.Type.VI_EN) {
            DictionaryFileWork.exportAllWords(VI_EN_PATH, words, keyWords);
        }
    }

    public static HashMap<String, String> getViWords() {
        return viWords;
    }

    public static TreeSet<String> getEngKeyWords() {
        return engKeyWords;
    }

    public static HashMap<String, String> getEngWords() {
        return engWords;
    }

    public static TreeSet<String> getViKeyWords() {
        return viKeyWords;
    }
}
