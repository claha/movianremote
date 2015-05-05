package com.claha.showtimeremote.core;

import android.content.Context;

import com.claha.showtimeremote.R;
import com.claha.showtimeremote.base.BaseSettings;

import java.util.ArrayList;
import java.util.List;

public class MovianRemoteSettings extends BaseSettings {

    public final String PORT = "42000";

    public MovianRemoteSettings(Context context) {
        super(context);
    }


    
    public boolean showURL() {
        return getBoolean(R.string.settings_developer_url_key);
    }

    public Profiles getProfiles() {
        Profiles profiles = new Profiles();
        for (String info : getStringList(R.string.settings_profiles_key)) {
            profiles.add(new Profile(info));
        }
        return profiles;
    }

    public void setProfiles(Profiles profiles) {
        putStringList(R.string.settings_profiles_key, profiles.toStringList());
    }

    public void addProfile(Profile profile) {
        Profiles profiles = getProfiles();
        profiles.add(profile);
        setProfiles(profiles);
    }

    public void deleteProfile(Profile profile) {
        Profiles profiles = getProfiles();
        Profile currentProfile = getCurrentProfile();

        if (profile.equals(currentProfile)) {
            int index = profiles.indexOf(currentProfile);

            if (index == 0 && profiles.size() > 1) {
                index = 1;
            } else if (index > 0) {
                index--;
            }

            if (index >= 0 && profiles.size() > 1) {
                setCurrentProfile(profiles.get(index));
            }
        }
        profiles.remove(profile);
        setProfiles(profiles);
    }

    public Profile getCurrentProfile() {
        Profiles profiles = getProfiles();
        return profiles.getByName(getString(R.string.settings_profiles_select_key));
    }

    public void setCurrentProfile(Profile profile) {
        putString(R.string.settings_profiles_select_key, profile.getName());
    }

    public static class Profile {

        private String name;
        private String ipAddress;

        public Profile(String name, String ipAddress) {
            this.name = name;
            this.ipAddress = ipAddress;
        }

        public Profile(String info) {
            this.name = info.split("_")[0];
            this.ipAddress = info.split("_")[1];
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIPAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String toPrettyString() {
            return name.toUpperCase() + " [" + ipAddress + "]";
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
