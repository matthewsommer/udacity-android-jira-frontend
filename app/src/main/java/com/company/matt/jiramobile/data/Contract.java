package com.company.matt.jiramobile.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.company.matt.jiramobile";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TASK = "task";

    public static final class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;

        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_REMOTE_ID = "remote_id";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_CREATION_DATE = "creation_date";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PROJECT = "project";

        public static Uri buildTaskUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTaskUri() { return CONTENT_URI.buildUpon().build();}

        public static Uri buildTaskProjectUri(String project) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_PROJECT, project).build();
        }

        public static String getTaskIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getTaskProjectFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_PROJECT);
        }
    }
}