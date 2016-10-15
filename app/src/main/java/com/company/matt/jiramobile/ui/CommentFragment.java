package com.company.matt.jiramobile.ui;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.company.matt.jiramobile.JIRA.Constants;
import com.company.matt.jiramobile.JIRA.Comment;
import com.company.matt.jiramobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {

    private CommentAdapter mCommentAdapter;
    private String mTaskId;
    static final String TASK_ID = "TASK_ID";

    public CommentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mTaskId = arguments.getString(AttachmentFragment.TASK_ID);
        }
        else {
            return rootView;
        }

        mCommentAdapter = new CommentAdapter(getActivity(), new ArrayList<Comment>());

        GridView mGridView = (GridView) rootView.findViewById(R.id.gridview_reviews);
        mGridView.setAdapter(mCommentAdapter);

        return rootView;
    }

    private void updateReviews() {
        FetchReviewsTask reviewsTask = new FetchReviewsTask();
        reviewsTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateReviews();
    }


    public class FetchReviewsTask extends AsyncTask<String, Void, List<Comment>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private List<Comment> getVideoDataFromJson(String jsonStr)
                throws JSONException {

            final String MDB_RESULTS = "results";

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray(MDB_RESULTS);

            List<Comment> comments = new ArrayList<Comment>();

            for(int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                Comment comment = new Comment(jsonObject.getString(Constants.JIRA_ID),
                        jsonObject.getString(Constants.MDB_AUTHOR),
                        jsonObject.getString(Constants.MDB_CONTENT));
                comments.add(comment);
            }
            return comments;
        }

        @Override
        protected List<Comment> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonResponseStr = null;
            Uri builtUri = null;

            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortValue = preferences.getString("sort", "");

                builtUri = Uri.parse(Constants.JIRA_BASE_URL).buildUpon()
                        .appendPath(mTaskId)
                        .appendPath("comments")
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                JsonResponseStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getVideoDataFromJson(JsonResponseStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Comment>  result) {
            if (result != null) {
                mCommentAdapter.clear();
                for(Comment comment : result) {
                    mCommentAdapter.add(comment);
                }
            }
        }
    }
}