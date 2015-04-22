package com.claha.showtimeremote.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int optionsMenuResource = optionsMenuResource();
        if (optionsMenuResource != -1) {
            getMenuInflater().inflate(optionsMenuResource(), menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    protected int toolbarResource() {
        return -1;
    }

    protected int optionsMenuResource() {
        return -1;
    }

    protected abstract int activityLayoutResource();
}
