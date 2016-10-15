package com.company.matt.jiramobile.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.company.matt.jiramobile.data.Contract.IssueEntry;

public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                Contract.IssueEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                IssueEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Issue table during delete", 0, cursor.getCount());
        cursor.close();
    }
    
    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }
    
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                Provider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: Provider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + Contract.CONTENT_AUTHORITY,
                    providerInfo.authority, Contract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: Provider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(Contract.IssueEntry.CONTENT_URI);
        assertEquals("Error: the IssueEntry CONTENT_URI should return IssueEntry.CONTENT_TYPE",
                Contract.IssueEntry.CONTENT_TYPE, type);
    }

    public void testBasicIssueQuery() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues issueValues = TestUtilities.createIssueValues();

        long issueRowId = db.insert(IssueEntry.TABLE_NAME, null, issueValues);
        assertTrue("Unable to Insert IssueEntry into the Database", issueRowId != -1);

        db.close();

        Cursor issueCursor = mContext.getContentResolver().query(
                Contract.IssueEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicIssueQuery", issueCursor, issueValues);
    }

    public void testBasicIssueWithIDQuery() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieValues = TestUtilities.createIssueValues();

        long movieRowId = db.insert(IssueEntry.TABLE_NAME, null, movieValues);
        assertTrue("Unable to Insert IssueEntry into the Database", movieRowId != -1);

        db.close();

        Cursor movieCursor = mContext.getContentResolver().query(
                IssueEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, movieValues);
    }

    public void testInsertReadProvider() {
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();

        ContentValues weatherValues = TestUtilities.createIssueValues();
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(IssueEntry.CONTENT_URI, true, tco);

        Uri weatherInsertUri = mContext.getContentResolver()
                .insert(IssueEntry.CONTENT_URI, weatherValues);
        assertTrue(weatherInsertUri != null);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(
                IssueEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating WeatherEntry insert.",
                weatherCursor, weatherValues);
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        TestUtilities.TestContentObserver issueObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(IssueEntry.CONTENT_URI, true, issueObserver);

        deleteAllRecordsFromProvider();

        issueObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(issueObserver);
    }
}