package com.company.matt.jiramobile.JIRA;

import android.net.Uri;
import android.util.Log;
import com.company.matt.jiramobile.*;
import org.json.JSONException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class JIRAClient {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    public static List<Task> FetchMovies(String category) {
        String JsonResponseStr = null;
        Uri builtUri = null;

        builtUri = Uri.parse(Constants.MDB_BASE_URL).buildUpon()
                .appendPath(category)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JsonResponseStr = Client.GetResponseStr(url);

        try {
            return JIRAUtility.getMovieDataFromJson(JsonResponseStr, category);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
    public static List<Attachment> FetchMovieVideos(String movieId) {
        String JsonResponseStr = null;
        Uri builtUri = null;

        builtUri = Uri.parse(Constants.MDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("videos")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JsonResponseStr = Client.GetResponseStr(url);

        try {
            return JIRAUtility.getVideoDataFromJson(JsonResponseStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public static List<Comment> FetchMovieReviews(String movieId) {
        String JsonResponseStr = null;
        Uri builtUri = null;

        builtUri = Uri.parse(Constants.MDB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath("comments")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JsonResponseStr = Client.GetResponseStr(url);

        try {
            return JIRAUtility.getReviewDataFromJson(JsonResponseStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}