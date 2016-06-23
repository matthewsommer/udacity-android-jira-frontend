package com.company.matt.jiramobile.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {
    private static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    private static final Uri TEST_MOVIE_DIR = Contract.TaskEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = Provider.buildUriMatcher();

        assertEquals("Error: The WEATHER URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), Provider.TASK);
    }
}