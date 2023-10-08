import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryManagement extends Dictionary {
    public static void PrintOneWord(String exactWord)
    {
        System.out.print(exactWord + ": ");
        ArrayList<String> meanings = Words.get(exactWord);
        for (int j = 0; j < meanings.size() - 1; j++)
        {
            System.out.print(meanings.get(j) + ", ");
        }
        System.out.println(meanings.get(meanings.size() - 1));
    }
    
    public static void InsertFromCommandline()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of words that will be inserted, or enter -1 to exit this function: ");
        int numberInsert = scanner.nextInt();
        if(numberInsert == -1) return;
        scanner.nextLine();
        System.out.println("If there are many meanings, please separate them by comma ','");

        for (int i = 0; i < numberInsert; i++)
        {
            System.out.print( (i + 1) + ". Enter English word or enter -1 to exit this function: ");
            String key = (scanner.nextLine()).toLowerCase().trim();
            if(key.equals("-1")) return;

            while (EnglishKeyWords.contains(key))
            {
                System.out.print("   The word has been inserted before! Please try again: ");
                key = (scanner.nextLine()).toLowerCase().trim();
                if(key.equals("-1")) return;
            }

            System.out.print("   Enter Vietnamese meaning: ");
            String meaning = (scanner.nextLine()).toLowerCase().trim();

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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word that you need or enter -1 to exit this function: ");
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
    public static void ChangeMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be changed its meaning, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);
                System.out.print("Enter the meaning that will be change, or enter -1 to exit this function: ");
                String oldMeaning;
                ArrayList<String> meaning = Words.get(keyWord);
                boolean foundMeaning = false;

                while(!foundMeaning)
                {
                    oldMeaning = (scanner.nextLine()).toLowerCase().trim();
                    if (meaning.contains(oldMeaning))
                    {
                        foundMeaning = true;
                        System.out.print("Enter new meaning: ");
                        String newMeaning = (scanner.nextLine()).toLowerCase().trim();

                        while (meaning.contains(newMeaning))
                        {
                            System.out.print("The meaning is an old meaning, please try again: ");
                            newMeaning = (scanner.nextLine()).toLowerCase().trim();
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
                        System.out.print("The meaning cannot be found! Please try again: ");
                    }
                }
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

    public static void AddMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be added meanings, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);
                System.out.print("Enter number of meaning: ");
                int numberMeaning = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Enter meaning:");
                ArrayList<String> meaningContainer = Words.get(keyWord);
                for (int i = 0; i < numberMeaning; i++)
                {
                    System.out.print((i + 1) + ". ");
                    String newMeaning = (scanner.nextLine()).toLowerCase().trim();
                    while (meaningContainer.contains(newMeaning))
                    {
                        System.out.println("Existed meaning!");
                        newMeaning = (scanner.nextLine()).toLowerCase().trim();
                    }

                    meaningContainer.add(newMeaning);
                }
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
        System.out.print("Enter an exact word that will be fixed, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);

                ArrayList<String> meaning = Words.get(keyWord);
                EnglishKeyWords.remove(keyWord);
                Words.remove(keyWord);

                System.out.print("Enter new English word: ");
                keyWord = (scanner.nextLine()).toLowerCase().trim();

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

    public static void RemoveMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an exact word that will be removed meaning, or enter -1 to exit this function: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (EnglishKeyWords.contains(keyWord))
            {
                foundWord = true;
                PrintOneWord(keyWord);

                System.out.print("Enter the meaning that will be removed: ");
                String removingMeaning = (scanner.nextLine()).toLowerCase().trim();
                ArrayList<String> meaning = Words.get(keyWord);

                if (meaning.contains(removingMeaning))
                {
                    meaning.remove(removingMeaning);
                    PrintOneWord(keyWord);
                }
                else
                {
                    System.out.println("The meaning cannot be found!");
                }
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
        System.out.print("Enter an exact word that will be removed, or enter -1 to exit this function: ");
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
                if(keyWord.equals("-1"))
                {
                    return;
                }
                System.out.print("The word cannot be found! Please try again: ");
            }
        }
    }
}
