package com.company.matt.jiramobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.company.matt.jiramobile.data.Contract.TaskEntry;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "jiramobile.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + Contract.TaskEntry.TABLE_NAME + " (" +
                Contract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TaskEntry.COLUMN_REMOTE_ID + " INTEGER NOT NULL UNIQUE," +
                Contract.TaskEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                Contract.TaskEntry.COLUMN_CREATION_DATE + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_PRIORITY + " TEXT NOT NULL, " +
                Contract.TaskEntry.COLUMN_DESCRIPTION + " TEXT, " +
                Contract.TaskEntry.COLUMN_PROJECT + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}