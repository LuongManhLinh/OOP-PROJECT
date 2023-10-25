package classes;

import classes.data.DictionaryData;

public class ViEnDictionary extends Dictionary {
    private static Dictionary instance = new ViEnDictionary();
    public static Dictionary getInstance() {
        return instance;
    }

    @Override
    public void awake() {
        keyWords = DictionaryData.getViKeyWords();
        words = DictionaryData.getEngWords();
    }

    @Override
    public void exit() {
        DictionaryData.writeData(Dictionary.Type.VI_EN, words, keyWords);
    }
}
