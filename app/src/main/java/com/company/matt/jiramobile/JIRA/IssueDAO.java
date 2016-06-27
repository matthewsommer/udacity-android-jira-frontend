package com.company.matt.jiramobile.JIRA;

import java.util.List;

public interface IssueDAO {
    public List<Issue> getAll();
    public Issue get(int jira_id);
    public Issue update(Issue issue);
    public boolean delete(int jira_id);
    public Issue create(Issue issue);
}