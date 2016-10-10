package com.company.matt.jiramobile.JIRA;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Priority implements Parcelable {
    private static final String LOG_TAG = Priority.class.getSimpleName();

    private String self;
    private String id;
    private String name;

    public Priority(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Priority(JSONObject jsonObject) {
        try {
            if(jsonObject.has("self")) {
                this.self = jsonObject.getString("self");
            }
            if(jsonObject.has("id")) {
                this.id = jsonObject.getString("id");
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

    public static final Parcelable.Creator<Priority> CREATOR = new Parcelable.Creator<Priority>() {
        public Priority createFromParcel(Parcel in) {
            return new Priority(in);
        }

        public Priority[] newArray(int size) {
            return new Priority[size];
        }
    };

    private Priority(Parcel in) {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}