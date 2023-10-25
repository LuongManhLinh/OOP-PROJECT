package classes;

import java.util.*;


public abstract class Dictionary {
    public static enum Type {
        VI_EN,
        EN_VI
    }

    protected HashMap<String, String> words = new HashMap<>();
    protected TreeSet<String> keyWords = new TreeSet<>();

    public Dictionary() {
        awake();
    }

    public abstract void awake();
    public abstract void exit();

    public HashMap<String, String> getWords() {
        return words;
    }

    public TreeSet<String> getKeyWords() {
        return keyWords;
    }

    public ArrayList<String> getKeyWordsAsArray() {
        return new ArrayList<>(keyWords);
    }
}
