package com.claha.showtimeremote;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private final String TAG = "SettingsFragment";

    private final static int PROFILES = 0;
    private final static int PROFILES_CHOOSE = 0;
    private final static int PROFILES_ADD = 1;
    private final static int PROFILES_DELETE = 2;

    private final static int NETWORK = 1;
    private final static int NETWORK_IP_ADDRESS = 0;
    private final static int NETWORK_PORT = 1;


    private List<Profile> profiles;

    private EditTextPreference profilesAdd;
    private ListPreference profilesChoose;
    private ListPreference profilesDelete;

    private EditTextPreference networkIPAddress;
    private EditTextPreference networkPort;

    private SharedPreferences sharedPreferences;

    public SettingsFragment() {
        profiles = new ArrayList<>();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        // Shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

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

        loadProfiles();
        updateProfiles();

        // Network
        PreferenceCategory network = (PreferenceCategory) root.getPreference(NETWORK);

        networkIPAddress = (EditTextPreference) network.getPreference(NETWORK_IP_ADDRESS);
        networkPort = (EditTextPreference) network.getPreference(NETWORK_PORT);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(TAG, "onPreferenceChange");

        // Profile add
        if (preference == profilesAdd) {
            Log.d(TAG, "profiles_add");

            String name = profilesAdd.getEditText().getText().toString();
            profiles.add(new Profile(name, networkIPAddress.getText(), networkPort.getText()));

            saveProfiles();
            updateProfiles();

            profilesChoose.setValueIndex(profiles.size() - 1);

            return true;

            // Profile delete
        } else if (preference == profilesDelete) {
            String name = (String) newValue;
            int index = profiles.indexOf(new Profile(name, "", ""));
            profiles.remove(index);

            saveProfiles();
            updateProfiles();

            return true;

            // Profile choose
        } else if (preference == profilesChoose) {
            String name = ((String) newValue).split("_")[0];
            int index = profiles.indexOf(new Profile(name, "", ""));

            updateNetwork(profiles.get(index));

            return true;
        }

        return false;
    }

    private void updateProfiles() {
        int N = profiles.size();

        if (N > 0) {
            CharSequence[] entriesAndEntryValues = new CharSequence[N];
            for (int i = 0; i < N; i++) {
                entriesAndEntryValues[i] = "" + profiles.get(i).name;
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

    private void updateNetwork(Profile profile) {
        networkIPAddress.setText(profile.ipAddress);
        networkPort.setText(profile.port);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == profilesAdd) {
            profilesAdd.setText("");
            profilesAdd.getEditText().setText("");
            return true;
        }

        return false;
    }

    private void loadProfiles() {
        Log.d(TAG, "saveProfiles");
        Set<String> profilesToLoadTemp = sharedPreferences.getStringSet("profiles", new HashSet<String>());
        List<String> profilesToLoad = new ArrayList<>(profilesToLoadTemp);
        Collections.sort(profilesToLoad);
        Log.d(TAG, "number of profiles: " + profilesToLoad.size());
        profiles = new ArrayList<>();
        for (String profile : profilesToLoad) {
            profiles.add(new Profile(profile));
        }
    }

    private void saveProfiles() {
        Log.d(TAG, "saveProfiles");
        Set<String> profilesToSave = new HashSet<>();
        for (Profile profile : profiles) {
            profilesToSave.add(profile.toString());
        }
        Log.d(TAG, "number of profiles: " + profilesToSave.size());
        sharedPreferences.edit().putStringSet("profiles", profilesToSave).apply();
    }

    private static class Profile {
        final String name;
        final String ipAddress;
        final String port;

        public Profile(String name, String ipAddress, String port) {
            this.name = name;
            this.ipAddress = ipAddress;
            this.port = port;
        }

        public Profile(String profile) {
            String[] info = profile.split("_");
            name = info[0];
            ipAddress = info[1];
            port = info[2];
        }

        @Override
        public String toString() {
            return name + "_" + ipAddress + "_" + port;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Profile)) {
                return false;
            }
            return ((Profile) o).name.equals(name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}