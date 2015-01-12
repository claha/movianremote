package com.claha.showtimeremote;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ShowtimeRemote extends NavigationDrawerActivity {

    private final ShowtimeHTTP showtimeHTTP = new ShowtimeHTTP();

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
        List<String> drawerItems = new ArrayList<>();
        drawerItems.add("Home");
        drawerItems.add("Navigation");
        drawerItems.add("Media");
        drawerItems.add("Settings");
        drawerItems.add("About");
        return drawerItems;
    }

    private boolean showOptionsMenu;

    @Override
    protected Fragment createFragment(String title) {
        Fragment fragment = null;

        showOptionsMenu = false;

        switch (title) {
            case "Home":
                fragment = new HomeFragment();
                break;
            case "Navigation":
                showOptionsMenu = true;
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

        return fragment;
    }

    @Override
    protected int getMenuResourceID() {
        return R.menu.menu_showtime_remote;
    }

    @Override
    protected int getContentResourceID() {
        return R.id.content;
    }

    @Override
    protected int getAppName() {
        return R.string.app_name;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        showtimeHTTP.setIpAddress(sharedPreferences.getString("ipAddress", null));
        showtimeHTTP.setPort(sharedPreferences.getString("port", null));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showtimeHTTP.search(query);
                getSupportActionBar().collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return showOptionsMenu;
    }

}
