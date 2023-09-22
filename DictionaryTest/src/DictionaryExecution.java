import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

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
        while(isRunning)
        {
            StdOut.println("Press " + SHOW_ALL + " to show all words");
            StdOut.println("Press " + LOOK_UP + " to look up words");
            StdOut.println("Press " + INSERT + " to insert words");
            StdOut.println("Press " + CHANGE_MEANING + " to change a word's meaning");
            StdOut.println("Press " + ADD_MEANING + " to add meanings");
            StdOut.println("Press " + FIX_WORD + " to fix a English word");
            StdOut.println("Press " + REMOVE_MEANING + " to remove a meaning in a word");
            StdOut.println("Press " + REMOVE_WORD + " to remove a word from the dictionary");
            StdOut.println("Press " + EXIT + " to update file and exit");

            command = StdIn.readString();

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
                default -> StdOut.println("Invalid command! Please enter a valid command!");
            }
            if (command.equals(EXIT))
            {
                StdOut.println("Saved! Goodbye!");
            }
            else
            {
                StdOut.println("\nPlease enter next command:");
            }
        }
    }
}
