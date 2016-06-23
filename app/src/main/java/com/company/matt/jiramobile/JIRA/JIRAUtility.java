package com.company.matt.jiramobile.JIRA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JIRAUtility {

    public static List<Task> getMovieDataFromJson(String responseStr, String category)
            throws JSONException {
        JSONObject moviesJson = new JSONObject(responseStr);
        JSONArray moviesArray = moviesJson.getJSONArray(Constants.MDB_RESULTS);

        List<Task> movies = new ArrayList<Task>();

        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject jsonObject = moviesArray.getJSONObject(i);

            Task task = new Task(jsonObject.getString(Constants.MDB_ID),
                    jsonObject.getString(Constants.MDB_ORIGINAL_TITLE),
                    jsonObject.getString(Constants.MDB_RELEASE_DATE),
                    jsonObject.getString(Constants.MDB_POSTER_PATH),
                    jsonObject.getString(Constants.MDB_VOTE_AVG),
                    jsonObject.getString(Constants.MDB_OVERVIEW),
                    category);
            movies.add(task);
        }
        return movies;
    }

    public static List<Attachment> getVideoDataFromJson(String responseStr)
            throws JSONException {
        JSONObject videosJson = new JSONObject(responseStr);
        JSONArray array = videosJson.getJSONArray(Constants.MDB_RESULTS);

        List<Attachment> attachments = new ArrayList<Attachment>();

        for(int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            Attachment attachment = new Attachment(jsonObject.getString(Constants.MDB_ID),
                    jsonObject.getString(Constants.MDB_KEY),
                    jsonObject.getString(Constants.MDB_NAME),
                    jsonObject.getString(Constants.MDB_SITE));
            attachments.add(attachment);
        }
        return attachments;
    }

    public static List<Comment> getReviewDataFromJson(String responseStr)
            throws JSONException {
        JSONObject videosJson = new JSONObject(responseStr);
        JSONArray array = videosJson.getJSONArray(Constants.MDB_RESULTS);

        List<Comment> comments = new ArrayList<Comment>();

        for(int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);

            Comment comment = new Comment(jsonObject.getString(Constants.MDB_ID),
                    jsonObject.getString(Constants.MDB_AUTHOR),
                    jsonObject.getString(Constants.MDB_CONTENT));
            comments.add(comment);
        }
        return comments;
    }
}