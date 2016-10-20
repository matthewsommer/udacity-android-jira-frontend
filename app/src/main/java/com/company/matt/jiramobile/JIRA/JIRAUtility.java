package com.company.matt.jiramobile.JIRA;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JIRAUtility {
    private static final String LOG_TAG = JIRAUtility.class.getSimpleName();

    public static List<Issue> getTaskDataFromJIRAJson(String jsonStr) throws JSONException {
        List<Issue> issues = new ArrayList<Issue>();
        JSONArray jsonArray = new JSONArray();
        try {
            if (jsonStr != null) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                if (jsonObject.has(Constants.JIRA_ISSUES)) {
                    jsonArray = jsonObject.getJSONArray(Constants.JIRA_ISSUES);
                } else {
                    jsonArray.put(jsonObject);
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    Issue issue = new Issue(jsonArray.getJSONObject(i));
                    issues.add(issue);
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return issues;
    }
}