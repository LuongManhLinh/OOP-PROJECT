package classes.dictionarycommandline;
import classes.EnViDictionary;
import classes.data.WordWork;

import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

public class DictionaryManagement {
    public static HashMap<String, String> words = EnViDictionary.getInstance().getWords();
    public static TreeSet<String> keyWords = EnViDictionary.getInstance().getKeyWords();

    public static void printOneWord(String exactWord) {
        System.out.println(exactWord + ": ");
        String meanings = WordWork.getRidOfHTMLForm(words.get(exactWord));
        System.out.println(meanings);
        System.out.println();
    }

    public static String getDescriptionCommandline(Scanner scanner) {
        StringBuilder meaning = new StringBuilder();
        String line;

        while (!(line = (scanner.nextLine()).toLowerCase().trim()).equals("$")) {
            if (line.equals("-1")) return "-1";

            meaning.append(line).append("\n");
        }
        // delete the last "\n"
        meaning.setLength(meaning.length() - 1);

        return WordWork.toHTMLForm(meaning.toString());
    }
    
    public static void insertFromCommandline()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of words that will be inserted: ");
        int numberInsert = scanner.nextInt();
        if(numberInsert == -1) return;
        scanner.nextLine();

        for (int i = 0; i < numberInsert; i++)
        {
            System.out.print( (i + 1) + ". Enter English word: ");
            String key = (scanner.nextLine()).toLowerCase().trim();
            if(key.equals("-1")) return;

            while (keyWords.contains(key))
            {
                System.out.print("   The word are existed! Please enter again: ");
                key = (scanner.nextLine()).toLowerCase().trim();
                if(key.equals("-1")) return;
            }

            System.out.println("   Describe Vietnamese meaning: ");
            System.out.println("   **(Note: you have to enter `$` in a simple line to finish describing, or enter `-1` to exit the function)");
            String meaning = getDescriptionCommandline(scanner);
            if (meaning.equals("-1")) return;
            keyWords.add(key);
            words.put(key, meaning);
        }
    }

    public static void lookUp()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word that you need: ");
        String keyWord;
        boolean found = false;

        while(!found)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            for (String eachWord : keyWords)
            {
                if (eachWord.indexOf(keyWord) == 0)
                {
                    printOneWord(eachWord);
                    found = true;
                }

                if (found && eachWord.indexOf(keyWord) != 0) {
                    break;
                }
            }

            if(!found)
            {
                if(keyWord.equals("-1")) {
                    return;
                }
                System.out.print("The word cannot be found! Please try again: ");
            }
        }
    }

    public static void addMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be added meanings: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (keyWords.contains(keyWord))
            {
                foundWord = true;
                printOneWord(keyWord);

                System.out.println("Describe adding Vietnamese meaning: ");
                System.out.println("**(Note: you have to enter `$` in a simple line to finish describing, or enter `-1` to exit the function)");
                String meaning = getDescriptionCommandline(scanner);
                if (meaning.equals("-1")) return;
                String result = words.get(keyWord) + "<br />" + meaning;
                result = WordWork.getRidOfHTMLForm(result);
                result = WordWork.toHTMLForm(result);

                words.put(keyWord, result);

                printOneWord(keyWord);
            }
            else
            {
                if(keyWord.equals("-1"))
                {
                    return;
                }
                System.out.print("The word cannot be found! Please try again: ");
            }
        }
    }

    public static void fixEnglishWord()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be fixed: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (keyWords.contains(keyWord))
            {
                foundWord = true;
                printOneWord(keyWord);

                String oldKeyWord = keyWord;

                System.out.print("Enter new English word: ");
                keyWord = (scanner.nextLine()).toLowerCase().trim();

                while (keyWord.isEmpty()) {
                    System.out.print("New English word cannot be empty, please enter again: ");
                    keyWord = (scanner.nextLine()).toLowerCase().trim();
                }

                String meaning = words.get(oldKeyWord);
                keyWords.remove(oldKeyWord);
                words.remove(oldKeyWord);

                keyWords.add(keyWord);
                words.put(keyWord, meaning);

                printOneWord(keyWord);
            }
            else
            {
                if(keyWord.equals("-1"))
                {
                    return;
                }
                System.out.print("The word cannot be found! Please try again: ");
            }
        }
    }

    public static void editMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be edited meaning: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (keyWords.contains(keyWord))
            {
                foundWord = true;
                printOneWord(keyWord);

                System.out.println("Enter the whole new meaning description: ");
                System.out.println("**(Be careful: the whole old meaning description will be removed by the new one!");
                System.out.println("**(Note: you have to enter `$` in a simple line to finish describing, or enter `-1` to exit the function)");

                String meaning = getDescriptionCommandline(scanner);
                if (meaning.equals("-1")) return;

                words.put(keyWord, meaning);

                printOneWord(keyWord);
            }
            else
            {
                if(keyWord.equals("-1"))
                {
                    return;
                }
                System.out.print("The word cannot be found! Please try again: ");
            }
        }
    }

    public static void removeWord()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be removed: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (keyWords.contains(keyWord))
            {
                foundWord = true;
                printOneWord(keyWord);

                keyWords.remove(keyWord);
                words.remove(keyWord);
                System.out.println("Removed!");
            }
            else
            {
                if(keyWord.equals("-1")) return;

                System.out.print("The word cannot be found! Please try again: ");
            }
        }
    }
}
