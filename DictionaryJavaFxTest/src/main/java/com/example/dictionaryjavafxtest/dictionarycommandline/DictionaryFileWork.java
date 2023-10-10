package com.example.dictionaryjavafxtest.dictionarycommandline;

import com.example.dictionaryjavafxtest.Dictionary;

import java.io.*;
import java.util.ArrayList;

public class DictionaryFileWork extends Dictionary
{
    public static void ImportWords (String filePath)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String eachLine;

            while ((eachLine = br.readLine()) != null)
            {
                ArrayList<String> key = WordWork.SeparateWordsInLineBySpace(eachLine);
                for (int i = 0; i < key.size(); i++)
                {
                    key.set(i, key.get(i).replace("-", " "));
                }
                String EnglishWord = key.get(0);
                key.remove(0);

                EnglishKeyWords.add(EnglishWord);
                Words.put(EnglishWord, key);
                if (EnglishWord.length() > longestWordLength)
                {
                    longestWordLength = EnglishWord.length();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

   public static void ExportAllWords (String filePath)
   {
       try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath)))
       {
          for (String eachWord : EnglishKeyWords)
          {
              ArrayList<String> meanings = Words.get(eachWord);
              eachWord = eachWord.replace(" ", "-");
              eachWord = String.format("%-" + longestWordLength + "s", eachWord);

              StringBuilder allMeanings = new StringBuilder();

              for (String eachMeaning : meanings)
              {
                  allMeanings.append(eachMeaning.replace(" ", "-")).append(" ");
              }

              bw.write(eachWord + "\t" + allMeanings.toString() + "\n");
          }
       }
       catch (IOException e)
       {
           e.printStackTrace();
       }
   }


}
