package classes;
import classes.data.DictionaryData;
import classes.data.WordWork;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EnViBookMark {
    private static ArrayList<String> engKey = new ArrayList<>();
    private static final String EN_VI_MARK = "src/main/resources/data/EnViBookMark.txt";

    public static void loadBookMark(){
        try {
            Scanner fileScanner = new Scanner(new FileReader(EN_VI_MARK));
            String key;
            while (fileScanner.hasNext()) {
                key = fileScanner.nextLine();

                engKey.add(key);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void writeBookMark(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EN_VI_MARK))) {
            for (String eachWord : engKey) {

                bw.write(eachWord + " " + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> getEngKey(){
        return engKey;
    }
    public static void insertToBookMark(String word){
        if(!engKey.contains(word))
            engKey.add(word);
    }

    public static boolean checkInBookMark(String word){
        return engKey.contains(word);
    }
    public static void remove(String word){
        engKey.remove(word);
    }
    public static void removeAll(){
        engKey.clear();
    }
    public static ArrayList<String> lookUp(String engWord){
        ArrayList<String> result = new ArrayList<>();
        if (engWord != null && !engWord.isEmpty()) {
            engWord = engWord.trim();
            engWord = engWord.toLowerCase();
            int pos = DictionaryManagementForApp.binarySearch(engWord, engKey);
            if (pos == -1) {
                return result;
            }
            for (int i = pos; i < engKey.size(); i++) {
                if (engKey.get(i).indexOf(engWord) == 0) {
                    result.add(engKey.get(i));
                } else {
                    break;
                }
            }
        }
        return result;
    }
}
