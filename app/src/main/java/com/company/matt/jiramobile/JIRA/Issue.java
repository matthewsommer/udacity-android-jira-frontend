package com.company.matt.jiramobile.JIRA;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Issue implements Parcelable {
    private static final String LOG_TAG = JIRAUtility.class.getSimpleName();
    private int jira_id;
    private String self;
    private String key;
    private Fields fields;

    public Issue(Fields fields) {
        this.fields = fields;
    }

    public Issue(JSONObject jsonObject) {
        try {
            if (jsonObject.has("id")) {
                this.jira_id = jsonObject.getInt("id");
            }
            if (jsonObject.has("self")) {
                this.self = jsonObject.getString("self");
            }
            if (jsonObject.has("key")) {
                this.self = jsonObject.getString("key");
            }
            if (jsonObject.has("fields")) {
                this.fields = new Fields(jsonObject.getJSONObject("fields"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fields",this.fields.toJSONObject());
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
        out.writeInt(jira_id);
        out.writeString(self);
        out.writeString(key);
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
        self = in.readString();
        key = in.readString();
    }

    public int getJira_id() {
        return jira_id;
    }

    public void setJira_id(int jira_id) {
        this.jira_id = jira_id;
    }


    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

}