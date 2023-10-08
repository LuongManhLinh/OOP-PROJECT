
public class DictionaryCommandline extends Dictionary {
    public static void ShowAllWords()
    {
        int i = 1;
        for (String eachWord : EnglishKeyWords)
        {
            System.out.print(i + ". ");
            DictionaryManagement.PrintOneWord(eachWord);
            i++;
        }
        if (i == 1)
        {
            System.out.println("The dictionary is empty!");
        }
    }
}
