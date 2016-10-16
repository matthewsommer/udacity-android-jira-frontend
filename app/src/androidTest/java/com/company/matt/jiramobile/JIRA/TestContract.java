package com.company.matt.jiramobile.JIRA;

import android.net.Uri;
import android.test.AndroidTestCase;

public class TestContract extends AndroidTestCase {
    public static final String LOG_TAG = TestIssueProviderJIRA.class.getSimpleName();
    IssueProvider issueProvider = new IssueProviderJIRA();

    public void testBuildIssueUri() {
        Uri taskUri = Contract.IssueEntry.buildIssueUri(14514);
        assertNotNull("Error: buildIssueUri returned null", taskUri);
        assertEquals("Error: ID not properly appended to the end of the Uri",
                "14514", taskUri.getLastPathSegment());
        assertEquals("Error", taskUri.toString(),
                "https://timetopretend.atlassian.net/rest/api/latest/issue/14514");
    }

    public void testBuildSearchUri() {
        Uri searchUri = Contract.IssueEntry.buildSearchUri();
        assertNotNull("Error: buildSearchUri returned null", searchUri);
        assertEquals("Error", searchUri.toString(),
                "https://timetopretend.atlassian.net/rest/api/latest/search"+
                        "?jql=resolution%20%3D%20Unresolved%20AND%20assignee"+
                        "%20in%20(currentUser())%20ORDER%20BY%20issuetype%20ASC&maxResults=100");
    }
}