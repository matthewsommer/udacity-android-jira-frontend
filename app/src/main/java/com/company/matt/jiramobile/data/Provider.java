package com.company.matt.jiramobile.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class Provider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    static final int TASK = 100;
    static final int TASK_WITH_ID = 101;
    static final int TASK_WITH_PROJECT = 102;

    private static final SQLiteQueryBuilder sTaskQueryBuilder;

    static{
        sTaskQueryBuilder = new SQLiteQueryBuilder();
        sTaskQueryBuilder.setTables(Contract.TaskEntry.TABLE_NAME);
    }

    private static final String sId =
            Contract.TaskEntry._ID + " = ?";

    private static final String sProject =
            Contract.TaskEntry.COLUMN_PROJECT + " = ?";

    private Cursor getById(
            Uri uri, String[] projection, String sortOrder) {
        String movieId = Contract.TaskEntry.getTaskIdFromUri(uri);

        String selection = sId;
        String[] selectionArgs = new String[]{movieId};

        return sTaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovie(Uri uri, String[] projection, String sortOrder) {
        String movieCategory = Contract.TaskEntry.getTaskProjectFromUri(uri);

        String selection = null;
        String[] selectionArgs = null;

        if(null != movieCategory) {
            selection = sProject;
            selectionArgs = new String[]{movieCategory};
        }

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
        matcher.addURI(authority, Contract.PATH_TASK, TASK);
        matcher.addURI(authority, Contract.PATH_TASK + "/#", TASK_WITH_ID);
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
            case TASK:
                return Contract.TaskEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "movie/*"
            case TASK_WITH_ID: {
                retCursor = getById(uri, projection, sortOrder);
                break;
            }
            // "movie"
            case TASK: {
                retCursor = getMovie(uri, projection, sortOrder);
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
            case TASK: {
                normalizeDate(values);
                long _id = db.insert(Contract.TaskEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.TaskEntry.buildTaskUri(_id);
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
            case TASK:
                rowsDeleted = db.delete(
                        Contract.TaskEntry.TABLE_NAME, selection, selectionArgs);
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
        if (values.containsKey(Contract.TaskEntry.COLUMN_CREATION_DATE)) {
            String dateStr = values.getAsString(Contract.TaskEntry.COLUMN_CREATION_DATE);
            if(!dateStr.isEmpty()) {
                //values.put(Contract.TaskEntry.COLUMN_CREATION_DATE, Contract.normalizeDate(dateStr));
            }
        }
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TASK:
                normalizeDate(values);
                rowsUpdated = db.update(Contract.TaskEntry.TABLE_NAME, values, selection,
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
            case TASK:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(Contract.TaskEntry.TABLE_NAME, null, value);
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