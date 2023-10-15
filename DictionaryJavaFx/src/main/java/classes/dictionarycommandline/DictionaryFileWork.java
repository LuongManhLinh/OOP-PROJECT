package classes.dictionarycommandline;


import classes.Dictionary;

import java.io.*;

public class DictionaryFileWork extends Dictionary
{
    public static void importWords (String filePath)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String eachLine;

            while ((eachLine = br.readLine()) != null)
            {
                String englishKeyWord = WordWork.decodeForm(eachLine, true);
                String vietnameseMeaning = WordWork.decodeForm(eachLine, false);

                EnglishKeyWords.add(englishKeyWord);
                Words.put(englishKeyWord, vietnameseMeaning);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void exportAllWords (String filePath)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath)))
        {
            for (String eachWord : EnglishKeyWords)
            {
                String englishKeyWord = WordWork.encodeForm(eachWord, true);
                String vietnameseMeaning = WordWork.encodeForm(Words.get(eachWord), false);

                bw.write(englishKeyWord + " " + vietnameseMeaning + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
