package com.hisivasankar.flicksbuddy.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sivasankar on 12/24/2015.
 */
public class HTTPServiceHandler {

    private final static String GET = "GET";
    private final static String POST = "POST";

    private StringBuffer dataBuffer;
    private HttpURLConnection httpURLConnection;
    private BufferedReader bufferedReader;

    public String doGet(final String url, String caller) throws IOException {
        InputStream is;
        try {
            URL apiURL = new URL(url);
            httpURLConnection = (HttpURLConnection) apiURL.openConnection();
            httpURLConnection.setRequestMethod(GET);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() != 200) return null;
            is = httpURLConnection.getInputStream();
            if (is == null) return null;
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            dataBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                dataBuffer.append(line);
            }
            if (dataBuffer.length() == 0)
                throw new NullPointerException("Reponse from server is null");
            Log.d(caller, dataBuffer.toString());
            return dataBuffer.toString();
        } catch (IOException e) {
            Log.e(caller, e.getMessage());
            throw e;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(caller, "Unable to close BufferedReader");
                    throw e;
                }
            }
        }
    }
}
