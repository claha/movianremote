package com.claha.showtimeremote.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int activityLayoutResource = activityLayoutResource();
        setContentView(activityLayoutResource);

        int toolbarResource = toolbarResource();
        if (toolbarResource != -1) {
            Toolbar toolbar = (Toolbar) findViewById(toolbarResource);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }
    }

    protected int toolbarResource() {
        return -1;
    }

    protected abstract int activityLayoutResource();
}
