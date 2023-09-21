import edu.princeton.cs.algs4.StdOut;


public class DictionaryCommandline extends Dictionary {
    public static void ShowAllWords()
    {
        int i = 1;
        for (String eachWord : EnglishKeyWords)
        {
            StdOut.print(i + ". ");
            DictionaryManagement.PrintOneWord(eachWord);
            i++;
        }
        if (i == 1)
        {
            StdOut.println("The dictionary is empty!");
        }
        StdOut.println("Done! Please enter next command:");
    }
}
