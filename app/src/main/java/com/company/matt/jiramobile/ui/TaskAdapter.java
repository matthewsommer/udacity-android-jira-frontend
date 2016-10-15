package com.company.matt.jiramobile.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.matt.jiramobile.R;

public class TaskAdapter extends CursorAdapter {
    private static final String LOG_TAG = TaskAdapter.class.getSimpleName();

    public static class ViewHolder {
        public final TextView summaryTextView;

        public ViewHolder(View view) {
            summaryTextView = (TextView) view.findViewById(R.id.list_item_summary);
        }
    }

    public TaskAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.list_item_task;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.summaryTextView.setText(cursor.getString(TaskFragment.COL_SUMMARY));
    }
}