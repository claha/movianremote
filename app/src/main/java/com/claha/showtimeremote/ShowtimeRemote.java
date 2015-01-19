package com.claha.showtimeremote;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.List;

public class ShowtimeRemote extends NavigationDrawerActivity {

    private ShowtimeHTTP showtimeHTTP;
    private ShowtimeSettings showtimeSettings;

    private boolean showOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.fragment_settings, false);

        showtimeHTTP = new ShowtimeHTTP(getApplicationContext());

        showtimeSettings = new ShowtimeSettings(getApplicationContext());

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
        String[] drawerItems = {"Navigation", "Media", "Settings"};
        return Arrays.asList(drawerItems);
    }

    @Override
    protected Fragment createFragment(String title) {
        Fragment fragment = null;

        showOptionsMenu = false;

        switch (title) {
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
        final boolean notifyCommit = showtimeSettings.getNotifyCommit();
        final boolean notifyRelease = showtimeSettings.getNotifyRelease();

        GitHubHTTP gitHubHTTP = new GitHubHTTP();

        gitHubHTTP.setOnCommitsCountedListener(new GitHubHTTP.OnCommitsCountedListener() {
            @Override
            public void onCounted(int count) {
                int prevCount = showtimeSettings.getCommitCount();
                showtimeSettings.setCommitCount(count);

                if (prevCount != 0 && notifyCommit && count > prevCount) {
                    new ShowtimeNotification(getApplicationContext(), "There is a new commit to GitHub").show();
                }
            }
        });

        gitHubHTTP.setOnReleasesCountedListener(new GitHubHTTP.OnReleasesCountedListener() {
            @Override
            public void onCounted(int count) {
                int prevCount = showtimeSettings.getReleaseCount();
                showtimeSettings.setReleaseCount(count);

                if (prevCount != 0 && notifyRelease && count > prevCount) {
                    ShowtimeNotification notification = new ShowtimeNotification(getApplicationContext(), "There is a new release on GitHub");
                    notification.setUrl("http://www.github.com/claha/showtimeremote/releases");
                    notification.show();
                }
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            gitHubHTTP.run();
        }
    }
}
