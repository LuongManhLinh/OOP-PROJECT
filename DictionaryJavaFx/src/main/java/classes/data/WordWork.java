package classes.data;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordWork {
    public static String engStartKey = "$-";
    public static String engEndKey = "-$";
    public static String viStartKey = "$+";
    public static String viEndKey = "+$";
    public static String regexEng = "\\$-.+-\\$";
    public static String regexVi = "\\$\\+.+\\+\\$";
    public static Pattern engPattern = Pattern.compile(regexEng);
    public static Pattern viPattern = Pattern.compile(regexVi);

    public static String decodeForm (String input, boolean isKey) {
        String output = "";
        if (isKey) {
            Matcher engMatcher = engPattern.matcher(input);
            if (engMatcher.find()) {
                output = engMatcher.group();
                output = output.replace(engStartKey, "").replace(engEndKey, "");
                return output;
            } else {
                System.out.println("Cannot find a English form");
            }
        }

        Matcher viMatcher = viPattern.matcher(input);
        if (viMatcher.find()) {
            output = viMatcher.group();
            output = output.replace(viStartKey, "").replace(viEndKey, "");
        } else {
            System.out.println("Cannot find a Vietnamese form");
        }

        return output;
    }

    public static String decodeForm(String input, boolean isKey, int line) {
        String output = "";
        if (isKey) {
            Matcher engMatcher = engPattern.matcher(input);
            if (engMatcher.find()) {
                output = engMatcher.group();
                output = output.replace(engStartKey, "").replace(engEndKey, "");
                return output;
            } else {
                System.out.println("Cannot find a English form at line " + line);
            }
        }

        Matcher viMatcher = viPattern.matcher(input);
        if (viMatcher.find()) {
            output = viMatcher.group();
            output = output.replace(viStartKey, "").replace(viEndKey, "");
        } else {
            System.out.println("Cannot find a Vietnamese form at line " + line);
        }

        return output;
    }

    public static String encodeForm(String input, boolean isKey) {
        if (isKey) {
            return engStartKey + input + engEndKey;
        }

        return viStartKey + input + viEndKey;
    }

    public static String getRidOfHTMLForm(String htmlFormString) {
        htmlFormString = htmlFormString.replace("<br />", "\n");

        return htmlFormString;
    }

    public static String toHTMLForm(String input) {
        input = input.replace("\n", "<br />");

        return input;
    }

    public static String toHTMLMeaningStyle(String meaning) {
        Scanner sc = new Scanner(WordWork.getRidOfHTMLForm(meaning));

        String newM = "";
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            line = line.trim();
            if (line.isEmpty()) {
                newM += "<br />";
            } else {
                if (line.charAt(0) == '@') {
                    line = line.replace("@", "").trim();
                    Matcher matcher = Pattern.compile("/.+/").matcher(line);

                    if (matcher.find()) {
                        String pronounce = matcher.group();
                        line = line.replace(pronounce, "").trim();

                        line = "<p style=\"margin:0; color:red; font-size:30\"><b>" + line + "</p></b>"
                                + "<hr style=\"height:2px; background-color:rgb(93,47,193)\">"
                                + pronounce;
                    } else {
                        line = "<p style=\"margin:0; color:red; font-size:30px\"><b>" + line + "</p></b>";
                        line += "<hr style=\"height:2px; background-color:rgb(93,47,193)\">";
                    }
                } else if (line.charAt(0) == '*') {
                    line = line.replace("*", "").trim();
                    line = "<b><p style=\"margin:0; color:rgb(0,0,255); font-size:20px\">" + line + "</b></p>";
                } else if (line.charAt(0) == '-') {
                    line = line.substring(1).trim();
                    line = "<li style=\"color:rgb(200,0,200); font-size:17\">" + line + "</li>";
                } else if (line.charAt(0) == '=') {
                    line = line.substring(1).trim();
                    line = "<I>" + line + "</I><br />";
                } else if (line.charAt(0) == ':') {
                    line = line.replace(":", "").trim();
                    line = line + "<br />";
                } else if (line.charAt(0) == '!') {
                    line = line.substring(1).trim();
                    line = "<p style=\"margin:0; color:rgb(57,200,37); font-size:20;\">" + line + "</p>";
                } else {
                    Matcher matcher = Pattern.compile("/.+/").matcher(line);
                    if (matcher.find()) {
                        line = "<hr style=\"height:2px; background-color:rgb(93,47,193)\">" + line;
                    }
                }
                newM += "<ul style =\"margin:0; padding:0; list-style-position:inside;\">"
                        + line + "</ul>";
            }
        }
        return newM;
    }
}
