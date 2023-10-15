package classes;

import java.util.ArrayList;

public class DictionaryManagementForApp extends Dictionary {
    public static ArrayList<String> lookUp(String keyWord) {
        ArrayList<String> result = new ArrayList<>();
        if (keyWord != null && !keyWord.isEmpty()) {
            keyWord = keyWord.trim();
            for (String eachWord : EnglishKeyWords)
            {
                if (eachWord.indexOf(keyWord) == 0)
                {
                    result.add(eachWord);
                }

            }
        }
        return result;
    }
}
