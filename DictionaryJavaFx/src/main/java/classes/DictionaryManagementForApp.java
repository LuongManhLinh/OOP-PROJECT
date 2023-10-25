package classes;

import java.util.ArrayList;

public class DictionaryManagementForApp {
    public static ArrayList<String> lookUp(String keyWord, Dictionary.Type type) {
        ArrayList<String> result = new ArrayList<>();
        if (keyWord != null && !keyWord.isEmpty()) {
            keyWord = keyWord.trim();
            if (type == Dictionary.Type.EN_VI) {
                for (String eachWord : EnViDictionary.getInstance().keyWords) {
                    if (eachWord.indexOf(keyWord) == 0) {
                        result.add(eachWord);
                    }
                }
            } else if (type == Dictionary.Type.VI_EN) {
                for (String eachWord : ViEnDictionary.getInstance().keyWords) {
                    if (eachWord.indexOf(keyWord) == 0) {
                        result.add(eachWord);
                    }
                }
            }
        }
        return result;
    }

    public static String getMeaning(String keyWord, Dictionary.Type type) {
        String meaning = "";
        if (type == Dictionary.Type.EN_VI) {
            meaning = EnViDictionary.getInstance().getWords().get(keyWord);
        } else if (type == Dictionary.Type.VI_EN) {
            meaning = ViEnDictionary.getInstance().getWords().get(keyWord);
        }
        return meaning;
    }
}
