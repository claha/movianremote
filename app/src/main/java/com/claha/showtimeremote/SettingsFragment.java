package com.claha.showtimeremote;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private final static String PACKAGE_NAME = "com.claha.showtimeremote";

    private PreferenceScreen screen;

    private String getKey(int id) {
        return getResources().getString(id);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        // Screen
        screen = getPreferenceScreen();

        //setupProfiles();

        // Developer
        final PreferenceCategory developer = (PreferenceCategory) screen.findPreference(getKey(R.string.settings_developer_key));
        //screen.removePreference(developer);

        // About
        PreferenceCategory about = (PreferenceCategory) screen.findPreference(getKey(R.string.settings_about_key));

        Preference aboutVersion = screen.findPreference(getKey(R.string.settings_about_version_key));
        aboutVersion.setOnPreferenceClickListener(new OnPreferenceMultipleClickListener() {
            @Override
            protected void onPreferenceMultipleClick() {
                screen.addPreference(developer);
            }
        });


        Preference aboutRate = about.findPreference(getKey(R.string.settings_about_rate_key));
        aboutRate.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
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

    /*@Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String name = (String) newValue;

        if (preference.getKey().equals(getKey(R.string.settings_profiles_select_key))) {
            settings.setCurrentProfile(settings.getProfiles().getByName(name));
            return true;
        }

        return false;
    }*/

    /*@Override
    public void onResume() {
        super.onResume();
        setupProfiles();
    }*/

    /*
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
            profilesSelect.setValueIndex(settings.getProfiles().indexOf(settings.getCurrentProfile()));
        } else {
            profilesSelect.setEnabled(false);
        }
    }*/

    //private OnNestedPreferenceClickListener onNestedPreferenceClickListener;

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onNestedPreferenceClickListener = (OnNestedPreferenceClickListener) context.getApplicationContext();
    }*/

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(getKey(R.string.settings_about_rate_key))) {
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
        }
        return true;
        /*else {
            onNestedPreferenceClickListener.onClick(preference.getKey());
            return true;
        }*/
    }

    /*public interface OnNestedPreferenceClickListener {
        void onClick(String key);
    }*/

}
