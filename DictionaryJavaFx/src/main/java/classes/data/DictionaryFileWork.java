package classes.data;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

public class DictionaryFileWork
{
    public static void importAllWords(String filePath, HashMap<String, String> words, ArrayList<String> keyWords){
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line, key, meaning;
        while (fileScanner.hasNext()) {
            line = fileScanner.nextLine();

            key = WordWork.decodeForm(line, true);
            meaning = WordWork.decodeForm(line, false);

            words.put(key, meaning);
            keyWords.add(key);
        }
    }

    public static void importAllWords(String filePath, HashMap<String, String> words, TreeSet<String> keyWords){
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line, key, meaning;
        while (fileScanner.hasNext()) {
            line = fileScanner.nextLine();

            key = WordWork.decodeForm(line, true);
            meaning = WordWork.decodeForm(line, false);

            words.put(key, meaning);
            keyWords.add(key);
        }
    }


    public static void exportAllWords (String filePath, HashMap<String, String> words, TreeSet<String> keyWords)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath)))
        {
            for (String eachWord : keyWords)
            {
                String englishKeyWord = WordWork.encodeForm(eachWord, true);
                String vietnameseMeaning = WordWork.encodeForm(words.get(eachWord), false);

                bw.write(englishKeyWord + " " + vietnameseMeaning + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
