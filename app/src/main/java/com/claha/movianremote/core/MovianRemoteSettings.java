package com.claha.movianremote.core;

import android.content.Context;
import android.util.Log;

import com.claha.movianremote.R;
import com.claha.movianremote.base.BaseSettings;

import java.util.ArrayList;
import java.util.List;

public class MovianRemoteSettings extends BaseSettings {

    public final String PORT = "42000";

    private Profile currentProfile;
    private Profiles profiles;

    public MovianRemoteSettings(Context context) {
        super(context);
        loadPreferences();
    }

    private void loadPreferences() {
        loadProfiles();
        loadCurrentProfile();
    }

    public void savePreferences() {
        saveProfiles();
    }

    private void loadProfiles() {
        String name, ipAddress;
        profiles = new Profiles();
        for (String profile : getStringList(R.string.settings_profiles_key)) {
            name = profile.split("_")[0];
            ipAddress = profile.split("_")[1];
            profiles.add(new Profile(name, ipAddress));
        }
    }

    private void loadCurrentProfile() {
        currentProfile = profiles.getByName(getString(R.string.settings_profiles_choose_key));
        Log.d("ShowtimeDebug", "loadCurrentProfile: " + currentProfile);
    }

    private void saveProfile() {
        putString(R.string.settings_profiles_choose_key, currentProfile.getName());
    }

    private void saveProfiles() {
        putStringList(R.string.settings_profiles_key, profiles.toStringList());
    }

    public Profiles getProfiles() {
        return profiles;
    }

    public int getNumProfiles() {
        return profiles.size();
    }

    public void addProfile(String name, String ipAddress) {
        Log.d("ShowtimeDebug", "addProfile: " + name + " " + ipAddress);
        Profile profileToAdd = new Profile(name, ipAddress);
        profiles.add(profileToAdd);
        chooseProfile(profileToAdd);
    }

    public void chooseProfile(Profile profile) {
        Log.d("ShowtimeDebug", "chooseProfile: " + profile);
        currentProfile = profile;
        saveProfile();
        setIPAddress(profile.getIPAddress());
    }

    public void chooseProfile(String name) {
        chooseProfile(profiles.getByName(name));
    }

    public void deleteProfile(String name) {
        Log.d("ShowtimeDebug", "deleteProfile: " + name);
        Profile profileToDelete = profiles.getByName(name);

        if (profileToDelete.equals(currentProfile)) {
            int index = profiles.indexOf(currentProfile);

            if (index == 0 && profiles.size() > 1) {
                index = 1;
            } else if (index > 0) {
                index--;
            }

            if (index >= 0 && profiles.size() > 1) {
                chooseProfile(profiles.get(index));
            } else {
                currentProfile = null;
            }
        }

        profiles.remove(profileToDelete);
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public int getCurrentProfileIndex() {
        Log.d("ShowtimeDebug", "getCurrentProfileIndex: " + currentProfile);
        return profiles.indexOf(currentProfile);
    }

    public String getIPAddress() {
        return getString(R.string.settings_ipAddress_key);
    }

    private void setIPAddress(String ipAddress) {
        Log.d("ShowtimeDebug", "setIPAddress: " + ipAddress);
        putString(R.string.settings_ipAddress_key, ipAddress);
    }

    public static class Profile {

        private final String name;
        private final String ipAddress;

        public Profile(String name, String ipAddress) {
            this.name = name;
            this.ipAddress = ipAddress;
        }

        public String getName() {
            return name;
        }

        public String getIPAddress() {
            return ipAddress;
        }

        public String toPrettyString() {
            return name.toUpperCase() + " (" + ipAddress + ")";
        }

        @Override
        public String toString() {
            return name + "_" + ipAddress;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Profile && ((Profile) o).name.equals(name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    public static class Profiles extends ArrayList<Profile> {

        public Profile getByName(String name) {
            int index = indexOf(new Profile(name, ""));
            if (index < 0) {
                return null;
            }
            return get(index);
        }

        public List<String> toStringList() {
            List<String> profiles = new ArrayList<>();
            for (int i = 0; i < size(); i++) {
                profiles.add(get(i).toString());
            }
            return profiles;
        }

        public List<String> toPrettyStringList() {
            List<String> profiles = new ArrayList<>();
            for (int i = 0; i < size(); i++) {
                profiles.add(get(i).toPrettyString());
            }
            return profiles;
        }
    }
}
