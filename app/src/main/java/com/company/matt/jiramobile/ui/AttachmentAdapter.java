package com.company.matt.jiramobile.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.company.matt.jiramobile.JIRA.Attachment;
import com.company.matt.jiramobile.R;

import java.util.List;

public class AttachmentAdapter extends ArrayAdapter<Attachment> {
    private static final String LOG_TAG = AttachmentAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param attachments A List of video objects to display in a list
     */
    public AttachmentAdapter(Activity context, List<Attachment> attachments) {
        super(context, 0, attachments);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attachment attachment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_attachment, parent, false);
            ((TextView) convertView.findViewById(R.id.list_item_video)).setText(attachment.getName());
        }

        return convertView;
    }
}