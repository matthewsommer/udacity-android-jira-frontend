package com.company.matt.jiramobile.JIRA;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    String id;
    String remote_id;
    String summary;
    String creation_date;
    String priority;
    String description;
    String project;

    public Task(String id,
                String summary,
                String creation_date,
                String priority,
                String project){
        this.id = id;
        this.summary = summary;
        this.creation_date = creation_date;
        this.priority = priority;
        this.description = description;
        this.project = project;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(summary);
        out.writeString(creation_date);
        out.writeString(priority);
        out.writeString(description);
    }

    public static final Parcelable.Creator<Task> CREATOR
            = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    private Task(Parcel in) {
        id = in.readString();
        summary = in.readString();
        creation_date = in.readString();
        priority = in.readString();
        description = in.readString();
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

    public String getId() {
        return id;
    }

    public String getProject() {
        return project;
    }
}