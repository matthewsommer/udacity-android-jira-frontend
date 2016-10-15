package com.company.matt.jiramobile.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.company.matt.jiramobile";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ISSUE = "issue";
    public static final String PATH_COMMENT = "comment";
    public static final String PATH_ATTACHMENT = "attachment";

    public static final class IssueEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ISSUE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ISSUE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ISSUE;

        //_ID and _Count are implemented by BaseColumns
        public static final String TABLE_NAME = "issues";
        public static final String COLUMN_REMOTE_ID = "remote_id";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_DESCRIPTION = "description";

        public static Uri buildIssueUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildIssueUri() { return CONTENT_URI.buildUpon().build();}

        public static Uri buildIssueCommentsUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).appendPath(PATH_COMMENT).build();
        }

        public static Uri buildIssueAttachmentsUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).appendPath(PATH_ATTACHMENT).build();
        }

        public static String getIssueIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class CommentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COMMENT;

        //_ID and _Count are implemented by BaseColumns
        public static final String TABLE_NAME = "comments";
        public static final String COLUMN_REMOTE_ID = "remote_id";
        public static final String COLUMN_JIRA_ISSUE_ID = "jira_issue_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CREATED_DATE = "created_date";
        public static final String COLUMN_BODY = "body";

        public static Uri buildCommentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildCommentUri() { return CONTENT_URI.buildUpon().build();}

        public static String getCommentIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class AttachmentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ATTACHMENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT;

        //_ID and _Count are implemented by BaseColumns
        public static final String TABLE_NAME = "attachments";
        public static final String COLUMN_REMOTE_ID = "remote_id";
        public static final String COLUMN_FILENAME = "filename";
        public static final String COLUMN_REMOTE_CONTENT_URL = "remote_content_url";
        public static final String COLUMN_JIRA_ISSUE_ID = "jira_issue_id";
        public static final String COLUMN_MIME_TYPE = "mime_type";

        public static Uri buildAttachmentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAttachmentUri() { return CONTENT_URI.buildUpon().build();}

        public static String getAttachmentIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}