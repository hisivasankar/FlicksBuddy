package com.hisivasankar.flicksbuddy.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by Sivasankar on 12/24/2015.
 */
public class HTTPServiceHandler {

    private final static String GET = "GET";
    private final static String POST = "POST";

    private StringBuffer dataBuffer;
    private HttpURLConnection httpURLConnection;
    private Uri.Builder uriBuilder;
    private BufferedReader bufferedReader;

    private URL getURL(String api, Map<String, String> params) throws MalformedURLException {
        if (params != null) {
            uriBuilder = Uri.parse(api).buildUpon();
            for (Map.Entry<String, String> param : params.entrySet()) {
                uriBuilder.appendQueryParameter(param.getKey(), param.getValue());
            }
        } else {
            return new URL(api);
        }
        Log.d("URL", uriBuilder.toString());
        return new URL(uriBuilder.toString());
    }

    public String doGet(final String caller, String api, Map<String, String> params) throws MalformedURLException {
        InputStream is;
        try {
            httpURLConnection = (HttpURLConnection) getURL(api, params).openConnection();
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
            if (dataBuffer.length() == 0) return null;
            return dataBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(caller, e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(caller, "Unable to close BufferedReader");
                }
            }
        }
        return null;
    }

    public String doPost(final String caller, String api, Map<String, String> params) {
        return null;
    }
}
