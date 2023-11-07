package classes;

import java.util.*;


public abstract class Dictionary {
    public enum Type {
        VI_EN,
        EN_VI
    }

    protected HashMap<String, String> words = new HashMap<>();
    protected ArrayList<String> keyWords = new ArrayList<>();

    public Dictionary() {
        awake();
    }

    public abstract void awake();
    public abstract void exit();

    public HashMap<String, String> getWords() {
        return words;
    }

    public ArrayList<String> getKeyWords() {
        return keyWords;
    }

}
