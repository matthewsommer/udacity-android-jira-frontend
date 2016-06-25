package com.company.matt.jiramobile.Networking;

import android.util.Base64;
import android.util.Log;

import com.company.matt.jiramobile.BuildConfig;
import com.company.matt.jiramobile.DetailFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static String GetResponseStr(URL url) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponseStr = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            String encoded = Base64.encodeToString(
                    (BuildConfig.TEST_USERNAME + ":" + BuildConfig.TEST_PASSWORD)
                            .getBytes("UTF-8"), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            JsonResponseStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return JsonResponseStr;
    }
}