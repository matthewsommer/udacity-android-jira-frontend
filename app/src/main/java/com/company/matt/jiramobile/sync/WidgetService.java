package com.company.matt.jiramobile.sync;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.company.matt.jiramobile.R;
import com.company.matt.jiramobile.data.Contract;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;
            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(Contract.IssueEntry.CONTENT_URI,null,null,null,null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.list_item_task);
                String summary = data.getString(data.getColumnIndex(Contract.IssueEntry.COLUMN_SUMMARY));
                String strID = data.getString(data.getColumnIndex(Contract.IssueEntry._ID));
                views.setTextViewText(R.id.list_item_summary, summary);

                final Intent intent = new Intent();
                Uri issueUri = Contract.IssueEntry.buildIssueUri(Long.valueOf(strID));
                intent.setData(issueUri);
                views.setOnClickFillInIntent(R.id.list_item_task, intent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(data.getColumnIndexOrThrow(Contract.IssueEntry._ID));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}