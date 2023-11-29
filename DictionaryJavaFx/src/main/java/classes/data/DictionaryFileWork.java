package classes.data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DictionaryFileWork {
    public static void importAllWords(String filePath, HashMap<String, String> words, ArrayList<String> keyWords){
        try {
            Scanner fileScanner = new Scanner(new FileReader(filePath));
            String line, key, meaning;
            while (fileScanner.hasNext()) {
                line = fileScanner.nextLine();

                key = WordWork.decodeForm(line, true);
                meaning = WordWork.decodeForm(line, false);

                words.put(key, meaning);
                keyWords.add(key);
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void exportAllWords (String filePath, HashMap<String, String> words, ArrayList<String> keyWords) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String eachWord : keyWords) {
                String englishKeyWord = WordWork.encodeForm(eachWord, true);
                String vietnameseMeaning = WordWork.encodeForm(words.get(eachWord), false);

                bw.write(englishKeyWord + " " + vietnameseMeaning + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void importAllWordsForShootingGame(String filePath, HashMap<String, ArrayList<String>> words, ArrayList<String> keyWords) {
        try {
            Scanner fileScanner = new Scanner(new FileReader(filePath));
            String line, key, meaning;
            while (fileScanner.hasNext()) {
                line = fileScanner.nextLine();
                key = WordWork.decodeForm(line, true);
                meaning = WordWork.decodeForm(line, false);

                keyWords.add(key);
                words.put(key, WordWork.separateMeaningForShootingGame(meaning));
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
