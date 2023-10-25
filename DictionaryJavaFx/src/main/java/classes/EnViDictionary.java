package classes;

import classes.data.DictionaryData;

public class EnViDictionary extends Dictionary {
    private static Dictionary instance = new EnViDictionary();
    public static Dictionary getInstance() {
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
