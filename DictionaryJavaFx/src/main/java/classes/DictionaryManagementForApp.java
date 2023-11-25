package classes;

import javafx.scene.control.Button;

import java.util.ArrayList;

public class DictionaryManagementForApp {
    public static String getMeaning(String keyWord, Dictionary.Type type) {
        String meaning = "";
        if (type == Dictionary.Type.EN_VI) {
            meaning = EnViDictionary.getInstance().getWords().get(keyWord);
        } else if (type == Dictionary.Type.VI_EN) {
            meaning = ViEnDictionary.getInstance().getWords().get(keyWord);
        }
        return meaning;
    }

    public static ArrayList<String> lookUp(String keyWord, Dictionary.Type type) {
        ArrayList<String> result = new ArrayList<>();
        if (keyWord != null && !keyWord.isEmpty()) {
            keyWord = keyWord.trim();
            if (type == Dictionary.Type.EN_VI) {
                keyWord = keyWord.toLowerCase();
                ArrayList<String> keyWords = EnViDictionary.getInstance().getKeyWords();
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
                ArrayList<String> keyWords = ViEnDictionary.getInstance().getKeyWords();
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

    public static void insert(String keyWord, String meaning, Dictionary.Type type) {
        if (type == Dictionary.Type.EN_VI) {
            binaryInsert(keyWord, EnViDictionary.getInstance().getKeyWords());
            EnViDictionary.getInstance().getWords().put(keyWord, meaning);
        } else if (type == Dictionary.Type.VI_EN) {
            binaryInsert(keyWord, ViEnDictionary.getInstance().getKeyWords());
            ViEnDictionary.getInstance().getWords().put(keyWord, meaning);
        }
    }

    public static void remove(String keyWord, Dictionary.Type type) {
        if (type == Dictionary.Type.EN_VI) {
            EnViDictionary.getInstance().getWords().remove(keyWord);
            EnViDictionary.getInstance().getKeyWords().remove(keyWord);
        } else if (type == Dictionary.Type.VI_EN) {
            ViEnDictionary.getInstance().getWords().remove(keyWord);
            ViEnDictionary.getInstance().getKeyWords().remove(keyWord);
        }
    }

    static int binarySearch(String keyWord, ArrayList<String> words) {
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

    static void binaryInsert(String keyWord, ArrayList<String> words) {
        if (keyWord == null || keyWord.isEmpty()) {
            return;
        }

        int left = 0;
        int right = words.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (mid == 0) {
                if (words.get(mid).compareTo(keyWord) < 0) {
                    words.add(mid + 1, keyWord);
                } else {
                    words.add(mid, keyWord);
                }
                return;
            }

            if (mid == words.size() - 1) {
                if (words.get(mid).compareTo(keyWord) < 0) {
                    words.add(keyWord);
                } else {
                    words.add(mid, keyWord);
                }
                return;
            }

            if (words.get(mid).compareTo(keyWord) > 0 && words.get(mid - 1).compareTo(keyWord) < 0) {
                words.add(mid, keyWord);
                return;
            } else if (words.get(mid).compareTo(keyWord) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
}
