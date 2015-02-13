package com.claha.showtimeremote;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import com.claha.showtimeremote.base.BaseActivity;
import com.claha.showtimeremote.core.ShowtimeSettings;

public class SettingsScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
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

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

        private final static int PROFILES = 0;
        private final static int PROFILES_CHOOSE = 0;
        private final static int PROFILES_ADD = 1;
        private final static int PROFILES_DELETE = 2;

        private final static int NETWORK = 1;
        private final static int NETWORK_IP_ADDRESS = 0;
        
        private final static int ABOUT = 2;

        private EditTextPreference profilesAdd;
        private ListPreference profilesChoose;
        private ListPreference profilesDelete;

        private EditTextPreference networkIPAddress;

        private Preference aboutVersion;

        private ShowtimeSettings showtimeSettings;

        public SettingsFragment() {
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.fragment_settings);

            // Settings
            showtimeSettings = new ShowtimeSettings(getActivity());

            // Root
            PreferenceScreen root = getPreferenceScreen();

            // Profiles
            PreferenceCategory profiles = (PreferenceCategory) root.getPreference(PROFILES);

            profilesChoose = (ListPreference) profiles.getPreference(PROFILES_CHOOSE);
            profilesDelete = (ListPreference) profiles.getPreference(PROFILES_DELETE);
            profilesAdd = (EditTextPreference) profiles.getPreference(PROFILES_ADD);

            profilesAdd.setOnPreferenceChangeListener(this);
            profilesDelete.setOnPreferenceChangeListener(this);
            profilesChoose.setOnPreferenceChangeListener(this);

            profilesAdd.setOnPreferenceClickListener(this);

            updateProfiles();

            // Network
            PreferenceCategory network = (PreferenceCategory) root.getPreference(NETWORK);
            networkIPAddress = (EditTextPreference) network.getPreference(NETWORK_IP_ADDRESS);

            // About
            PreferenceCategory about = (PreferenceCategory) root.getPreference(ABOUT);
            aboutVersion = about.getPreference(0);
            aboutVersion.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            String name = (String) newValue;
            int numProfiles = showtimeSettings.getNumProfiles();

            // Profile add
            if (preference == profilesAdd) {
                //String name = profilesAdd.getText();
                //String name = profilesAdd.getEditText().getText().toString();
                String ipAddress = networkIPAddress.getText();

                if (name.length() > 0) {

                    showtimeSettings.addProfile(name, ipAddress);

                    updateProfiles();

                    profilesChoose.setValueIndex(numProfiles);
                }

                return true;

                // Profile delete
            } else if (preference == profilesDelete) {
                showtimeSettings.deleteProfile(name);
                updateProfiles();

                ShowtimeSettings.Profile profile = showtimeSettings.getCurrentProfile();

                if (profile != null) {
                    profilesChoose.setValue(profile.getName());
                    networkIPAddress.setText(profile.getIPAddress());
                }

                return true;

                // Profile choose
            } else if (preference == profilesChoose) {
                showtimeSettings.chooseProfile(name);
                networkIPAddress.setText(showtimeSettings.getCurrentProfile().getIPAddress());
                return true;
            }

            return false;
        }

        private void updateProfiles() {
            showtimeSettings.savePreferences();

            int N = showtimeSettings.getNumProfiles();
            ShowtimeSettings.Profiles profiles = showtimeSettings.getProfiles();

            if (N > 0) {
                CharSequence[] entriesAndEntryValues = new CharSequence[N];
                for (int i = 0; i < N; i++) {
                    entriesAndEntryValues[i] = "" + profiles.get(i).getName();
                }
                profilesChoose.setEntries(entriesAndEntryValues);
                profilesDelete.setEntries(entriesAndEntryValues);
                profilesChoose.setEntryValues(entriesAndEntryValues);
                profilesDelete.setEntryValues(entriesAndEntryValues);
                profilesChoose.setEnabled(true);
                profilesDelete.setEnabled(true);
                profilesDelete.setValue(null);
            } else {
                profilesChoose.setEnabled(false);
                profilesDelete.setEnabled(false);
            }
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == profilesAdd) {
                profilesAdd.getEditText().setText("");
                return true;
            }
            return false;
        }
    }
}
