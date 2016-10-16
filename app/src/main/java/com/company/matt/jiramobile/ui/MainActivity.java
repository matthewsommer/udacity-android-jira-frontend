package com.company.matt.jiramobile.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.company.matt.jiramobile.R;
import com.company.matt.jiramobile.data.Contract;
import com.company.matt.jiramobile.sync.SyncAdapter;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements TaskFragment.Callback  {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String ATTACHMENTSFRAGMENT_TAG = "AFTAG";
    private static final String COMMENTSFRAGMENT_TAG = "CFTAG";
    private boolean mTwoPane;
    private Uri firstItemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                DetailFragment df = new DetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, df, DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        SyncAdapter.initializeSyncAdapter(this);
        SyncAdapter.syncImmediately(this);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG,Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortValue = sharedPref.getString("sort", "");
        String[] values = getResources().getStringArray(R.array.pref_sort_order_values);
        String[] options = getResources().getStringArray(R.array.pref_sort_order_options);
        int index = Arrays.asList(values).indexOf(sortValue);
        if(index != -1){
            setTitle(options[index]);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void selectFirstItem(String id, String movieId) {
        if(mTwoPane) {
            onItemSelected(id,movieId);
        }
    }

    @Override public void onItemSelected(String id, String movieId) {
        Uri contentUri = Contract.IssueEntry.buildIssueUri(Long.parseLong(id));
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            DetailFragment df = new DetailFragment();
            df.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, df, DETAILFRAGMENT_TAG)
                    .commit();

            Bundle vfArguments = new Bundle();
            vfArguments.putString(AttachmentFragment.TASK_ID, movieId);

            AttachmentFragment vf = new AttachmentFragment();
            vf.setArguments(vfArguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.videos_container, vf, ATTACHMENTSFRAGMENT_TAG)
                    .commit();

            CommentFragment rf = new CommentFragment();
            rf.setArguments(vfArguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.reviews_container, rf, COMMENTSFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intnt = new Intent(this, DetailActivity.class).setData(contentUri);
            intnt.putExtra(AttachmentFragment.TASK_ID, movieId);
            startActivity(intnt);
        }
    }
}