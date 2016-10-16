package com.company.matt.jiramobile.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.matt.jiramobile.R;
import com.company.matt.jiramobile.data.Contract;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            Contract.IssueEntry.COLUMN_SUMMARY,
            Contract.IssueEntry.COLUMN_STATUS,
            Contract.IssueEntry.COLUMN_PRIORITY
    };

    public static final int COL_SUMMARY = 0;
    public static final int COL_STATUS = 1;
    public static final int COL_PRIORITY = 2;

    private TextView mSummaryTextView;
    private TextView mPriorityTextView;
    private TextView mStatusTextView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mSummaryTextView = (TextView) rootView.findViewById(R.id.detail_summary);
        mStatusTextView = (TextView) rootView.findViewById(R.id.detail_status);
        mPriorityTextView = (TextView) rootView.findViewById(R.id.detail_priority);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("onLoadFinished", Integer.toString(data.getCount()));
        if (data != null && data.moveToFirst()) {
            String summary = data.getString(COL_SUMMARY);
            String priority = data.getString(COL_PRIORITY);
            String status = data.getString(COL_STATUS);

            mSummaryTextView.setText(summary);
            mPriorityTextView.setText(getString(R.string.label_priority) + ": " + priority);
            mStatusTextView.setText(getString(R.string.label_status) + ": " + status);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}