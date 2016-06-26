package com.company.matt.jiramobile.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Matt on 6/15/16.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(Contract.TaskEntry.TABLE_NAME);

        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
        SQLiteDatabase db = new DbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        assertTrue("Error: Your database was created without movie entry tables",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + Contract.TaskEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(Contract.TaskEntry._ID);
        movieColumnHashSet.add(Contract.TaskEntry.COLUMN_REMOTE_ID);
        movieColumnHashSet.add(Contract.TaskEntry.COLUMN_SUMMARY);
        movieColumnHashSet.add(Contract.TaskEntry.COLUMN_PRIORITY);
        movieColumnHashSet.add(Contract.TaskEntry.COLUMN_CREATION_DATE);
        movieColumnHashSet.add(Contract.TaskEntry.COLUMN_DESCRIPTION);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }

    public void testMovieTable() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieValues = TestUtilities.createTaskValues();

        long movieRowId = db.insert(Contract.TaskEntry.TABLE_NAME, null, movieValues);
        assertTrue(movieRowId != -1);

        Cursor movieCursor = db.query(
                Contract.TaskEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        assertTrue( "Error: No Records returned from location query", movieCursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("testInsertReadDb movieEntry failed to validate",
                movieCursor, movieValues);

        assertFalse( "Error: More than one record returned from movie query",
                movieCursor.moveToNext() );

        movieCursor.close();
        dbHelper.close();
    }
}