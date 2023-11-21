package classes;

import classes.data.DictionaryData;

public class EnViDictionary extends Dictionary {
    private static Dictionary instance = null;
    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new EnViDictionary();
        }
        return instance;
    }
    @Override
    public void awake() {
        keyWords = DictionaryData.getEngKeyWords();
        words = DictionaryData.getViWords();
    }

    @Override
    public void exit() {
        DictionaryData.writeData(Dictionary.Type.EN_VI, words, keyWords);
    }
}
