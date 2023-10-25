package classes.dictionarycommandline;
import classes.EnViDictionary;
import classes.data.WordWork;

import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

public class DictionaryManagement {
    public static HashMap<String, String> words = EnViDictionary.getInstance().getWords();
    public static TreeSet<String> keyWords = EnViDictionary.getInstance().getKeyWords();

    public static void printOneWord(String exactWord) {
        System.out.println(exactWord + ": ");
        String meanings = WordWork.getRidOfHTMLForm(words.get(exactWord));
        System.out.println(meanings);
        System.out.println();
    }

    public static String getDescriptionCommandline(Scanner scanner) {
        StringBuilder meaning = new StringBuilder();
        String line;

        while (!(line = (scanner.nextLine()).toLowerCase().trim()).equals("$")) {
            if (line.equals("-1")) return "-1";

            meaning.append(line).append("\n");
        }
        // delete the last "\n"
        meaning.setLength(meaning.length() - 1);

        return WordWork.toHTMLForm(meaning.toString());
    }
    
    public static void insertFromCommandline()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số lượng từ sẽ được thêm vào: ");
        int numberInsert = scanner.nextInt();
        if(numberInsert == -1) return;
        scanner.nextLine();

        for (int i = 0; i < numberInsert; i++)
        {
            System.out.print( (i + 1) + ". Nhập từ tiếng Anh: ");
            String key = (scanner.nextLine()).toLowerCase().trim();
            if(key.equals("-1")) return;

            while (keyWords.contains(key))
            {
                System.out.print("   Từ đã tồn tại! Vui lòng nhập lại: ");
                key = (scanner.nextLine()).toLowerCase().trim();
                if(key.equals("-1")) return;
            }

            System.out.println("   nhập nghĩa tiếng Việt: ");
            System.out.println("   **(Lưu ý: Bạn phải nhập `$` ở một dòng riêng biệt sau khi nhập nghĩa để kết thúc)");
            String meaning = getDescriptionCommandline(scanner);
            if (meaning.equals("-1")) return;
            keyWords.add(key);
            words.put(key, meaning);
        }
    }

    public static void lookUp()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ mà bạn cần tra cứu: ");
        String keyWord;
        boolean found = false;

        while(!found)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            for (String eachWord : keyWords)
            {
                if (eachWord.indexOf(keyWord) == 0)
                {
                    printOneWord(eachWord);
                    found = true;
                }

                if (found && eachWord.indexOf(keyWord) != 0) {
                    break;
                }
            }

            if(!found)
            {
                if(keyWord.equals("-1")) {
                    return;
                }
                System.out.print("Không tìm thấy từ! Vui lòng nhập lại: ");
            }
        }
    }

    public static void addMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ mà bạn muốn thêm nghĩa: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (keyWords.contains(keyWord))
            {
                foundWord = true;
                printOneWord(keyWord);

                System.out.println("Nhập nghĩa tiếng Việt sẽ được thêm vào: ");
                System.out.println("**(Lưu ý: Bạn phải nhập `$` ở một dòng riêng biệt sau khi nhập nghĩa để kết thúc)");
                String meaning = getDescriptionCommandline(scanner);
                if (meaning.equals("-1")) return;
                String result = words.get(keyWord) + "<br />" + meaning;
                result = WordWork.getRidOfHTMLForm(result);
                result = WordWork.toHTMLForm(result);

                words.put(keyWord, result);

                printOneWord(keyWord);
            }
            else
            {
                if(keyWord.equals("-1"))
                {
                    return;
                }
                System.out.print("Không tìm thấy từ! Vui lòng nhập lại: ");
            }
        }
    }

    public static void fixEnglishWord()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ bạn muốn sửa: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (keyWords.contains(keyWord))
            {
                foundWord = true;
                printOneWord(keyWord);

                String oldKeyWord = keyWord;

                System.out.print("Nhập từ tiếng Anh sau khi sửa: ");
                keyWord = (scanner.nextLine()).toLowerCase().trim();

                while (keyWord.isEmpty()) {
                    System.out.print("Từ tiếng Anh sau khi sửa không thể rỗng, vui lòng nhập lại: ");
                    keyWord = (scanner.nextLine()).toLowerCase().trim();
                }

                String meaning = words.get(oldKeyWord);
                keyWords.remove(oldKeyWord);
                words.remove(oldKeyWord);

                keyWords.add(keyWord);
                words.put(keyWord, meaning);

                printOneWord(keyWord);
            }
            else
            {
                if(keyWord.equals("-1"))
                {
                    return;
                }
                System.out.print("Không tìm thấy từ! Vui lòng nhập lại: ");
            }
        }
    }

    public static void editMeaning()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ bạn muốn sửa nghĩa: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (keyWords.contains(keyWord))
            {
                foundWord = true;
                printOneWord(keyWord);

                System.out.println("**(Lưu ý: toàn bộ nghĩa cũ sẽ bị thay thế bởi nghĩa mới nhập vào)");
                System.out.println("**(Lưu ý: Bạn phải nhập `$` ở một dòng riêng biệt sau khi nhập nghĩa để kết thúc)");
                System.out.println("Nhập nghĩa tiếng Việt mới cho từ: ");

                String meaning = getDescriptionCommandline(scanner);
                if (meaning.equals("-1")) return;

                words.put(keyWord, meaning);

                printOneWord(keyWord);
            }
            else
            {
                if(keyWord.equals("-1"))
                {
                    return;
                }
                System.out.print("Không tìm thấy từ! Vui lòng thử lại: ");
            }
        }
    }

    public static void removeWord()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập từ bạn muốn xóa: ");
        String keyWord;
        boolean foundWord = false;

        while(!foundWord)
        {
            keyWord = (scanner.nextLine()).toLowerCase().trim();
            if (keyWords.contains(keyWord))
            {
                foundWord = true;
                printOneWord(keyWord);

                keyWords.remove(keyWord);
                words.remove(keyWord);
                System.out.println("Đã xóa!");
            }
            else
            {
                if(keyWord.equals("-1")) return;

                System.out.print("Không tìm thấy từ! Vui lòng thử lại: ");
            }
        }
    }
}
