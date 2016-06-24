package com.company.matt.jiramobile.JIRA;

public final class Constants  {

    public static final String JIRA_BASE_URL = "https://timetopretend.atlassian.net/rest/api/latest/search?jql=resolution%20%3D%20Unresolved%20AND%20assignee%20in%20(currentUser())%20ORDER%20BY%20issuetype%20ASC&maxResults=1000&fields=id,key,summary,description,priority,status,issuetype,project,created,updated,attachment,issuelinks,customfield_10009,customfield_10904,customfield_10905,customfield_11109,customfield_11103,customfield_10901,customfield_10900,customfield_11301";
    public static final String JIRA_REST_URL = "https://timetopretend.atlassian.net/rest/api/latest";
    public static final String MDB_POPULAR = "popular";
    public static final String MDB_TOP_RATED = "top_rated";
    public static final String MDB_FAVORITE = "favorite";
    public static final String APPID_PARAM = "api_key";
    public static final String MDB_KEY = "key";
    public static final String MDB_SITE = "site";
    public static final String MDB_AUTHOR = "author";
    public static final String MDB_CONTENT = "content";
    public static final String MDB_NAME = "name";

    public static final String JIRA_ISSUES = "issues";
    public static final String JIRA_COMMENTS = "comments";
    public static final String JIRA_ATTACHMENT = "attachments";
    public static final String JIRA_ID = "id";
    public static final String JIRA_FIELDS = "fields";
    public static final String JIRA_SUMMARY = "summary";
    public static final String JIRA_DESCRIPTION = "description";
    public static final String JIRA_CREATED_DATE = "created";
    public static final String JIRA_PRIORITY = "priority";
    public static final String MDB_POSTER_PATH = "poster_path";
    public static final String MDB_VOTE_AVG = "priority";
    public static final String MDB_OVERVIEW = "overview";
    public static final String JIRA_PROJECT = "project";
    public static final String JIRA_NAME = "name";
    public static final String JIRA_DISPLAY_NAME = "displayName";
    public static final String JIRA_AUTHOR = "author";

    private Constants(){
        throw new AssertionError();
    }
}