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

    private static final String ISSUE_SHARE_HASHTAG = " #JIRAMobileApp";

    private ShareActionProvider mShareActionProvider;
    private Uri mUri;
    private String Id;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            Contract.IssueEntry._ID,
            Contract.IssueEntry.COLUMN_REMOTE_ID,
            Contract.IssueEntry.COLUMN_SUMMARY,
            Contract.IssueEntry.COLUMN_STATUS,
            Contract.IssueEntry.COLUMN_PRIORITY,
            Contract.IssueEntry.COLUMN_DESCRIPTION
    };

    public static final int COL_ID = 0;
    public static final int COL_REMOTE_ID = 1;
    public static final int COL_SUMMARY = 2;
    public static final int COL_STATUS = 3;
    public static final int COL_PRIORITY = 4;
    public static final int COL_DESCRIPTION = 5;

    private ImageView mIconView;
    private TextView mSummaryTextView;
    private TextView mCreationDateTextView;
    private TextView mPriorityTextView;
    private TextView mDescriptionTextView;

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
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mSummaryTextView = (TextView) rootView.findViewById(R.id.detail_title_textview);
        mCreationDateTextView = (TextView) rootView.findViewById(R.id.detail_release_date);
        mPriorityTextView = (TextView) rootView.findViewById(R.id.detail_vote_average);
        mDescriptionTextView = (TextView) rootView.findViewById(R.id.detail_synopsis);

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
            Id = data.getString(COL_ID);
            String taskId = data.getString(COL_REMOTE_ID);
            String summary = data.getString(COL_SUMMARY);
            String priority = data.getString(COL_PRIORITY);

            mSummaryTextView.setText(summary);
            mPriorityTextView.setText(priority);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}