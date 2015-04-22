package com.claha.showtimeremote;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.claha.showtimeremote.base.BaseActivity;

public class SettingsScreen extends BaseActivity implements SettingsFragment.OnNestedPreferenceClickListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }

    @Override
    protected int toolbarResource() {
        return R.id.toolbar;
    }

    @Override
    protected int activityLayoutResource() {
        return R.layout.activity_settings_screen;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            fragmentManager.popBackStack();
            getSupportActionBar().setTitle("Settings");
        }
    }

    @Override
    public void onClick(String key) {
        if (key.equals(getResources().getString(R.string.settings_profiles_manage_key))) {
            fragmentManager.beginTransaction().replace(R.id.content, new SettingsProfilesFragment()).addToBackStack(null).commit();
            getSupportActionBar().setTitle("Manage Profiles");
        }
    }




}
