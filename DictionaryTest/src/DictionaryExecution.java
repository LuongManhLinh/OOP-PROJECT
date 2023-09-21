import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class DictionaryExecution extends Dictionary {
    private static final String SHOW_ALL = "1";
    private static final String LOOK_UP = "2";
    private static final String INSERT = "3";
    private static final String CHANGE_MEANING = "4";
    private static final String ADD_MEANING = "5";
    private static final String FIX_WORD = "6";
    private static final String EXIT = "9";


    public static void Run()
    {
        Dictionary.Awake();
        StdOut.println("Press " + SHOW_ALL + " to show all words");
        StdOut.println("Press " + LOOK_UP + " to look up words");
        StdOut.println("Press " + INSERT + " to insert words");
        StdOut.println("Press " + CHANGE_MEANING + " to change a word's meaning");
        StdOut.println("Press " + ADD_MEANING + " to add meanings");
        StdOut.println("Press " + FIX_WORD + " to fix a English word");
        StdOut.println("Press " + EXIT + " to update file and exit");

        String command;
        boolean isRunning = true;
        while(isRunning)
        {
            command = StdIn.readString();

            switch (command)
            {
                case SHOW_ALL:
                    DictionaryCommandline.ShowAllWords();
                    break;
                case LOOK_UP:
                    DictionaryManagement.LookUp();
                    break;
                case INSERT:
                    DictionaryManagement.InsertFromCommandline();
                    break;
                case CHANGE_MEANING:
                    DictionaryManagement.ChangeMeaning();
                    break;
                case ADD_MEANING:
                    DictionaryManagement.AddMeaning();
                    break;
                case FIX_WORD:
                    DictionaryManagement.FixWord();
                    break;
                case EXIT:
                    isRunning = false;
                    Dictionary.Exit();
                    break;
                default:
                    StdOut.println("Invalid command! Please enter a valid command:");
                    break;
            }
        }

    }
}
