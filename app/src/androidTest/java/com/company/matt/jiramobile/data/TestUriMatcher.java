package com.company.matt.jiramobile.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.company.matt.jiramobile.data.Contract.IssueEntry;
import com.company.matt.jiramobile.data.Contract.CommentEntry;
import com.company.matt.jiramobile.data.Contract.AttachmentEntry;

public class TestUriMatcher extends AndroidTestCase {
    private static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    private static final Uri TEST_ISSUE_DIR = IssueEntry.CONTENT_URI;
    private static final Uri TEST_COMMENT_DIR = CommentEntry.CONTENT_URI;
    private static final Uri TEST_ISSUE_COMMENTS_DIR = IssueEntry.buildIssueCommentsUri(47389);
    private static final Uri TEST_ISSUE_ATTACHMENTS_DIR = IssueEntry.buildIssueAttachmentsUri(47389);
    private static final Uri TEST_ATTACHMENT_DIR = AttachmentEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = Provider.buildUriMatcher();

        assertEquals("Error: The ISSUE URI was matched incorrectly.",
                testMatcher.match(TEST_ISSUE_DIR), Provider.ISSUE);
        assertEquals("Error: The COMMENT URI was matched incorrectly.",
                testMatcher.match(TEST_COMMENT_DIR), Provider.COMMENT);
        assertEquals("Error: The ATTACHMENT URI was matched incorrectly.",
                testMatcher.match(TEST_ATTACHMENT_DIR), Provider.ATTACHMENT);

        assertEquals("Error: The COMMENTS URI was matched incorrectly.",
                testMatcher.match(TEST_ISSUE_COMMENTS_DIR), Provider.COMMENTS_WITH_ISSUE_ID);

        assertEquals("Error: The COMMENTS URI was matched incorrectly.",
                testMatcher.match(TEST_ISSUE_ATTACHMENTS_DIR), Provider.ATTACHMENTS_WITH_ISSUE_ID);
    }
}