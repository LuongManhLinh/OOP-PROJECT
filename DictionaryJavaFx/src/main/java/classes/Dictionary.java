package classes;

import classes.dictionarycommandline.DictionaryFileWork;

import java.util.*;


public class Dictionary {
    protected static HashMap<String, String> Words = new HashMap<>();
    protected static TreeSet<String> EnglishKeyWords = new TreeSet<>();

    protected static String fileContainsWords = "src/main/resources/dictionaryData.txt";

    public static void Awake()
    {
        DictionaryFileWork.importWords(fileContainsWords);
    }

    public static void Exit()
    {
        DictionaryFileWork.exportAllWords(fileContainsWords);
    }

    public static HashMap<String, String> getWords() {
        return Words;
    }

    public static TreeSet<String> getEnglishKeyWords() {
        return EnglishKeyWords;
    }

    public static ArrayList<String> getEnglishKeyWord() {
        return new ArrayList<>(EnglishKeyWords);
    }
}
