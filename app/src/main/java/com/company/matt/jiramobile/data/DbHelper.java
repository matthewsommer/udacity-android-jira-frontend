package com.company.matt.jiramobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.company.matt.jiramobile.data.Contract.IssueEntry;
import com.company.matt.jiramobile.data.Contract.CommentEntry;
import com.company.matt.jiramobile.data.Contract.AttachmentEntry;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;

    static final String DATABASE_NAME = "jiramobile.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createIssuesTable(sqLiteDatabase);
        createCommentsTable(sqLiteDatabase);
        createAttachmentsTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IssueEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CommentEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AttachmentEntry.TABLE_NAME);
        onCreate(db);
    }

    private void createIssuesTable(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + IssueEntry.TABLE_NAME + " (" +
                IssueEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IssueEntry.COLUMN_REMOTE_ID + " INTEGER NOT NULL UNIQUE," +
                IssueEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                IssueEntry.COLUMN_PRIORITY + " TEXT NOT NULL, " +
                IssueEntry.COLUMN_DESCRIPTION + " TEXT, " +
                IssueEntry.COLUMN_STATUS + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    private void createCommentsTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                CommentEntry.TABLE_NAME + " (" +
                CommentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CommentEntry.COLUMN_JIRA_ISSUE_ID + " INTEGER NOT NULL," +
                CommentEntry.COLUMN_REMOTE_ID + " INTEGER NOT NULL," +
                CommentEntry.COLUMN_BODY + " TEXT NOT NULL," +
                CommentEntry.COLUMN_AUTHOR + " TEXT NOT NULL," +
                CommentEntry.COLUMN_CREATED_DATE + " INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    private void createAttachmentsTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                AttachmentEntry.TABLE_NAME + " (" +
                AttachmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AttachmentEntry.COLUMN_JIRA_ISSUE_ID + " INTEGER NOT NULL," +
                AttachmentEntry.COLUMN_REMOTE_ID + " INTEGER NOT NULL," +
                AttachmentEntry.COLUMN_FILENAME + " TEXT NOT NULL," +
                AttachmentEntry.COLUMN_REMOTE_CONTENT_URL + " TEXT NOT NULL," +
                AttachmentEntry.COLUMN_MIME_TYPE + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }
}