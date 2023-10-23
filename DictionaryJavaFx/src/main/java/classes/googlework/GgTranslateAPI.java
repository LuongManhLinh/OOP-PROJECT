package classes.googlework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GgTranslateAPI {
    private static String translate(String langFrom, String langTo, String text) throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycbxQ0-fILOpnen_t4Sg17ityIpkpgq8aHVGjf-fOedVCYQ3XrNpPVqt8bYUNWK5_CUI-9Q/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static void main(String[] args) throws IOException {
        String langFrom = "en";
        String langTo = "vi";
        String text = "Show me your code and conceal your data structures, and I shall" +
                " continue to be mystified. Show me your data structures, and I won't" +
                " usually need your code; it'll be obvious.";

        String res = translate(langFrom, langTo, text);
        System.out.println(res);
    }
}