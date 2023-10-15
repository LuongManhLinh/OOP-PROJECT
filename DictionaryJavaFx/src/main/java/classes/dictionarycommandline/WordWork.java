package classes.dictionarycommandline;

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

    public static String decodeForm (String input, boolean isEnglish) {
        String output = "";
        if (isEnglish) {
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

    public static String encodeForm(String input, boolean isEnglish) {
        if (isEnglish) {
            return engStartKey + input + engEndKey;
        }

        return viStartKey + input + viEndKey;
    }

    public static String getRidOfHTMLForm(String htmlFormString) {
        htmlFormString = htmlFormString.replace("<br />", "\n");
        htmlFormString = htmlFormString.replace("<I><Q>", "");
        htmlFormString = htmlFormString.replace("</Q></I>", "");

        return htmlFormString;
    }

    public static String toHTMLForm(String input) {
        input = input.replace("\n", "<br />");
        input = "<I><Q>" + input + "</Q></I>";

        return input;
    }
}
