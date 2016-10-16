package com.company.matt.jiramobile.JIRA;

import com.company.matt.jiramobile.networking.Client;

import java.util.List;

public interface IssueProvider {
    public List<Issue> getAll();
    public Issue get(int jira_id);
    public Issue update(Issue issue);
    public boolean delete(int jira_id);
    public Issue create(Issue issue, Client.Callback callback);
    public Issue create(Issue issue);
}