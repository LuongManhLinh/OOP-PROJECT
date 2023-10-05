import edu.princeton.cs.algs4.*;

import java.util.ArrayList;

public class DictionaryManagement extends Dictionary {
    public static void InsertFromCommandline()
    {
        StdOut.print("Enter number of words that will be inserted, or enter -1 to exit this function: ");
        int numberInsert = StdIn.readInt();
        if(numberInsert == -1) return;
        StdIn.readLine();
        StdOut.println("If there are many meanings, please separate them by comma ','");

        for (int i = 0; i < numberInsert; i++)
        {
            StdOut.print( (i + 1) + ". Enter English word or enter -1 to exit this function: ");
            String key = (StdIn.readLine()).toLowerCase().trim();
            if(key.equals("-1")) return;

            while (EnglishKeyWords.contains(key))
            {
                StdOut.print("   The word has been inserted before! Please try again: ");
                key = (StdIn.readLine()).toLowerCase().trim();
                if(key.equals("-1")) return;
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
        StdIn.readLine();
        StdOut.print("Enter a word that you need or enter -1 to exit this function: ");
        String keyWord;
        boolean found = false;

        while(!found)
        {
            keyWord = (StdIn.readLine()).toLowerCase().trim();
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
                if(keyWord.equals("-1")) {
                    return;
                }
                StdOut.print("The word cannot be found! Please try again: ");
            }
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
        StdIn.readLine();
        StdOut.print("Enter an exact word that will be changed its meaning, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (StdIn.readLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);
                StdOut.print("Enter the meaning that will be change, or enter -1 to exit this function: ");
                String oldMeaning;
                ArrayList<String> meaning = Words.get(keyWord);
                boolean foundMeaning = false;

                while(!foundMeaning)
                {
                    oldMeaning = (StdIn.readLine()).toLowerCase().trim();
                    if (meaning.contains(oldMeaning))
                    {
                        foundMeaning = true;
                        StdOut.print("Enter new meaning: ");
                        String newMeaning = (StdIn.readLine()).toLowerCase().trim();

                        while (meaning.contains(newMeaning))
                        {
                            StdOut.print("The meaning is an old meaning, please try again: ");
                            newMeaning = (StdIn.readLine()).toLowerCase().trim();
                        }

                        meaning.remove(oldMeaning);
                        meaning.add(newMeaning);

                        PrintOneWord(keyWord);
                    }
                    else
                    {
                        if(oldMeaning.equals("-1"))
                        {
                            return;
                        }
                        StdOut.print("The meaning cannot be found! Please try again: ");
                    }
                }
            }
            else
            {
                if(keyWord.equals("-1"))
                {
                    return;
                }
                StdOut.print("The word cannot be found! Please try again: ");
            }
        }
    }

    public static void AddMeaning()
    {
        StdIn.readLine();
        StdOut.print("Enter an exact word that will be added meanings, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (StdIn.readLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
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
                StdOut.print("The word cannot be found! Please try again: ");
            }
        }
    }

    public static void FixEnglishWord()
    {
        StdIn.readLine();
        StdOut.print("Enter an exact word that will be fixed, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (StdIn.readLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
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
                if(keyWord.equals("-1"))
                {
                    return;
                }
                StdOut.print("The word cannot be found! Please try again: ");
            }
        }
    }

    public static void RemoveMeaning()
    {
        StdIn.readLine();
        StdOut.print("Enter an exact word that will be removed meaning, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (StdIn.readLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
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
                StdOut.print("The word cannot be found! Please try again: ");
            }
        }
    }

    public static void RemoveWord()
    {
        StdIn.readLine();
        StdOut.print("Enter an exact word that will be removed, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (StdIn.readLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);

                EnglishKeyWords.remove(keyWord);
                Words.remove(keyWord);
                StdOut.print("Removed!");
            }
            else
            {
                if(keyWord.equals("-1"))
                {
                    return;
                }
                StdOut.print("The word cannot be found! Please try again: ");
            }
        }
    }
}
