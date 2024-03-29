package com.company.matt.jiramobile.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.matt.jiramobile.JIRA.Fields;
import com.company.matt.jiramobile.JIRA.Issue;
import com.company.matt.jiramobile.JIRA.IssueProvider;
import com.company.matt.jiramobile.JIRA.IssueProviderJIRA;
import com.company.matt.jiramobile.JIRA.Issuetype;
import com.company.matt.jiramobile.JIRA.Project;
import com.company.matt.jiramobile.R;
import com.company.matt.jiramobile.data.Contract;
import com.company.matt.jiramobile.networking.Client;
import com.company.matt.jiramobile.sync.SyncAdapter;

import com.google.android.gms.analytics.Tracker;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements TaskFragment.Callback {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String ATTACHMENTSFRAGMENT_TAG = "AFTAG";
    private static final String COMMENTSFRAGMENT_TAG = "CFTAG";
    private boolean mTwoPane;
    private Tracker mTracker;
    Client.Callback mCallback = null;
    private Context mContext;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (findViewById(R.id.issue_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                DetailFragment df = new DetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.issue_detail_container, df, DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(v.getContext());
                final View mView = layoutInflaterAndroid.inflate(R.layout.create_issue_dialog_box, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(v.getContext());
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                mCallback = new Client.Callback(){
                                    @Override
                                    public void onTaskFinished(String response) {
                                        Log.d("test","test");
                                        SyncAdapter.syncImmediately(MainActivity.this);
                                    }
                                };
                                Issuetype issuetype = new Issuetype("3","Task");
                                Project project = new Project("10400","Health");
                                Fields fields = new Fields(userInputDialogEditText.getText().toString(),project,issuetype);
                                Issue local_issue = new Issue(fields);
                                IssueProvider issueProvider = new IssueProviderJIRA();
                                Issue createdIssue = issueProvider.create(local_issue, mCallback);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

        if (isConnected){
            SyncAdapter.initializeSyncAdapter(this);
            SyncAdapter.syncImmediately(this);
        } else{
            Toast.makeText(mContext, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
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
                    .replace(R.id.issue_detail_container, df, DETAILFRAGMENT_TAG)
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