package com.claha.showtimeremote;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.claha.showtimeremote.core.MovianRemoteSettings;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private final static String PACKAGE_NAME = "com.claha.showtimeremote";

    private PreferenceScreen screen;
    private MovianRemoteSettings settings;

    private String getKey(int id) {
        return getResources().getString(id);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        // Settings
        settings = new MovianRemoteSettings(getActivity());

        // Screen
        screen = getPreferenceScreen();

        setupProfiles();

        // About
        PreferenceCategory about = (PreferenceCategory) screen.findPreference(getKey(R.string.settings_about_key));

        Preference aboutVersion = screen.findPreference(getKey(R.string.settings_about_version_key));
        aboutVersion.setOnPreferenceClickListener(new OnPreferenceMultipleClickListener() {
            @Override
            protected void onPreferenceMultipleClick() {
                Toast.makeText(getActivity(), "Clicked 5 times", Toast.LENGTH_SHORT).show();
            }
        });


        Preference aboutRate = about.findPreference(getKey(R.string.settings_about_rate_key));
        aboutRate.setOnPreferenceClickListener(this);
    }

    private abstract class OnPreferenceMultipleClickListener implements Preference.OnPreferenceClickListener {

        private long time;
        private int count;

        public OnPreferenceMultipleClickListener() {
            count = 0;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            count++;
            if (count == 5) {
                onPreferenceMultipleClick();
                count = 0;
            } else {
                if (System.currentTimeMillis() - time > 500) {
                    count = 1;
                }
                time = System.currentTimeMillis();
            }

            return true;
        }

        protected abstract void onPreferenceMultipleClick();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String name = (String) newValue;

        if (preference.getKey().equals(getKey(R.string.settings_profiles_select_key))) {
            settings.setCurrentProfile(settings.getProfiles().getByName(name));
            return true;
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        //settings = new MovianRemoteSettings(getActivity());
        setupProfiles();
    }

    private void setupProfiles() {

        PreferenceCategory profilesCategory = (PreferenceCategory) screen.findPreference(getKey(R.string.settings_profiles_key));

        ListPreference profilesSelect = (ListPreference) profilesCategory.findPreference(getKey(R.string.settings_profiles_select_key));
        profilesSelect.setOnPreferenceChangeListener(this);

        Preference profilesManage = profilesCategory.findPreference(getKey(R.string.settings_profiles_manage_key));
        profilesManage.setOnPreferenceClickListener(this);

        MovianRemoteSettings.Profiles profiles = settings.getProfiles();
        int N = profiles.size();

        if (N > 0) {
            CharSequence[] entriesAndEntryValues = new CharSequence[N];
            for (int i = 0; i < N; i++) {
                entriesAndEntryValues[i] = "" + profiles.get(i).getName();
            }
            profilesSelect.setEntries(entriesAndEntryValues);
            profilesSelect.setEntryValues(entriesAndEntryValues);
            profilesSelect.setEnabled(true);
            for (String i : settings.getProfiles().toPrettyStringList()) {
                Log.d("DEBUG", i);
            }
            Log.d("DEBUG", settings.getCurrentProfile().toPrettyString());
            profilesSelect.setValueIndex(settings.getProfiles().indexOf(settings.getCurrentProfile()));
        } else {
            profilesSelect.setEnabled(false);
        }
    }

    private OnNestedPreferenceClickListener onNestedPreferenceClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onNestedPreferenceClickListener = (OnNestedPreferenceClickListener) activity;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getKey(R.string.settings_about_rate_key))) {
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
            return true;
        } else {
            onNestedPreferenceClickListener.onClick(preference.getKey());
            return true;
        }
    }

    public interface OnNestedPreferenceClickListener {
        void onClick(String key);
    }

}
