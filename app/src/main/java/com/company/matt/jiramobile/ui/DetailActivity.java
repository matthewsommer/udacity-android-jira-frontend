package com.company.matt.jiramobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.company.matt.jiramobile.R;

public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle moviearguments = new Bundle();
            moviearguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment df = new DetailFragment();
            df.setArguments(moviearguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, df)
                    .commit();

            Bundle vfArguments = new Bundle();
            vfArguments.putString(AttachmentFragment.TASK_ID, getIntent().getStringExtra(AttachmentFragment.TASK_ID));

            AttachmentFragment vf = new AttachmentFragment();
            vf.setArguments(vfArguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.videos_container, vf)
                    .commit();

            CommentFragment rf = new CommentFragment();
            rf.setArguments(vfArguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.reviews_container, rf)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}