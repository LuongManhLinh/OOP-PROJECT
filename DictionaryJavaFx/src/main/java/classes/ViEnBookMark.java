package classes;

import classes.data.WordWork;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ViEnBookMark {
    private static ArrayList<String> vieKey = new ArrayList<>();
    private static final String VI_EN_MARK = "src/main/resources/data/ViEnBookMark.txt";

    public static void loadBookMark(){
        try {
            Scanner fileScanner = new Scanner(new FileReader(VI_EN_MARK));
            String key;
            while (fileScanner.hasNext()) {
                key = fileScanner.nextLine();

                vieKey.add(key);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void writeBookMark(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(VI_EN_MARK))) {
            for (String eachWord : vieKey) {

                bw.write(eachWord + " " + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> getVieKey(){
        return vieKey;
    }
    public static void insertToBookMark(String word){
        if(!vieKey.contains(word))
            vieKey.add(word);
    }
    public static boolean checkInBookMark(String word){
        return vieKey.contains(word);
    }
    public static void remove(String word){
        vieKey.remove(word);
    }
    public static void removeAll(){
        vieKey.clear();
    }

    public static ArrayList<String> lookUp(String engWord){
        ArrayList<String> result = new ArrayList<>();
        if (engWord != null && !engWord.isEmpty()) {
            engWord = engWord.trim();
            engWord = engWord.toLowerCase();
            int pos = DictionaryManagementForApp.binarySearch(engWord, vieKey);
            if (pos == -1) {
                return result;
            }
            for (int i = pos; i < vieKey.size(); i++) {
                if (vieKey.get(i).indexOf(engWord) == 0) {
                    result.add(vieKey.get(i));
                } else {
                    break;
                }
            }
        }
        return result;
    }
}

