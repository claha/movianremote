package com.claha.showtimeremote;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowtimeSettings extends BaseSettings {

    public ShowtimeSettings(Context context) {
        super(context);
    }

    public Profiles loadProfiles() {
        Profiles profiles = new Profiles();
        for (String profile : getStringList(R.string.settings_profiles_key)) {
            profiles.add(new Profile(profile));
        }
        return profiles;
    }

    public void saveProfiles(Profiles profiles) {
        putStringList(R.string.settings_profiles_key, profiles.getStringList());
        if (!profiles.contains(getCurrentProfile())) {
            putString(R.string.settings_profiles_choose_key, null);
        }
    }

    public Profile getCurrentProfile() {
        Profiles profiles = loadProfiles();
        return profiles.getByName(getString(R.string.settings_profiles_choose_key));
    }

    public void setCurrentProfile(Profile profile) {
        putString(R.string.settings_profiles_choose_key, profile.getName());
        putString(R.string.settings_ipAddress_key, profile.getIPAddress());
        putString(R.string.settings_port_key, profile.getPort());
    }

    public String getIPAddress() {
        return getString(R.string.settings_ipAddress_key);
    }

    public String getPort() {
        return getString(R.string.settings_port_key);
    }

    public boolean getNotifyCommit() {
        return getBoolean(R.string.settings_notify_commit_key);
    }

    public boolean getNotifyRelease() {
        return getBoolean(R.string.settings_notify_release_key);
    }

    public int getCommitCount() {
        return getInt(R.string.settings_notify_commit_count);
    }

    public void setCommitCount(int count) {
        putInt(R.string.settings_notify_commit_count, count);
    }

    public int getReleaseCount() {
        return getInt(R.string.settings_notify_release_count);
    }

    public void setReleaseCount(int count) {
        putInt(R.string.settings_notify_release_count, count);
    }

    public static class Profile {

        private final static int NAME = 0;
        private final static int IP_ADDRESS = 1;
        private final static int PORT = 2;

        private final List<String> info;

        public Profile(List<String> info) {
            this.info = info;
        }

        public Profile(String info) {
            this.info = Arrays.asList(info.split("_"));
        }

        public String getName() {
            return info.get(NAME);
        }

        public String getIPAddress() {
            return info.get(IP_ADDRESS);
        }

        public String getPort() {
            return info.get(PORT);
        }

        public String toPrettyString() {
            return getName().toUpperCase() + " (" + getIPAddress() + ")";
        }

        @Override
        public String toString() {
            return TextUtils.join("_", info);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Profile)) {
                return false;
            }
            return ((Profile) o).info.get(NAME).equals(info.get(NAME));
        }

        @Override
        public int hashCode() {
            return info.hashCode();
        }
    }

    public static class Profiles extends ArrayList<Profile> {

        public Profile getByName(String name) {
            int index = indexOf(new Profile(name + "_ _ "));
            if (index < 0) {
                return null;
            }
            return get(index);
        }

        public List<String> getStringList() {
            List<String> profiles = new ArrayList<>();
            for (int i = 0; i < size(); i++) {
                profiles.add(get(i).toString());
            }
            return profiles;
        }

        public List<String> getPrettyStringList() {
            List<String> profiles = new ArrayList<>();
            for (int i = 0; i < size(); i++) {
                profiles.add(get(i).toPrettyString());
            }
            return profiles;
        }

    }
}
