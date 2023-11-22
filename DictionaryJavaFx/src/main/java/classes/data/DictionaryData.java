package classes.data;

import classes.Dictionary;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DictionaryData {
    private static HashMap<String, String> viWords = new HashMap<>();
    private static ArrayList<String> engKeyWords = new ArrayList<>();
    private static HashMap<String, String> engWords = new HashMap<>();
    private static ArrayList<String> viKeyWords = new ArrayList<>();

    private static final String EN_VI_PATH = "src/main/resources/data/anhvietData.txt";
    private static final String VI_EN_PATH = "src/main/resources/data/vietanhData.txt";



    public static void loadData() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            DictionaryFileWork.importAllWords(EN_VI_PATH, viWords, engKeyWords);
        });
        executorService.submit(() -> {
            DictionaryFileWork.importAllWords(VI_EN_PATH, engWords, viKeyWords);
        });
        executorService.shutdown();
        
//        DictionaryFileWork.importAllWords(EN_VI_PATH, viWords, engKeyWords);
//        DictionaryFileWork.importAllWords(VI_EN_PATH, engWords, viKeyWords);
    }

    public static void writeData(Dictionary.Type type, HashMap<String, String> words, ArrayList<String> keyWords) {
        if (type == Dictionary.Type.EN_VI) {
            DictionaryFileWork.exportAllWords(EN_VI_PATH, words, keyWords);
        } else if (type == Dictionary.Type.VI_EN) {
            DictionaryFileWork.exportAllWords(VI_EN_PATH, words, keyWords);
        }
    }

    public static HashMap<String, String> getViWords() {
        return viWords;
    }

    public static ArrayList<String> getEngKeyWords() {
        return engKeyWords;
    }

    public static HashMap<String, String> getEngWords() {
        return engWords;
    }

    public static ArrayList<String> getViKeyWords() {
        return viKeyWords;
    }
}
