package classes.googlework;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class GgTranslateTextToSpeech {
    public static String errorString = "|o!d^x<@";
    private static ExecutorService audioExe = null;
    private static Player audioPlayer = null;
    private static InputStream getAudio(String text, String languageOutput) throws IOException {
        String urlStr = "https://translate.google.com/translate_tts?ie=UTF-8&tl="
                + languageOutput
                + "&client=tw-ob&q="
                + URLEncoder.encode(text, StandardCharsets.UTF_8);

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream audioSrc = con.getInputStream();
        return new BufferedInputStream(audioSrc);
    }

    public static String play(String text, String languageOutput) {
        InputStream audio = null;
        try {
            audio = getAudio(text, languageOutput);
        } catch (IOException e) {
            return errorString;
        }

        if (audio == null) {
            return errorString;
        }

        audioExe = Executors.newSingleThreadExecutor();
        InputStream finalAudio = audio;
        audioExe.submit(() -> {
            try {
                audioPlayer = new Player(finalAudio);
                audioPlayer.play();
            } catch (JavaLayerException e) {

            }
        });
        audioExe.shutdown();
        return "";
    }

    public static void stop() {
        if (audioPlayer != null && !audioPlayer.isComplete()) {
            audioPlayer.close();
            if (audioExe != null) {
                audioExe.shutdown();
            }
        }
    }

}