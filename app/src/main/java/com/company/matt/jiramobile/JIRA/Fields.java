package com.company.matt.jiramobile.JIRA;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Fields implements Parcelable {
    private static final String LOG_TAG = Fields.class.getSimpleName();
    private String summary;
    private Project project;
    private Issuetype issuetype;

    public Fields(String summary, Project project, Issuetype issuetype) {
        this.summary = summary;
        this.project = project;
        this.issuetype = issuetype;
    }

    public Fields(JSONObject jsonObject) {
        try {
            if (jsonObject.has("summary")) {
                this.summary = jsonObject.getString("summary");
            }
            if (jsonObject.has("project")) {
                this.project = new Project(jsonObject.getJSONObject(Constants.JIRA_PROJECT));
            }
            if (jsonObject.has("issuetype")) {
                this.issuetype = new Issuetype(jsonObject.getJSONObject("issuetype"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("summary",this.summary);
            jsonObject.put("project",this.project.toJSONObject());
            jsonObject.put("issuetype",this.issuetype.toJSONObject());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return jsonObject;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(summary);
    }

    public static final Parcelable.Creator<Fields> CREATOR = new Parcelable.Creator<Fields>() {
        public Fields createFromParcel(Parcel in) {
            return new Fields(in);
        }

        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };

    private Fields(Parcel in) {
        summary = in.readString();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}