package com.company.matt.jiramobile;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.GridView;

import com.company.matt.jiramobile.JIRA.Constants;
import com.company.matt.jiramobile.JIRA.Attachment;

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

public class AttachmentFragment extends Fragment {

    private AttachmentAdapter mAttachmentAdapter;
    private String mTaskId;
    static final String TASK_ID = "TASK_ID";

    public AttachmentFragment() {
    }

    public interface Callback {
        void playVideo(String videoKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mTaskId = arguments.getString(AttachmentFragment.TASK_ID);
        }

        View rootView = inflater.inflate(R.layout.fragment_attachments, container, false);

        mAttachmentAdapter = new AttachmentAdapter(getActivity(), new ArrayList<Attachment>());

        GridView mGridView = (GridView) rootView.findViewById(R.id.gridview_videos);
        mGridView.setAdapter(mAttachmentAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Attachment attachment = mAttachmentAdapter.getItem(position);
                Activity current_activity = getActivity();
            }
        });

        return rootView;
    }

    private void updateAttachments() {
        FetchAttachmentsTask attachmentsTask = new FetchAttachmentsTask();
        attachmentsTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateAttachments();
    }


    public class FetchAttachmentsTask extends AsyncTask<String, Void, List<Attachment>> {

        private final String LOG_TAG = FetchAttachmentsTask.class.getSimpleName();

        private List<Attachment> getVideoDataFromJson(String jsonStr)
                throws JSONException {

            final String MDB_RESULTS = "results";

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray(MDB_RESULTS);

            List<Attachment> attachments = new ArrayList<Attachment>();

            for(int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                Attachment attachment = new Attachment(jsonObject.getString(Constants.MDB_ID),
                        jsonObject.getString(Constants.MDB_KEY),
                        jsonObject.getString(Constants.MDB_NAME),
                        jsonObject.getString(Constants.MDB_SITE));
                attachments.add(attachment);
            }
            return attachments;
        }

        @Override
        protected List<Attachment> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonResponseStr = null;
            Uri builtUri = null;

            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortValue = preferences.getString("sort", "");

                builtUri = Uri.parse(Constants.MDB_BASE_URL).buildUpon()
                        .appendPath(mTaskId)
                        .appendPath("attachments")
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
        protected void onPostExecute(List<Attachment>  result) {
            if (result != null) {
                mAttachmentAdapter.clear();
                for(Attachment attachment : result) {
                    mAttachmentAdapter.add(attachment);
                }
            }
        }
    }
}