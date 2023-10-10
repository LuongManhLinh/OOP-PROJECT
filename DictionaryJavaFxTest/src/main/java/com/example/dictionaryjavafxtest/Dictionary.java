package com.example.dictionaryjavafxtest;

import com.example.dictionaryjavafxtest.dictionarycommandline.DictionaryFileWork;

import java.util.*;


public class Dictionary {
    protected static HashMap<String, ArrayList<String>> Words = new HashMap<>();
    protected static TreeSet<String> EnglishKeyWords = new TreeSet<>();

    protected static int longestWordLength = 0;

    protected static String fileContainsWords = "src/main/resources/Dictionary.txt";

    public static void Awake()
    {
        DictionaryFileWork.ImportWords(fileContainsWords);
    }

    public static void Exit()
    {
        DictionaryFileWork.ExportAllWords(fileContainsWords);
    }
}
