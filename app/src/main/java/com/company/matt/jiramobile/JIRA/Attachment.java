package com.company.matt.jiramobile.JIRA;

import android.os.Parcel;
import android.os.Parcelable;

public class Attachment implements Parcelable  {
    String id;
    String key;
    String name;
    String site;

    public Attachment(String id, String key, String name, String site){
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(key);
        out.writeString(name);
        out.writeString(site);
    }

    public static final Parcelable.Creator<Attachment> CREATOR
            = new Parcelable.Creator<Attachment>() {
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };

    private Attachment(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}