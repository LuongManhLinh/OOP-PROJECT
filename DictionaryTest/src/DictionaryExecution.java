import java.util.Scanner;

public class DictionaryExecution extends Dictionary {
    private static final String SHOW_ALL = "1";
    private static final String LOOK_UP = "2";
    private static final String INSERT = "3";
    private static final String CHANGE_MEANING = "4";
    private static final String ADD_MEANING = "5";
    private static final String FIX_WORD = "6";
    private static final String REMOVE_MEANING = "7";
    private static final String REMOVE_WORD = "8";
    private static final String EXIT = "9";


    public static void Run()
    {
        Dictionary.Awake();

        String command;
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while(isRunning)
        {
            System.out.println("Press " + SHOW_ALL + " to show all words");
            System.out.println("Press " + LOOK_UP + " to look up words");
            System.out.println("Press " + INSERT + " to insert words");
            System.out.println("Press " + CHANGE_MEANING + " to change a word's meaning");
            System.out.println("Press " + ADD_MEANING + " to add meanings");
            System.out.println("Press " + FIX_WORD + " to fix a English word");
            System.out.println("Press " + REMOVE_MEANING + " to remove a meaning in a word");
            System.out.println("Press " + REMOVE_WORD + " to remove a word from the dictionary");
            System.out.println("Press " + EXIT + " to update file and exit");

//            System.out.println("If you want to exit function, press -1!");
            command = scanner.next();
            switch (command)
            {
                case SHOW_ALL -> DictionaryCommandline.ShowAllWords();
                case LOOK_UP -> DictionaryManagement.LookUp();
                case INSERT -> DictionaryManagement.InsertFromCommandline();
                case CHANGE_MEANING -> DictionaryManagement.ChangeMeaning();
                case ADD_MEANING -> DictionaryManagement.AddMeaning();
                case FIX_WORD -> DictionaryManagement.FixEnglishWord();
                case REMOVE_MEANING -> DictionaryManagement.RemoveMeaning();
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
