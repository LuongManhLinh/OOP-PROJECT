package classes;

import classes.data.WordWork;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ViEnBookMark {
    private static ArrayList<String> vieKey = new ArrayList<>();
    private static final String EN_VI_MARK = "src/main/resources/data/ViEnBookMark.txt";

    public static void loadBookMark(){
        try {
            Scanner fileScanner = new Scanner(new FileReader(EN_VI_MARK));
            String line, key;
            while (fileScanner.hasNext()) {
                line = fileScanner.nextLine();

                key = WordWork.decodeForm(line, true);
                vieKey.add(key);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void writeBookMark(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EN_VI_MARK))) {
            for (String eachWord : vieKey) {
                String vieKeyWord = WordWork.encodeForm(eachWord, true);

                bw.write(vieKeyWord + " " + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> getEngKey(){
        return vieKey;
    }
    public static void insertToBookMark(String word){
        if(!vieKey.contains(word))
            vieKey.add(word);
    }
    //    public static List<String> lookUpInBookMark(String word){
//        List<>
//    }
    public static boolean checkInBookMark(String word){
        return vieKey.contains(word);
    }
    public static void remove(String word){
        vieKey.remove(word);
    }
}
