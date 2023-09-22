import edu.princeton.cs.algs4.*;

import java.util.ArrayList;

public class DictionaryManagement extends Dictionary {
    public static void InsertFromCommandline()
    {
        StdOut.print("Enter number of words that will be inserted: ");
        int numberInsert = StdIn.readInt();
        StdIn.readLine();
        StdOut.println("If there are many meanings, please separate them by comma ','");

        for (int i = 0; i < numberInsert; i++)
        {
            StdOut.print( (i + 1) + ". Enter English word: ");
            String key = (StdIn.readLine()).toLowerCase().trim();

            while (EnglishKeyWords.contains(key))
            {
                StdOut.println("   The word has been inserted before! ");
                StdOut.print("    Please enter again: ");
                key = (StdIn.readLine()).toLowerCase().trim();
            }

            StdOut.print("   Enter Vietnamese meaning: ");
            String meaning = (StdIn.readLine()).toLowerCase().trim();

            EnglishKeyWords.add(key);

            ArrayList<String> VNMeaning = WordWork.SeparateWordsInLineByComma(meaning);

            Words.put(key, VNMeaning);

            if (key.length() > longestWordLength)
            {
                longestWordLength = key.length();
            }


        }
    }

    public static void LookUp()
    {
        StdOut.print("Enter a word that you need: ");
        String keyWord = (StdIn.readString()).toLowerCase();

        boolean found = false;
        for (String eachWord : EnglishKeyWords)
        {
            if (eachWord.indexOf(keyWord) == 0)
            {
                PrintOneWord(eachWord);
                found = true;
            }
        }

        if(!found)
        {
            StdOut.println("The word cannot be found!");
        }
    }

    public static void PrintOneWord(String exactWord)
    {
        StdOut.print(exactWord + ": ");
        ArrayList<String> meanings = Words.get(exactWord);
        for (int j = 0; j < meanings.size() - 1; j++)
        {
            StdOut.print(meanings.get(j) + ", ");
        }
        StdOut.println(meanings.get(meanings.size() - 1));
    }


    public static void ChangeMeaning()
    {
        StdOut.print("Enter an exact word that will be changed its meaning: ");
        String keyWord = (StdIn.readString()).toLowerCase();
        StdIn.readLine();

        if (EnglishKeyWords.contains(keyWord))
        {
            PrintOneWord(keyWord);
            StdOut.print("Enter the meaning that will be change: ");
            String oldMeaning = (StdIn.readLine()).toLowerCase().trim();
            ArrayList<String> meaning = Words.get(keyWord);

            if (meaning.contains(oldMeaning))
            {
                StdOut.print("Enter new meaning: ");
                String newMeaning = (StdIn.readLine()).toLowerCase().trim();

                while (meaning.contains(newMeaning))
                {
                    StdOut.print("The meaning is an old meaning, please enter again: ");
                    newMeaning = (StdIn.readLine()).toLowerCase().trim();
                }

                meaning.remove(oldMeaning);
                meaning.add(newMeaning);

                PrintOneWord(keyWord);
            }
            else
            {
                StdOut.println("The meaning cannot be found!");
            }
        }
        else
        {
            StdOut.println("The word cannot be found!");
        }
    }

    public static void AddMeaning()
    {
        StdOut.print("Enter an exact word that will be added meanings: ");
        String keyWord = (StdIn.readString()).toLowerCase().trim();

        if (EnglishKeyWords.contains(keyWord))
        {
            PrintOneWord(keyWord);
            StdOut.print("Enter number of meaning: ");
            int numberMeaning = StdIn.readInt();
            StdIn.readLine();

            StdOut.println("Enter meaning:");
            ArrayList<String> meaningContainer = Words.get(keyWord);
            for (int i = 0; i < numberMeaning; i++)
            {
                StdOut.print((i + 1) + ". ");
                String newMeaning = (StdIn.readLine()).toLowerCase().trim();
                while (meaningContainer.contains(newMeaning))
                {
                    StdOut.println("Existed meaning!");
                    newMeaning = (StdIn.readLine()).toLowerCase().trim();
                }

                meaningContainer.add(newMeaning);
            }
        }
        else
        {
            StdOut.println("The word cannot be found!");
        }
    }

    public static void FixEnglishWord()
    {
        StdOut.print("Enter an exact word that will be fixed: ");
        String keyWord = (StdIn.readString()).toLowerCase().trim();
        StdIn.readLine();

        if (EnglishKeyWords.contains(keyWord))
        {
            PrintOneWord(keyWord);

            ArrayList<String> meaning = Words.get(keyWord);
            EnglishKeyWords.remove(keyWord);
            Words.remove(keyWord);

            StdOut.print("Enter new English word: ");
            keyWord = (StdIn.readLine()).toLowerCase().trim();

            EnglishKeyWords.add(keyWord);
            Words.put(keyWord, meaning);

            PrintOneWord(keyWord);
        }
        else
        {
            StdOut.println("The word cannot be found!");
        }
    }

    public static void RemoveMeaning()
    {
        StdOut.print("Enter an exact word that will be removed meaning: ");
        String keyWord = (StdIn.readString()).toLowerCase().trim();
        StdIn.readLine();

        if (EnglishKeyWords.contains(keyWord))
        {
            PrintOneWord(keyWord);

            StdOut.print("Enter the meaning that will be removed: ");
            String removingMeaning = (StdIn.readLine()).toLowerCase().trim();
            ArrayList<String> meaning = Words.get(keyWord);

            if (meaning.contains(removingMeaning))
            {
                meaning.remove(removingMeaning);
                PrintOneWord(keyWord);
            }
            else
            {
                StdOut.println("The meaning cannot be found!");
            }
        }
        else
        {
            StdOut.println("The word cannot be found!");
        }
    }

    public static void RemoveWord()
    {
        StdOut.print("Enter an exact word that will be removed: ");
        String keyWord = (StdIn.readString()).toLowerCase().trim();
        StdIn.readLine();

        if (EnglishKeyWords.contains(keyWord))
        {
            PrintOneWord(keyWord);

            EnglishKeyWords.remove(keyWord);
            Words.remove(keyWord);
            StdOut.print("Removed!");
        }
        else
        {
            StdOut.println("The word cannot be found!");
        }
    }

}
