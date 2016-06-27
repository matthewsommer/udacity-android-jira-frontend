package com.company.matt.jiramobile.JIRA;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "timetopretend.atlassian.net/rest/api/latest";
    public static final Uri BASE_CONTENT_URI = Uri.parse("https://" + CONTENT_AUTHORITY);
    public static final String PATH_TASK = "issue";

    public static final class IssueEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;

        public static final String PATH_SEARCH = "search";

        public static Uri buildIssueUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSearchUri() {
            return CONTENT_URI.buildUpon().appendPath("search")
                    .appendQueryParameter("jql", "resolution = Unresolved AND assignee in (currentUser()) ORDER BY issuetype ASC")
                    .appendQueryParameter("maxResults","1000")
                    .appendQueryParameter("fields","id,key,summary,description,priority,status,issuetype,project,created,updated,attachment,issuelinks,customfield_10009,customfield_10904,customfield_10905,customfield_11109,customfield_11103,customfield_10901,customfield_10900,customfield_11301")
                    .build();
        }
    }
}