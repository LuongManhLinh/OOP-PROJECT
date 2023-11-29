package classes.dictionarycommandline;


import classes.EnViDictionary;

import java.util.Scanner;

public class DictionaryExecution {
    private static final String LOOK_UP = "1";
    private static final String INSERT = "2";
    private static final String EDIT_MEANING = "3";
    private static final String ADD_MEANING = "4";
    private static final String FIX_WORD = "5";
    private static final String REMOVE_WORD = "6";
    private static final String EXIT = "7";


    public static void Run()
    {
        String command;
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while(isRunning)
        {
            System.out.println("Nhập " + LOOK_UP + " để tra từ");
            System.out.println("Nhập " + INSERT + " để thêm từ");
            System.out.println("Nhập " + EDIT_MEANING + " để sửa nghĩa của một từ");
            System.out.println("Nhập " + ADD_MEANING + " để thêm nghĩa cho một từ");
            System.out.println("Nhập " + FIX_WORD + " để sửa một từ");
            System.out.println("Nhập " + REMOVE_WORD + " để xóa một từ trong từ điển");
            System.out.println("Nhập " + EXIT + " để cập nhật dữ liệu và thoát ứng dụng");

            System.out.println("**Lưu ý: Trong khi đang thực hiện một chức năng, bạn có thể nhập `-1` để thoát khỏi chức năng đó!");

            command = scanner.next();
            switch (command)
            {
                case LOOK_UP -> DictionaryManagement.lookUp();
                case INSERT -> DictionaryManagement.insertFromCommandline();
                case EDIT_MEANING -> DictionaryManagement.editMeaning();
                case ADD_MEANING -> DictionaryManagement.addMeaning();
                case FIX_WORD -> DictionaryManagement.fixEnglishWord();
                case REMOVE_WORD -> DictionaryManagement.removeWord();
                case EXIT -> {
                    isRunning = false;
                    EnViDictionary.getInstance().exit();
                }
                default -> System.out.println("Lệnh không hợp lệ! Vui lòng nhập lệnh hợp lệ!");
            }
            if (command.equals(EXIT))
            {
                System.out.println("Đã lưu! Tạm biệt!");
            }
            else
            {
                System.out.println("\nVui lòng nhập lệnh tiếp theo:");
            }
        }
    }
}
