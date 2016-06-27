package com.company.matt.jiramobile.JIRA;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Project implements Parcelable {
    private static final String LOG_TAG = Fields.class.getSimpleName();
    private String self;
    private String id;
    private String key;
    private String name;

    public Project(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Project(JSONObject jsonObject) {
        try {
            if(jsonObject.has("self")) {
                this.self = jsonObject.getString("self");
            }
            if(jsonObject.has("id")) {
                this.id = jsonObject.getString("id");
            }
            if(jsonObject.has("key")) {
                this.key = jsonObject.getString("key");
            }
            if(jsonObject.has("name")) {
                this.name = jsonObject.getString("name");
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",this.id);
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

    }

    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    private Project(Parcel in) {

    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}