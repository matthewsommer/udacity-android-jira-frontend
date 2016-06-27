package com.company.matt.jiramobile.JIRA;

import android.test.AndroidTestCase;

import org.json.JSONObject;

import java.util.List;

public class TestIssueDAOImpl extends AndroidTestCase {
    public static final String LOG_TAG = TestIssueDAOImpl.class.getSimpleName();
    IssueDAO issueDAO = new IssueDAOImpl();

    public void testGetAll() {
        List<Issue> issues = issueDAO.getAll();
        assertTrue("issueDAO.getAll() returned zero issues", issues.size() > 0);
    }

    public void testCreateAndDelete() {
        //Health project and Task issue type
        Issue local_issue = new Issue("Unit Test Summary", "10400","3");
        assertTrue("Issue JIRA Id is not zero", local_issue.getJira_id() == 0);

        Issue createdIssue = issueDAO.create(local_issue);
        assertTrue("Issue JIRA Id is zero after creation", createdIssue.getJira_id() != 0);

        Issue getIssue = issueDAO.get(createdIssue.getJira_id());
        assertNotNull(getIssue);
        assertEquals("Unit Test Summary", getIssue.getSummary());
        assertEquals("Health", getIssue.getProject());

        getIssue.setSummary("Updated Unit Test Summary");

        Issue updatedIssue = issueDAO.update(getIssue);
        assertEquals(getIssue.getSummary(), updatedIssue.getSummary());
        assertTrue(issueDAO.delete(createdIssue.getJira_id()));
    }
}