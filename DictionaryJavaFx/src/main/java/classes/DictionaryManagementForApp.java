package classes;

import java.util.ArrayList;

public class DictionaryManagementForApp {
    public static ArrayList<String> lookUp(String keyWord, Dictionary.Type type) {
        ArrayList<String> result = new ArrayList<>();
        if (keyWord != null && !keyWord.isEmpty()) {
            keyWord = keyWord.trim();
            if (type == Dictionary.Type.EN_VI) {
                keyWord = keyWord.toLowerCase();
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
                if (result.isEmpty()) {
                    keyWord = keyWord.toLowerCase();
                    for (String eachWord : ViEnDictionary.getInstance().keyWords) {
                        if (eachWord.toLowerCase().indexOf(keyWord) == 0) {
                            result.add(eachWord);
                        }
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

    public static ArrayList<String> binaryLookUp(String keyWord, Dictionary.Type type) {
        ArrayList<String> result = new ArrayList<>();
        if (keyWord != null && !keyWord.isEmpty()) {
            keyWord = keyWord.trim();
            if (type == Dictionary.Type.EN_VI) {
                keyWord = keyWord.toLowerCase();
                ArrayList<String> keyWords = EnViDictionary.getInstance().getKeyWordsAsArray();
                int pos = binarySearch(keyWord, keyWords);
                if (pos == -1) {
                    return result;
                }
                for (int i = pos; i < keyWords.size(); i++) {
                    if (keyWords.get(i).indexOf(keyWord) == 0) {
                        result.add(keyWords.get(i));
                    } else {
                        break;
                    }
                }
            } else if (type == Dictionary.Type.VI_EN) {
                ArrayList<String> keyWords = ViEnDictionary.getInstance().getKeyWordsAsArray();
                int pos = binarySearch(keyWord, keyWords);
                if (pos == -1) {
                    keyWord = keyWord.toLowerCase();
                    pos = binarySearch(keyWord, keyWords);
                    if (pos == -1) {
                        return result;
                    }
                }
                for (int i = pos; i < keyWords.size(); i++) {
                    if (keyWords.get(i).indexOf(keyWord) == 0) {
                        result.add(keyWords.get(i));
                    } else {
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static int binarySearch(String keyWord, ArrayList<String> words) {
        if (keyWord == null || keyWord.isEmpty() || words.isEmpty()) {
            return -1;
        }

        int result = -1;
        int left = 0;
        int right = words.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            String word = words.get(mid);
            if (word.charAt(0) == keyWord.charAt(0)) {
                if (word.indexOf(keyWord) == 0) {
                    result = mid;
                    right = mid - 1;
                } else if (word.compareTo(keyWord) < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else if (word.charAt(0) < keyWord.charAt(0)) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }
}
