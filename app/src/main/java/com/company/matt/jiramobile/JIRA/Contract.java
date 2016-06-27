package com.company.matt.jiramobile.JIRA;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "timetopretend.atlassian.net/rest/api/latest";
    public static final Uri BASE_CONTENT_URI = Uri.parse("https://" + CONTENT_AUTHORITY);
    public static final String PATH_ISSUE = "issue";
    public static final String PATH_SEARCH = "search";

    public static final class IssueEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ISSUE).build();

        public static final Uri SEARCH_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ISSUE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ISSUE;

        public static Uri buildIssueUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSearchUri() {
            return SEARCH_URI.buildUpon().appendQueryParameter("jql", "resolution = Unresolved AND assignee in (currentUser()) ORDER BY issuetype ASC")
                    .appendQueryParameter("maxResults","1000")
                    .build();
        }
    }
}