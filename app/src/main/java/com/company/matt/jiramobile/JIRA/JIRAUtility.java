package com.company.matt.jiramobile.JIRA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JIRAUtility {

    public static List<Task> getTaskDataFromJIRAJson(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray(Constants.JIRA_ISSUES);

        List<Task> tasks = new ArrayList<Task>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject tmpJSONObject = jsonArray.getJSONObject(i);

            JSONObject fieldsObject = tmpJSONObject.getJSONObject(Constants.JIRA_FIELDS);
            JSONObject priorityObject = fieldsObject.getJSONObject(Constants.JIRA_PRIORITY);
            JSONObject projectObject = fieldsObject.getJSONObject(Constants.JIRA_PROJECT);

            Task task = new Task(tmpJSONObject.getString(Constants.JIRA_ID),
                    fieldsObject.getString(Constants.JIRA_SUMMARY),
                    fieldsObject.getString(Constants.JIRA_CREATED_DATE),
                    priorityObject.getString(Constants.JIRA_NAME),
                    projectObject.getString(Constants.JIRA_NAME));
            tasks.add(task);
        }
        return tasks;
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