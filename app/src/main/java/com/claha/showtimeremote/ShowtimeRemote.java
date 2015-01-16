package com.claha.showtimeremote;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.List;

public class ShowtimeRemote extends NavigationDrawerActivity {

    private static final String TAG = "ShowtimeRemote";
    private final ShowtimeHTTP showtimeHTTP = new ShowtimeHTTP();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.fragment_settings, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setupNotifications();
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
        String[] drawerItems = getResources().getStringArray(R.array.settings_misc_start_page_entries);
        return Arrays.asList(drawerItems);
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
    protected int getStartPage() {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("misc_start_page", "0"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


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

    private void setupNotifications() {
        final boolean notifyCommit = sharedPreferences.getBoolean("notify_commit", false);
        final boolean notifyRelease = sharedPreferences.getBoolean("notify_release", false);

        GitHubHTTP gitHubHTTP = new GitHubHTTP();

        gitHubHTTP.setOnCommitsCountedListener(new GitHubHTTP.OnCommitsCountedListener() {
            @Override
            public void onCounted(int count) {
                int prevCount = sharedPreferences.getInt("notify_commit_count", 0);
                sharedPreferences.edit().putInt("notify_commit_count", count).apply();

                if (prevCount != 0 && notifyCommit && count > prevCount) {
                    Log.d(TAG, "Notify commit");
                    new ShowtimeNotification(getApplicationContext(), "There is a new commit to GitHub").show();
                }
            }
        });

        gitHubHTTP.setOnReleasesCountedListener(new GitHubHTTP.OnReleasesCountedListener() {
            @Override
            public void onCounted(int count) {
                int prevCount = sharedPreferences.getInt("notiy_notify_release_count", 0);
                sharedPreferences.edit().putInt("notiy_notify_release_count", count).apply();

                if (prevCount != 0 && notifyRelease && count > prevCount) {
                    Log.d(TAG, "Notify release");
                    ShowtimeNotification notification = new ShowtimeNotification(getApplicationContext(), "There is a new release on GitHub");
                    notification.setUrl("http://www.github.com/claha/showtimeremote/releases");
                    notification.show();
                }
            }
        });

        gitHubHTTP.run();
    }
}
