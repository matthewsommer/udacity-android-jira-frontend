package com.company.matt.jiramobile.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import com.company.matt.jiramobile.data.Contract.IssueEntry;
import com.company.matt.jiramobile.data.Contract.CommentEntry;
import com.company.matt.jiramobile.data.Contract.AttachmentEntry;

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
        tableNameHashSet.add(Contract.IssueEntry.TABLE_NAME);

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

        assertTrue("Error: Your database was created without issue entry tables",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + Contract.IssueEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> columnHashSet = new HashSet<String>();
        columnHashSet.add(IssueEntry._ID);
        columnHashSet.add(IssueEntry.COLUMN_REMOTE_ID);
        columnHashSet.add(IssueEntry.COLUMN_SUMMARY);
        columnHashSet.add(IssueEntry.COLUMN_PRIORITY);
        columnHashSet.add(IssueEntry.COLUMN_STATUS);
        columnHashSet.add(IssueEntry.COLUMN_REPORTER);
        columnHashSet.add(IssueEntry.COLUMN_DESCRIPTION);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required entry columns",
                columnHashSet.isEmpty());
        db.close();
    }

    public void testIssuesTable() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createIssueValues();

        long issueRowId = db.insert(Contract.IssueEntry.TABLE_NAME, null, contentValues);
        assertTrue(issueRowId != -1);

        Cursor issueCursor = db.query(
                Contract.IssueEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        assertTrue( "Error: No Records returned from location query", issueCursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("testInsertReadDb issueEntry failed to validate",
                issueCursor, contentValues);

        assertFalse( "Error: More than one record returned from issue query",
                issueCursor.moveToNext() );

        issueCursor.close();
        dbHelper.close();
    }

    public void testCommentsTable() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createCommentValues();

        long issueRowId = db.insert(CommentEntry.TABLE_NAME, null, contentValues);
        assertTrue(issueRowId != -1);

        Cursor commentCursor = db.query(
                CommentEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        assertTrue( "Error: No Records returned from location query", commentCursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("testInsertReadDb failed to validate",
                commentCursor, contentValues);

        assertFalse( "Error: More than one record returned from query",
                commentCursor.moveToNext() );

        commentCursor.close();
        dbHelper.close();
    }

    public void testAttachmentsTable() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createAttachmentValues();

        long issueRowId = db.insert(AttachmentEntry.TABLE_NAME, null, contentValues);
        assertTrue(issueRowId != -1);

        Cursor attachmentCursor = db.query(
                AttachmentEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        assertTrue( "Error: No Records returned from location query", attachmentCursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("testInsertReadDb failed to validate",
                attachmentCursor, contentValues);

        assertFalse( "Error: More than one record returned from query",
                attachmentCursor.moveToNext() );

        attachmentCursor.close();
        dbHelper.close();
    }
}