package com.jayway.robojs.javascript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

final class JavaScriptDownloader {

    public static String downloadScript(String address) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String content = null;
        String status = null;
        int statusCode = 0;

        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Pragma", "no-cache");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Expires", "0");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);

            status = connection.getResponseMessage();
            statusCode = connection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                content = builder.toString();
            }
        } catch (IOException e) {
            Log.e("MYTAG", statusCode + ": " + status, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Throwable t) {
                Log.e("MYTAG", "Couldn't close resources properly", t);
            }
        }

        return content;
    }

}
