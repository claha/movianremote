package com.claha.showtimeremote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.claha.showtimeremote.adapter.CircularPagerAdapter;
import com.claha.showtimeremote.base.BaseActivity;
import com.claha.showtimeremote.base.BaseFragment;
import com.claha.showtimeremote.core.GitHubHTTP;
import com.claha.showtimeremote.core.ShowtimeHTTP;
import com.claha.showtimeremote.core.ShowtimeNotification;
import com.claha.showtimeremote.core.ShowtimeSettings;

import java.util.List;

public class ShowtimeRemote extends BaseActivity {

    private ViewPager viewPagerBottom;
    private ViewPager viewPagerMain;

    private ShowtimeHTTP showtimeHTTP;
    private ShowtimeSettings showtimeSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_remote);

        viewPagerBottom = (ViewPager) findViewById(R.id.viewPagerBottom);
        viewPagerMain = (ViewPager) findViewById(R.id.viewPagerMain);

        showtimeHTTP = new ShowtimeHTTP(getApplicationContext());
        showtimeSettings = new ShowtimeSettings(getApplicationContext());

        setupAdapters();

        setupNotifications();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_showtime_remote, menu);
        setupSearchView(menu.findItem(R.id.menu_search));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupAdapters() {
        List<String> profiles = showtimeSettings.loadProfiles().getPrettyStringList();
        if (profiles.isEmpty()) {
            profiles.add(showtimeSettings.getIPAddress());
        }

        ProfileAdapter adapter = new ProfileAdapter(viewPagerBottom, profiles);
        viewPagerBottom.setAdapter(adapter);

        int index = showtimeSettings.loadProfiles().indexOf(showtimeSettings.getCurrentProfile()) + 1;
        viewPagerBottom.setCurrentItem(index);

        RemoteFragmentPagerAdapter adapter2 = new RemoteFragmentPagerAdapter(getSupportFragmentManager());
        viewPagerMain.setAdapter(adapter2);
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

    private void setupSearchView(MenuItem searchItem) {
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
    }

    private void showSettings() {
        Intent intent = new Intent(this, SettingsScreen.class);
        startActivity(intent);
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

    private class ProfileAdapter extends CircularPagerAdapter<String> {

        public ProfileAdapter(ViewPager viewPager, List<String> data) {
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
            showtimeSettings.setCurrentProfile(showtimeSettings.loadProfiles().get(position));
        }
    }

    private class RemoteFragmentPagerAdapter extends FragmentPagerAdapter {

        private final int NUM_FRAGMENTS = 2;

        public RemoteFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NavigationFragment();
                case 1:
                    return new MediaFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }
    }


}