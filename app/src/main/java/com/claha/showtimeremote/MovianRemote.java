package com.claha.showtimeremote;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.HapticFeedbackConstants;
import android.view.View;

import com.claha.showtimeremote.base.BaseActivity;
import com.claha.showtimeremote.core.MovianRemoteSettings;
import com.claha.showtimeremote.widget.MovianRemoteButton;

public class MovianRemote extends BaseActivity {

    //private MovianHTTP http;
    private MovianRemoteSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.fragment_settings, false);

        //http = new MovianHTTP(getApplicationContext());
        settings = new MovianRemoteSettings(getApplicationContext());

        //setupAdapters();

        MovianRemoteButton btn = (MovianRemoteButton)findViewById(R.id.btn_misc_settings);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                startActivity(new Intent(getApplicationContext(), SettingsScreen.class));
            }
        });

        configureProgrammableButtons();

    }

    @Override
    protected int activityLayoutResource() {
        return R.layout.activity_movian_remote;
    }

    @Override
    protected void onResume() {
        super.onResume();
        configureProgrammableButtons();
    }

    private void configureProgrammableButtons() {
        MovianRemoteButton red = (MovianRemoteButton)findViewById(R.id.btn_prog_a);
        red.setOnClickAction(settings.getRedProgrammableButtonOnClickAction());
        red.setOnLongClickAction(settings.getRedProgrammableButtonOnLongClickAction());

        MovianRemoteButton green = (MovianRemoteButton)findViewById(R.id.btn_prog_b);
        green.setOnClickAction(settings.getGreenProgrammableButtonOnClickAction());
        green.setOnLongClickAction(settings.getGreenProgrammableButtonOnLongClickAction());

        MovianRemoteButton blue = (MovianRemoteButton)findViewById(R.id.btn_prog_c);
        blue.setOnClickAction(settings.getBlueProgrammableButtonOnClickAction());
        blue.setOnLongClickAction(settings.getBlueProgrammableButtonOnLongClickAction());
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setupSearchView(menu.findItem(R.id.menu_search));
        return true;
    }*/

    /*@Override
    protected int optionsMenuResource() {
        return R.menu.menu_movian_remote;
    }*/

    /*@Override
    protected int toolbarResource() {
        return R.id.toolbar;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    /*private void setupProfileSelector() {
        if (settings.getProfiles().isEmpty()) {
            settings.addProfile(new MovianRemoteSettings.Profile("DEFAULT", "192.168.1.226"));
            settings.setCurrentProfile(settings.getProfiles().get(0));
        }
        if (settings.getCurrentProfile() == null) {
            settings.setCurrentProfile(settings.getProfiles().get(0));
        }

        //viewPagerBottom.setAdapter(new ProfileSelector(viewPagerBottom, settings.getProfiles().toPrettyStringList()));
        //viewPagerBottom.setCurrentItem(settings.getProfiles().indexOf(settings.getCurrentProfile()) + 1); // +1 because it is a circular adapter
    }

    private void setupAdapters() {

        setupProfileSelector();

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
    }*/

}