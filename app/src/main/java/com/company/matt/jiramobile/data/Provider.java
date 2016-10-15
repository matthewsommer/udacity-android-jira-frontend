package com.company.matt.jiramobile.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.company.matt.jiramobile.data.Contract.IssueEntry;
import com.company.matt.jiramobile.data.Contract.CommentEntry;
import com.company.matt.jiramobile.data.Contract.AttachmentEntry;

public class Provider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    static final int ISSUE = 100;
    static final int ISSUE_WITH_ID = 101;
    static final int COMMENT = 200;
    static final int COMMENT_WITH_ID = 201;
    static final int COMMENTS_WITH_ISSUE_ID = 202;
    static final int ATTACHMENT = 300;
    static final int ATTACHMENT_WITH_ID = 301;
    static final int ATTACHMENTS_WITH_ISSUE_ID = 302;

    private static final SQLiteQueryBuilder sTaskQueryBuilder;

    static{
        sTaskQueryBuilder = new SQLiteQueryBuilder();
        sTaskQueryBuilder.setTables(Contract.IssueEntry.TABLE_NAME);
    }

    private static final String sId =
            Contract.IssueEntry._ID + " = ?";

    private Cursor getById(
            Uri uri, String[] projection, String sortOrder) {
        String issueId = Contract.IssueEntry.getIssueIdFromUri(uri);

        String selection = sId;
        String[] selectionArgs = new String[]{issueId};

        return sTaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCommentsByIssueId(
            Uri uri, String[] projection, String sortOrder) {
        String issueId = IssueEntry.getIssueIdFromUri(uri);

        String selection = sId;
        String[] selectionArgs = new String[]{issueId};

        return sTaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAttachmentsByIssueId(
            Uri uri, String[] projection, String sortOrder) {
        String issueId = IssueEntry.getIssueIdFromUri(uri);

        String selection = sId;
        String[] selectionArgs = new String[]{issueId};

        return sTaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;
        matcher.addURI(authority, Contract.PATH_ISSUE, ISSUE);
        matcher.addURI(authority, Contract.PATH_ISSUE + "/#", ISSUE_WITH_ID);
        matcher.addURI(authority, Contract.PATH_COMMENT, COMMENT);
        matcher.addURI(authority, Contract.PATH_COMMENT + "/#", COMMENT_WITH_ID);
        matcher.addURI(authority, Contract.PATH_ISSUE + "/#/" + Contract.PATH_COMMENT, COMMENTS_WITH_ISSUE_ID);
        matcher.addURI(authority, Contract.PATH_ATTACHMENT, ATTACHMENT);
        matcher.addURI(authority, Contract.PATH_ATTACHMENT + "/#", ATTACHMENT_WITH_ID);
        matcher.addURI(authority, Contract.PATH_ISSUE + "/#/" + Contract.PATH_ATTACHMENT, ATTACHMENTS_WITH_ISSUE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ISSUE:
                return Contract.IssueEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "issue/*"
            case ISSUE_WITH_ID: {
                retCursor = getById(uri, projection, sortOrder);
                break;
            }
            // "issue"
            case ISSUE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        IssueEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ISSUE: {
                normalizeDate(values);
                long _id = db.insert(Contract.IssueEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.IssueEntry.buildIssueUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case ISSUE:
                rowsDeleted = db.delete(
                        Contract.IssueEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {
//        if (values.containsKey(Contract.IssueEntry.COLUMN_CREATION_DATE)) {
//            String dateStr = values.getAsString(Contract.IssueEntry.COLUMN_CREATION_DATE);
//            if(!dateStr.isEmpty()) {
//                //values.put(Contract.IssueEntry.COLUMN_CREATION_DATE, Contract.normalizeDate(dateStr));
//            }
//        }
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ISSUE:
                normalizeDate(values);
                rowsUpdated = db.update(Contract.IssueEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ISSUE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(Contract.IssueEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}