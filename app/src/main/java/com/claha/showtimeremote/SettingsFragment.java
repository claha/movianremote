package com.claha.showtimeremote;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private final static int PROFILES = 0;
    private final static int PROFILES_CHOOSE = 0;
    private final static int PROFILES_ADD = 1;
    private final static int PROFILES_DELETE = 2;

    private final static int NETWORK = 1;
    private final static int NETWORK_IP_ADDRESS = 0;
    private final static int NETWORK_PORT = 1;

    private ShowtimeSettings.Profiles profiles;

    private EditTextPreference profilesAdd;
    private ListPreference profilesChoose;
    private ListPreference profilesDelete;

    private EditTextPreference networkIPAddress;
    private EditTextPreference networkPort;

    private ShowtimeSettings showtimeSettings;

    public SettingsFragment() {
        profiles = new ShowtimeSettings.Profiles();
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

        this.profiles = showtimeSettings.loadProfiles();
        updateProfiles();

        // Network
        PreferenceCategory network = (PreferenceCategory) root.getPreference(NETWORK);

        networkIPAddress = (EditTextPreference) network.getPreference(NETWORK_IP_ADDRESS);
        networkPort = (EditTextPreference) network.getPreference(NETWORK_PORT);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        // Profile add
        if (preference == profilesAdd) {
            String info = profilesAdd.getEditText().getText().toString();
            info += "_" + networkIPAddress.getText();
            info += "_" + networkPort.getText();

            profiles.add(new ShowtimeSettings.Profile(info));
            updateProfiles();

            profilesChoose.setValueIndex(profiles.size() - 1);

            return true;

            // Profile delete
        } else if (preference == profilesDelete) {
            ShowtimeSettings.Profile profile = profiles.getByName((String) newValue);

            if (profile.equals(showtimeSettings.getCurrentProfile())) {
                int index = profiles.indexOf(profile);

                if (index == 0 && profiles.size() > 1) {
                    index = 1;
                } else if (index > 0) {
                    index--;
                }

                if (index >= 0) {
                    profilesChoose.setValueIndex(index);
                }
            }

            profiles.remove(profile);
            updateProfiles();

            return true;

            // Profile choose
        } else if (preference == profilesChoose) {
            updateNetwork();
            return true;
        }

        return false;
    }

    private void updateProfiles() {
        showtimeSettings.saveProfiles(profiles);
        int N = profiles.size();

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

    private void updateNetwork() {
        ShowtimeSettings.Profile profile = showtimeSettings.getCurrentProfile();
        networkIPAddress.setText(profile.getIPAddress());
        networkPort.setText(profile.getPort());
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