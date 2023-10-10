package com.example.dictionaryjavafxtest.dictionarycommandline;

import java.util.ArrayList;
import java.util.Scanner;

public class WordWork {
    public static ArrayList<String> SeparateWordsInLineBySpace(String input)
    {
        // Type of input: word1 word2 word3
        ArrayList<String> output = new ArrayList<>();
        Scanner scanner = new Scanner(input);
        while(scanner.hasNext())
        {
            output.add(scanner.next());
        }

        return output;
    }

    public static ArrayList<String> SeparateWordsInLineByComma(String input)
    {
        // Type of input: word1, word2, word3

        // input = chiến binh,  áo trắng, hai

        input = input.replace(" ", "-");
        // input = chiến-binh,--áo-trắng,-hai

        input = input.replace(",", " ");
        // input = chiến-binh --áo-trắng -hai

        ArrayList<String> output = SeparateWordsInLineBySpace(input);
        for(int i = 0; i < output.size(); i++)
        {
            output.set(i, output.get(i).replace("-", " ").trim());
        }
        // output = 1.chiến binh 2.áo trắng 3.hai

        return output;
    }
}
