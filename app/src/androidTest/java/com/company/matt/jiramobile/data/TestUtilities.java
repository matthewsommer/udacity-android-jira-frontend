package com.company.matt.jiramobile.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.company.matt.jiramobile.utils.PollingCheck;

import com.company.matt.jiramobile.data.Contract.IssueEntry;
import com.company.matt.jiramobile.data.Contract.CommentEntry;
import com.company.matt.jiramobile.data.Contract.AttachmentEntry;

import java.util.Map;
import java.util.Set;

public class TestUtilities extends AndroidTestCase {
    static final String TEST_LOCATION = "90291";
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createIssueValues() {
        ContentValues objectValues = new ContentValues();
        objectValues.put(IssueEntry.COLUMN_REMOTE_ID, 47389);
        objectValues.put(IssueEntry.COLUMN_SUMMARY, "Get stuff done.");
        objectValues.put(IssueEntry.COLUMN_STATUS, "Open");
        objectValues.put(IssueEntry.COLUMN_PRIORITY, "High");
        objectValues.put(IssueEntry.COLUMN_DESCRIPTION, "Implement all the things!");
        return objectValues;
    }

    static ContentValues createCommentValues() {
        ContentValues objectValues = new ContentValues();
        objectValues.put(CommentEntry.COLUMN_REMOTE_ID, 14220);
        objectValues.put(CommentEntry.COLUMN_JIRA_ISSUE_ID, 47389);
        objectValues.put(CommentEntry.COLUMN_AUTHOR, "ironman");
        objectValues.put(CommentEntry.COLUMN_CREATED_DATE, TEST_DATE);
        objectValues.put(CommentEntry.COLUMN_BODY, "Implement all the things!");
        return objectValues;
    }

    static ContentValues createAttachmentValues() {
        ContentValues objectValues = new ContentValues();
        objectValues.put(AttachmentEntry.COLUMN_REMOTE_ID, 11914);
        objectValues.put(AttachmentEntry.COLUMN_FILENAME, "Avatar.jpg");
        objectValues.put(AttachmentEntry.COLUMN_REMOTE_CONTENT_URL, "https://timetopretend.atlassian.net/secure/attachment/11914/Avatar.jpg");
        objectValues.put(AttachmentEntry.COLUMN_JIRA_ISSUE_ID, 47389);
        objectValues.put(AttachmentEntry.COLUMN_MIME_TYPE, "image/jpeg");
        return objectValues;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}

