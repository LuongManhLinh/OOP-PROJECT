package com.example.dictionaryjavafxtest.dictionarycommandline;

import com.example.dictionaryjavafxtest.Dictionary;

import java.util.Scanner;

public class DictionaryManagement extends Dictionary {
    public static void PrintOneWord(String exactWord)
    {
        System.out.println(exactWord + ": ");
        String meanings = WordWork.getRidOfHTMLForm(Words.get(exactWord));
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

    public static void ShowAllWords()
    {
        int i = 1;
        for (String eachWord : EnglishKeyWords)
        {
            System.out.print(i + ". ");
            PrintOneWord(eachWord);
            i++;
        }
        if (i == 1)
        {
            System.out.println("The dictionary is empty!");
        }
    }
    
    public static void InsertFromCommandline()
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

            while (EnglishKeyWords.contains(key))
            {
                System.out.print("   The word are existed! Please enter again: ");
                key = (scanner.nextLine()).toLowerCase().trim();
                if(key.equals("-1")) return;
            }

            System.out.println("   Describe Vietnamese meaning: ");
            System.out.println("   **(Note: you have to enter `$` in a simple line to finish describing, or enter `-1` to exit the function)");
            String meaning = getDescriptionCommandline(scanner);
            if (meaning.equals("-1")) return;
            EnglishKeyWords.add(key);
            Words.put(key, meaning);
        }
    }

    public static void LookUp()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word that you need: ");
        String keyWord;
        boolean found = false;

        while(!found)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            for (String eachWord : EnglishKeyWords)
            {
                if (eachWord.indexOf(keyWord) == 0)
                {
                    PrintOneWord(eachWord);
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

    public static void AddMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be added meanings: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);

                System.out.println("Describe adding Vietnamese meaning: ");
                System.out.println("**(Note: you have to enter `$` in a simple line to finish describing, or enter `-1` to exit the function)");
                String meaning = getDescriptionCommandline(scanner);
                if (meaning.equals("-1")) return;
                String result = Words.get(keyWord) + "<br />" + meaning;
                result = WordWork.getRidOfHTMLForm(result);
                result = WordWork.toHTMLForm(result);

                Words.put(keyWord, result);

                PrintOneWord(keyWord);
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

    public static void FixEnglishWord()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be fixed: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);

                String oldKeyWord = keyWord;

                System.out.print("Enter new English word: ");
                keyWord = (scanner.nextLine()).toLowerCase().trim();

                while (keyWord.isEmpty()) {
                    System.out.print("New English word cannot be empty, please enter again: ");
                    keyWord = (scanner.nextLine()).toLowerCase().trim();
                }

                String meaning = Words.get(oldKeyWord);
                EnglishKeyWords.remove(oldKeyWord);
                Words.remove(oldKeyWord);

                EnglishKeyWords.add(keyWord);
                Words.put(keyWord, meaning);

                PrintOneWord(keyWord);
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

    public static void EditMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be edited meaning: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);

                System.out.println("Enter the whole new meaning description: ");
                System.out.println("**(Be careful: the whole old meaning description will be removed by the new one!");
                System.out.println("**(Note: you have to enter `$` in a simple line to finish describing, or enter `-1` to exit the function)");

                String meaning = getDescriptionCommandline(scanner);
                if (meaning.equals("-1")) return;

                Words.put(keyWord, meaning);

                PrintOneWord(keyWord);
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

    public static void RemoveWord()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be removed: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);

                EnglishKeyWords.remove(keyWord);
                Words.remove(keyWord);
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
