package com.claha.showtimeremote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.claha.showtimeremote.adapter.CircularPagerAdapter;
import com.claha.showtimeremote.base.BaseActivity;
import com.claha.showtimeremote.base.BaseFragment;
import com.claha.showtimeremote.base.BaseFragmentPagerAdapter;
import com.claha.showtimeremote.base.BaseViewPagerIndicator;
import com.claha.showtimeremote.core.MovianHTTP;
import com.claha.showtimeremote.core.MovianRemoteSettings;

import java.util.ArrayList;
import java.util.List;

public class MovianRemote extends BaseActivity {

    private ViewPager viewPagerBottom;
    private ViewPager viewPagerMain;
    private BaseViewPagerIndicator viewPagerIndicator;

    private MovianHTTP http;
    private MovianRemoteSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.fragment_settings, false);

        viewPagerBottom = (ViewPager) findViewById(R.id.viewPagerBottom);
        viewPagerMain = (ViewPager) findViewById(R.id.viewPagerMain);
        viewPagerIndicator = (BaseViewPagerIndicator) findViewById(R.id.viewPagerIndicator);

        http = new MovianHTTP(getApplicationContext());
        settings = new MovianRemoteSettings(getApplicationContext());

        setupAdapters();

    }

    @Override
    protected int activityLayoutResource() {
        return R.layout.activity_movian_remote;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupProfileSelector();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setupSearchView(menu.findItem(R.id.menu_search));
        return true;
    }

    @Override
    protected int optionsMenuResource() {
        return R.menu.menu_movian_remote;
    }

    @Override
    protected int toolbarResource() {
        return R.id.toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupProfileSelector() {
        if (settings.getProfiles().isEmpty()) {
            settings.addProfile(new MovianRemoteSettings.Profile("DEFAULT", "192.168.0.0"));
            settings.setCurrentProfile(settings.getProfiles().get(0));
        }
        if (settings.getCurrentProfile() == null) {
            settings.setCurrentProfile(settings.getProfiles().get(0));
        }

        viewPagerBottom.setAdapter(new ProfileSelector(viewPagerBottom, settings.getProfiles().toPrettyStringList()));
        viewPagerBottom.setCurrentItem(settings.getProfiles().indexOf(settings.getCurrentProfile()) + 1); // +1 because it is a circular adapter
    }

    private void setupAdapters() {

        setupProfileSelector();

        //
        List<Class<? extends BaseFragment>> fragments = new ArrayList<>();
        fragments.add(NavigationFragment.class);
        fragments.add(MediaFragment.class);
        viewPagerMain.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments));

        viewPagerIndicator.setViewPager(viewPagerMain);
    }

    private void setupSearchView(MenuItem searchItem) {
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                http.search(query);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.collapseActionView();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public static class NavigationFragment extends BaseFragment {

        @Override
        protected int getFragmentLayoutResource() {
            return R.layout.fragment_navigation;
        }
    }

    public static class MediaFragment extends BaseFragment {

        @Override
        protected int getFragmentLayoutResource() {
            return R.layout.fragment_media;
        }
    }

    private class ProfileSelector extends CircularPagerAdapter<String> {

        public ProfileSelector(ViewPager viewPager, List<String> data) {
            super(viewPager, data);
        }

        @Override
        protected View instantiateView(Context context, String item) {
            TextView view = new TextView(context);
            view.setText(item);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(0xFFFFFFFF);
            view.setTypeface(Typeface.DEFAULT_BOLD);
            return view;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            position = getOriginalPosition(position);
            settings.setCurrentProfile(settings.getProfiles().get(position));
        }
    }
}