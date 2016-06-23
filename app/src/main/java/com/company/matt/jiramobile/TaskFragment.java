package com.company.matt.jiramobile;

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
import com.company.matt.jiramobile.data.Contract;

public class TaskFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = TaskFragment.class.getSimpleName();
    private TaskAdapter mTaskAdapter;

    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private static final int TASK_LOADER = 0;

    private static final String[] TASK_COLUMNS = {
            Contract.TaskEntry._ID,
            Contract.TaskEntry.COLUMN_REMOTE_ID,
            Contract.TaskEntry.COLUMN_SUMMARY,
            Contract.TaskEntry.COLUMN_PRIORITY,
            Contract.TaskEntry.COLUMN_PROJECT
    };

    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_POSTER = 3;
    static final int COL_CATEGORY = 4;

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

        mGridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        mGridView.setAdapter(mTaskAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String id = cursor.getString(COL_ID);
                    String movieId = cursor.getString(COL_MOVIE_ID);
                    ((Callback) getActivity())
                            .onItemSelected(id,movieId);
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
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
        Uri tasksUri = Contract.TaskEntry.buildTaskUri();
        String sortOrder = Contract.TaskEntry.COLUMN_SUMMARY + " ASC";

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
            mGridView.smoothScrollToPosition(mPosition);
        }
        else {
            if(data.getCount() > 0 && data.moveToFirst()) {
                final String firstId = data.getString(COL_ID);
                final String firstMovieID = data.getString(COL_MOVIE_ID);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ((Callback) getActivity()).selectFirstItem(firstId,firstMovieID);
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