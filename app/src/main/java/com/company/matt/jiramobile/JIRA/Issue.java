package com.company.matt.jiramobile.JIRA;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Issue implements Parcelable {
    private static final String LOG_TAG = JIRAUtility.class.getSimpleName();

    private int jira_id;
    private String summary;
    private String creation_date;
    private String priority;
    private String description;
    private String project;
    private String issue_type;

    public Issue(String summary, String project, String issue_type) {
        this.summary = summary;
        this.project = project;
        this.issue_type = issue_type;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(jira_id);
        out.writeString(summary);
        out.writeString(creation_date);
        out.writeString(priority);
        out.writeString(description);
        out.writeString(issue_type);
    }

    public static final Parcelable.Creator<Issue> CREATOR = new Parcelable.Creator<Issue>() {
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };

    private Issue(Parcel in) {
        jira_id = in.readInt();
        summary = in.readString();
        creation_date = in.readString();
        priority = in.readString();
        description = in.readString();
        issue_type = in.readString();
    }

    public String getSummary() {
        return summary;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public int getJira_id() {
        return jira_id;
    }

    public String getProject() {
        return project;
    }

    public String getIssue_type() {
        return issue_type;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setJira_id(int jira_id) {
        this.jira_id = jira_id;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Issue(JSONObject json) {
        try {
            this.setSummary(json.getString("summary"));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}