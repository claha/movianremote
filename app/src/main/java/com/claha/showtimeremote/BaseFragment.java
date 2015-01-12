package com.claha.showtimeremote;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    private final static String IP_ADDRESS = "ipAddress";
    private final static String PORT = "port";

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return inflater.inflate(getLayoutResource(), container, false);
    }

    protected abstract int getLayoutResource();

    String getIPAddress() {
        return sharedPreferences.getString(IP_ADDRESS, null);
    }

    String getPort() {
        return sharedPreferences.getString(PORT, null);
    }
}

