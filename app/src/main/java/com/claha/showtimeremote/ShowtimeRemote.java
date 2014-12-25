package com.claha.showtimeremote;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowtimeRemote extends NavigationDrawerActivity {

    private static final String TAG = "ShowtimeRemote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.fragment_settings, false);
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_showtime_remote;
    }

    @Override
    protected int getToolbarResourceID() {
        return R.id.toolbar;
    }

    @Override
    protected int getDrawerLayoutResourceID() {
        return R.id.drawer;
    }

    @Override
    protected int getDrawerResourceID() {
        return R.id.left_drawer;
    }

    @Override
    protected List<String> getDrawerItems() {
        return new ArrayList(Arrays.asList("Home", "Navigation", "Media", "Settings", "About"));
    }

    @Override
    protected Fragment createFragment(String title) {
        Fragment fragment = null;
        Bundle args = new Bundle();

        switch (title) {
            case "Home":
                fragment = new HomeFragment();
                break;
            case "Navigation":
                fragment = new NavigationFragment();
                break;
            case "Media":
                fragment = new MediaFragment();
                break;
            case "Settings":
                fragment = new SettingsFragment();
                break;
            case "About":
                fragment = new AboutFragment();
                break;
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getMenuResourceID() {
        return R.menu.showtime_remote;
    }

    @Override
    protected int getContentResourceID() {
        return R.id.content;
    }

    @Override
    protected int getAppName() {
        return R.string.app_name;
    }

}
