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
            JSONObject jsonObject = new JSONObject(jsonStr);
            if(jsonObject.has(Constants.JIRA_ISSUES)) {
                jsonArray = jsonObject.getJSONArray(Constants.JIRA_ISSUES);
            }
            else {
                jsonArray.put(jsonObject);
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmpJSONObject = jsonArray.getJSONObject(i);

                JSONObject fieldsObject = tmpJSONObject.getJSONObject(Constants.JIRA_FIELDS);
                JSONObject priorityObject = fieldsObject.getJSONObject(Constants.JIRA_PRIORITY);
                JSONObject projectObject = fieldsObject.getJSONObject(Constants.JIRA_PROJECT);

                Issue issue = new Issue(fieldsObject.getString(Constants.JIRA_SUMMARY),
                        projectObject.getString(Constants.JIRA_NAME),"");
                issue.setJira_id(tmpJSONObject.getInt(Constants.JIRA_ID));
                issues.add(issue);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return issues;
    }

    public static List<Comment> getCommentsFromJIRAJson(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray(Constants.JIRA_COMMENTS);

        List<Comment> comments = new ArrayList<Comment>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject tmpJSONObject = jsonArray.getJSONObject(i);
            JSONObject authorObject = tmpJSONObject.getJSONObject(Constants.JIRA_AUTHOR);

            Comment comment = new Comment("1","Matt","Comment1");
            comments.add(comment);
        }
        return comments;
    }

    public static List<Attachment> getAttachmentsFromJIRAJson(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray(Constants.JIRA_ISSUES);

        List<Attachment> attachments = new ArrayList<Attachment>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject tmpJSONObject = jsonArray.getJSONObject(i);
        }
        return attachments;
    }
}