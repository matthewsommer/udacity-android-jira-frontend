package com.company.matt.jiramobile.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.company.matt.jiramobile.JIRA.Issue;
import com.company.matt.jiramobile.JIRA.IssueProvider;
import com.company.matt.jiramobile.JIRA.IssueProviderJIRA;
import com.company.matt.jiramobile.R;
import com.company.matt.jiramobile.data.Contract;
import java.util.List;
import java.util.Vector;

import com.company.matt.jiramobile.data.Contract.IssueEntry;
import com.company.matt.jiramobile.data.Contract.CommentEntry;
import com.company.matt.jiramobile.data.Contract.AttachmentEntry;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = SyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    private static final String[] NOTIFY_TASK_PROJECTION = new String[] {
            Contract.IssueEntry.COLUMN_REMOTE_ID
    };

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        List<Issue> object_list = null;

        IssueProvider issueProvider = new IssueProviderJIRA();
        object_list = issueProvider.getAll();

        if(object_list != null && object_list.size() > 0) {
            Vector<ContentValues> cVVector = new Vector<ContentValues>(object_list.size());
            for (Issue issue : object_list) {
                ContentValues objectValues = new ContentValues();
                objectValues.put(IssueEntry.COLUMN_REMOTE_ID, issue.getJira_id());
                objectValues.put(IssueEntry.COLUMN_SUMMARY, issue.getFields().getSummary());
                objectValues.put(IssueEntry.COLUMN_PRIORITY, issue.getFields().getPriority().getName());
                objectValues.put(IssueEntry.COLUMN_STATUS, issue.getFields().getStatus().getName());
                objectValues.put(IssueEntry.COLUMN_DESCRIPTION, issue.getFields().getDescription());
                cVVector.add(objectValues);
            }

            AddVectorToDB(cVVector);
        }
        return;
    }

    private void AddVectorToDB(Vector<ContentValues> cVVector) {
        getContext().getContentResolver().delete(IssueEntry.CONTENT_URI,
                null,
                null);

        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContext().getContentResolver().bulkInsert(Contract.IssueEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if ( null == accountManager.getPassword(newAccount) ) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}