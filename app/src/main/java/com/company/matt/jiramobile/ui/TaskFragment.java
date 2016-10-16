package com.company.matt.jiramobile.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.company.matt.jiramobile.R;
import com.company.matt.jiramobile.data.Contract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class TaskFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = TaskFragment.class.getSimpleName();
    private TaskAdapter mTaskAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int TASK_LOADER = 0;

    private static final String[] TASK_COLUMNS = {
            Contract.IssueEntry._ID,
            Contract.IssueEntry.COLUMN_REMOTE_ID,
            Contract.IssueEntry.COLUMN_SUMMARY,
            Contract.IssueEntry.COLUMN_PRIORITY
    };

    static final int COL_ID = 0;
    static final int COL_REMOTE_ID = 1;
    static final int COL_SUMMARY = 2;
    static final int COL_PRIORITY = 3;
    static final int COL_PROJECT = 4;

    public interface Callback {
        void selectFirstItem(String id, String movieId);
        void onItemSelected(String id, String movieId);
    }

    public TaskFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTaskAdapter = new TaskAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView = (ListView) rootView.findViewById(R.id.listview_tasks);
        mListView.setAdapter(mTaskAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String id = cursor.getString(COL_ID);
                    String movieId = cursor.getString(COL_REMOTE_ID);
                    ((Callback) getActivity())
                            .onItemSelected(id,movieId);
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TASK_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri tasksUri = Contract.IssueEntry.buildIssueUri();
        String sortOrder = Contract.IssueEntry.COLUMN_SUMMARY + " ASC";

        return new CursorLoader(getActivity(),
                tasksUri,
                TASK_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTaskAdapter.swapCursor(data);
        if (mPosition != GridView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
        else {
            if(data.getCount() > 0 && data.moveToFirst()) {
                final String firstId = data.getString(COL_ID);
                final String firstTaskID = data.getString(COL_REMOTE_ID);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ((Callback) getActivity()).selectFirstItem(firstId,firstTaskID);
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTaskAdapter.swapCursor(null);
    }
}