package com.company.matt.jiramobile.JIRA;

import android.net.Uri;
import android.util.Log;

import com.company.matt.jiramobile.networking.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IssueDAOImpl implements IssueDAO {
    private static final String LOG_TAG = IssueDAOImpl.class.getSimpleName();

    public void IssueDaoImpl() { }

    @Override
    public boolean delete(int jira_id) {
        String responseStr = null;
        Uri builtUri = Contract.IssueEntry.buildIssueUri(jira_id);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        responseStr = Client.GetResponseStr("DELETE",url,"");

        if(responseStr != null && responseStr.equalsIgnoreCase("204")) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public List<Issue> getAll() {
        String responseStr = null;
        Uri builtUri = null;

        builtUri = Uri.parse(Constants.JIRA_BASE_URL).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        responseStr = Client.GetResponseStr("GET",url,"");

        try {
            return JIRAUtility.getTaskDataFromJIRAJson(responseStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Issue get(int jira_id) {
        String JsonResponseStr = null;
        Uri builtUri = Contract.IssueEntry.buildIssueUri(jira_id);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JsonResponseStr = Client.GetResponseStr("GET",url,"");

        List<Issue> issues = new ArrayList<Issue>();

        try {
            issues = JIRAUtility.getTaskDataFromJIRAJson(JsonResponseStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        if(issues.size() > 0) {
            return issues.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public Issue update(Issue issue) {
        String JsonResponseStr = null;
        Uri builtUri = null;

        builtUri = Uri.parse(Constants.JIRA_REST_URL+"/issue/"+issue.getJira_id()).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject fieldsObject = new JSONObject();
        JSONObject projectObject = new JSONObject();
        JSONObject issuetypeObject = new JSONObject();

        try {
            projectObject.put("id",issue.getProject());
            issuetypeObject.put("id",issue.getIssue_type());
            fieldsObject.put("summary",issue.getSummary());
            jsonObject.put("fields",fieldsObject);

        }  catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        JsonResponseStr = Client.GetResponseStr("PUT",url,jsonObject.toString());

        JSONObject responseJSON = new JSONObject();
        if(responseJSON.length() > 0) {
            try {
                responseJSON = new JSONObject(JsonResponseStr);
                issue.setJira_id(responseJSON.getInt("id"));

            }  catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

        }
        return issue;
    }

    @Override
    public Issue create(Issue issue) {
        String JsonResponseStr = null;
        Uri builtUri = null;

        builtUri = Uri.parse(Constants.JIRA_REST_URL+"/issue").buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject fieldsObject = new JSONObject();
        JSONObject projectObject = new JSONObject();
        JSONObject issuetypeObject = new JSONObject();

        try {
            projectObject.put("id",issue.getProject());
            issuetypeObject.put("id",issue.getIssue_type());
            fieldsObject.put("project",projectObject);
            fieldsObject.put("summary",issue.getSummary());
            fieldsObject.put("issuetype",issuetypeObject);
            jsonObject.put("fields",fieldsObject);

        }  catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        JsonResponseStr = Client.GetResponseStr("POST",url,jsonObject.toString());

        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON = new JSONObject(JsonResponseStr);
            issue.setJira_id(responseJSON.getInt("id"));

        }  catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return issue;
    }
}