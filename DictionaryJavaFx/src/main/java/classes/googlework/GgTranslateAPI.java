package classes.googlework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GgTranslateAPI {
    public static String errorString = "|o!d^x<@";
    private static BufferedReader connect(String langFrom, String langTo, String text) throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycbxQ0-fILOpnen_t4Sg17ityIpkpgq8aHVGjf-fOedVCYQ3XrNpPVqt8bYUNWK5_CUI-9Q/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        return new BufferedReader(new InputStreamReader(con.getInputStream()));
    }

    public static String translate(String langFrom, String langTo, String text) {
        BufferedReader in;
        try {
            in = connect(langFrom, langTo, text);
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("&#39;")) {
                    inputLine = inputLine.replace("&#39;", "'");
                }
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (IOException e) {
            return errorString;
        }
    }
}