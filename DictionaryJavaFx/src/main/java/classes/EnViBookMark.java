package classes;
import classes.data.DictionaryData;
import classes.data.WordWork;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EnViBookMark{
    private static ArrayList<String> engKey = new ArrayList<>();
    private static final String EN_VI_MARK = "src/main/resources/data/EnViBookMark.txt";

    public static void loadBookMark(){
        try {
            Scanner fileScanner = new Scanner(new FileReader(EN_VI_MARK));
            String line, key;
            while (fileScanner.hasNext()) {
                line = fileScanner.nextLine();

                key = WordWork.decodeForm(line, true);
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
                String englishKeyWord = WordWork.encodeForm(eachWord, true);

                bw.write(englishKeyWord + " " + "\n");
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
    //    public static List<String> lookUpInBookMark(String word){
//        List<>
//    }
    public static boolean checkInBookMark(String word){
        return engKey.contains(word);
    }
    public static void remove(String word){
        engKey.remove(word);
    }
}
