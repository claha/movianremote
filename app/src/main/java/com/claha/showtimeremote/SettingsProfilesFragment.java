package com.claha.showtimeremote;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.claha.showtimeremote.core.MovianRemoteSettings;
import com.claha.showtimeremote.preference.IPAddressPickerPreference;

public class SettingsProfilesFragment extends PreferenceFragment {

    private MovianRemoteSettings settings;
    private Context context;
    private PreferenceScreen screen;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new MovianRemoteSettings(getActivity());
        context = getActivity();
        initPreferenceScreen();
    }

    private void initPreferenceScreen() {

        // Preference Screen
        screen = getPreferenceManager().createPreferenceScreen(context);

        // Create new profile
        Preference add = new Preference(context);
        add.setTitle("Add new profile");
        add.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MovianRemoteSettings.Profile profile = new MovianRemoteSettings.Profile("NEW PROFILE", "192.168.0.0");
                settings.addProfile(profile);
                addProfile(profile);
                return true;
            }
        });
        screen.addPreference(add);

        // Add profiles
        for (final MovianRemoteSettings.Profile profile : settings.getProfiles()) {
            addProfile(profile);
        }

        setPreferenceScreen(screen);

    }

    private void addProfile(MovianRemoteSettings.Profile profile) {
        PreferenceCategory category = createCategory(profile);
        screen.addPreference(category);

        EditTextPreference name = createNamePreference(profile, category);
        category.addPreference(name);

        Preference ipAddress = createIPAddressPreference(profile, category);
        category.addPreference(ipAddress);

        Preference delete = createDeletePreference(profile, category);
        category.addPreference(delete);
    }

    private PreferenceCategory createCategory(MovianRemoteSettings.Profile profile) {
        PreferenceCategory category = new PreferenceCategory(context);
        category.setTitle(profile.toPrettyString());
        return category;
    }

    private EditTextPreference createNamePreference(final MovianRemoteSettings.Profile profile, final PreferenceCategory category) {
        final EditTextPreference name = new EditTextPreference(context);
        name.setTitle("Name");
        name.setSummary("Change name of this profile");
        name.setText(profile.getName());
        name.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newName = (String) newValue;

                MovianRemoteSettings.Profiles profiles = settings.getProfiles();
                profiles.getByName(profile.getName()).setName(newName);

                MovianRemoteSettings.Profile currentProfile = settings.getCurrentProfile();

                settings.setProfiles(profiles);

                if (currentProfile.getName().equals(profile.getName())) {
                    settings.setCurrentProfile(settings.getProfiles().getByName(newName));
                }

                profile.setName(newName);
                category.setTitle(profile.toPrettyString());
                return true;
            }
        });

        return name;
    }

    private IPAddressPickerPreference createIPAddressPreference(final MovianRemoteSettings.Profile profile, final PreferenceCategory category) {
        final IPAddressPickerPreference ipAddress = new IPAddressPickerPreference(context, null);
        ipAddress.setTitle("IP-Address");
        ipAddress.setSummary("Change ip-address of this profile");
        ipAddress.setIPAddress(profile.getIPAddress());
        ipAddress.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String newIPAddress = (String) newValue;

                MovianRemoteSettings.Profiles profiles = settings.getProfiles();
                profiles.getByName(profile.getName()).setIpAddress(newIPAddress);
                settings.setProfiles(profiles);

                profile.setIpAddress(newIPAddress);
                category.setTitle(profile.toPrettyString());
                return true;
            }
        });

        return ipAddress;
    }

    private Preference createDeletePreference(final MovianRemoteSettings.Profile profile, final PreferenceCategory category) {
        Preference delete = new Preference(context);
        delete.setTitle("Delete");
        delete.setSummary("Delete this profile");
        delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                settings.deleteProfile(profile);
                screen.removePreference(category);
                return true;
            }
        });

        return delete;
    }

}
