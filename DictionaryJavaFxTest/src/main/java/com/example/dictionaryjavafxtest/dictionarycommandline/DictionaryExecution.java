package com.example.dictionaryjavafxtest.dictionarycommandline;

import com.example.dictionaryjavafxtest.Dictionary;

import java.util.Scanner;

public class DictionaryExecution extends Dictionary {
    private static final String SHOW_ALL = "1";
    private static final String LOOK_UP = "2";
    private static final String INSERT = "3";
    private static final String EDIT_MEANING = "4";
    private static final String ADD_MEANING = "5";
    private static final String FIX_WORD = "6";
    private static final String REMOVE_WORD = "7";
    private static final String EXIT = "8";


    public static void Run()
    {
        String command;
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while(isRunning)
        {
            System.out.println("Enter " + SHOW_ALL + " to show all words");
            System.out.println("Enter " + LOOK_UP + " to look up words");
            System.out.println("Enter " + INSERT + " to insert words");
            System.out.println("Enter " + EDIT_MEANING + " to edit meaning in a word");
            System.out.println("Enter " + ADD_MEANING + " to add meanings");
            System.out.println("Enter " + FIX_WORD + " to fix a English word");
            System.out.println("Enter " + REMOVE_WORD + " to remove a word from the dictionary");
            System.out.println("Enter " + EXIT + " to update file and exit");

            System.out.println("**Note: While executing a function, you can enter `-1` to exit the function!");

            command = scanner.next();
            switch (command)
            {
                case SHOW_ALL -> DictionaryManagement.ShowAllWords();
                case LOOK_UP -> DictionaryManagement.LookUp();
                case INSERT -> DictionaryManagement.InsertFromCommandline();
                case EDIT_MEANING -> DictionaryManagement.EditMeaning();
                case ADD_MEANING -> DictionaryManagement.AddMeaning();
                case FIX_WORD -> DictionaryManagement.FixEnglishWord();
                case REMOVE_WORD -> DictionaryManagement.RemoveWord();
                case EXIT -> {
                    isRunning = false;
                    Dictionary.Exit();
                }
                default -> System.out.println("Invalid command! Please enter a valid command!");
            }
            if (command.equals(EXIT))
            {
                System.out.println("Saved! Goodbye!");
            }
            else
            {
                System.out.println("\nPlease enter next command:");
            }
        }
    }
}
