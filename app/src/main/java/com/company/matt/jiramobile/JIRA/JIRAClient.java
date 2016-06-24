package com.company.matt.jiramobile.JIRA;

import android.net.Uri;
import android.util.Log;
import com.company.matt.jiramobile.Networking.Client;
import org.json.JSONException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class JIRAClient {
    private static final String LOG_TAG = JIRAClient.class.getSimpleName();

    public static List<Task> FetchTasks() {
        String JsonResponseStr = null;
        Uri builtUri = null;

        builtUri = Uri.parse(Constants.JIRA_BASE_URL).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JsonResponseStr = Client.GetResponseStr(url);

        try {
            return JIRAUtility.getTaskDataFromJIRAJson(JsonResponseStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}