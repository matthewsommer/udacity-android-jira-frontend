package com.company.matt.jiramobile.JIRA;

import android.test.AndroidTestCase;

import java.util.List;

public class TestIssueProviderJIRA extends AndroidTestCase {
    public static final String LOG_TAG = TestIssueProviderJIRA.class.getSimpleName();
    IssueProvider issueProvider = new IssueProviderJIRA();

    public void testGetAll() {
        List<Issue> issues = issueProvider.getAll();
        assertNotNull("Error: buildIssueUri returned null", issues);
        assertTrue("issueProvider.getAll() returned zero issues", issues.size() > 0);
        assertEquals(20,issues.size());
    }

    public void testCreateAndDelete() {
        //Health project and Task issue type
        Issuetype issuetype = new Issuetype("3","Task");
        Project project = new Project("10400","Health");
        Fields fields = new Fields("Unit Test Summary",project,issuetype);
        Issue local_issue = new Issue(fields);
        assertTrue("Issue JIRA Id is not zero", local_issue.getJira_id() == 0);

        Issue createdIssue = issueProvider.create(local_issue);
        assertTrue("Issue JIRA Id is zero after creation", createdIssue.getJira_id() != 0);

        Issue getIssue = issueProvider.get(createdIssue.getJira_id());
        assertNotNull(getIssue);
        assertEquals("Unit Test Summary", getIssue.getFields().getSummary());
        assertEquals("Health", getIssue.getFields().getProject().getName());

        getIssue.getFields().setSummary("Updated Unit Test Summary");

        Issue updatedIssue = issueProvider.update(getIssue);
        assertEquals(getIssue.getFields().getSummary(), updatedIssue.getFields().getSummary());
        assertTrue(issueProvider.delete(createdIssue.getJira_id()));
    }
}