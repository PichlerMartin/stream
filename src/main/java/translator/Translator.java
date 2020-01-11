package translator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * https://whatsmate.github.io/2016-08-18-translate-text-java/
 */
public class Translator {
    private static final String CLIENT_ID = "FREE_TRIAL_ACCOUNT";
    private static final String CLIENT_SECRET = "PUBLIC_SECRET";
    private static final String ENDPOINT = "http://api.whatsmate.net/v1/translation/translate";

    /**
     * Entry Point
     */
    public static void main(String[] args) throws Exception {
        // TODO: specify language here
        String fromLang = "en";
        String toLang = "es";
        String text = "Let's have some fun!";

        List<Object> text = Translator.readFile();

        Translator.translate(fromLang, toLang, text);
    }

    private static ArrayList<String> readFile(){
        Scanner s = null;
        try {
            s = new Scanner(new File(Objects.requireNonNull(Translator.class.getClassLoader().getResource("database.properties")).getFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //noinspection MismatchedQueryAndUpdateOfCollection
        ArrayList<String> list = new ArrayList<String>();
        while (Objects.requireNonNull(s).hasNext()){
            list.add(s.next());
        }
        s.close();
    }

    /**
     * Sends out a WhatsApp message via WhatsMate WA Gateway.
     */
    private static void translate(String fromLang, String toLang, String text) throws Exception {
        String jsonPayload = "{" +
                "\"fromLang\":\"" +
                fromLang +
                "\"," +
                "\"toLang\":\"" +
                toLang +
                "\"," +
                "\"text\":\"" +
                text +
                "\"" +
                "}";

        URL url = new URL(ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
        conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
        conn.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn.getOutputStream();
        os.write(jsonPayload.getBytes());
        os.flush();
        os.close();

        int statusCode = conn.getResponseCode();
        System.out.println("Status Code: " + statusCode);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()
        ));
        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
        conn.disconnect();
    }

}