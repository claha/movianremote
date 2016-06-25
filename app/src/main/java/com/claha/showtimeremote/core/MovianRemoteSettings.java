package com.claha.showtimeremote.core;

import android.content.Context;

import com.claha.showtimeremote.R;
import com.claha.showtimeremote.base.BaseSettings;

public class MovianRemoteSettings extends BaseSettings {

    public static final String PORT = "42000";

    public MovianRemoteSettings(Context context) {
        super(context);
    }

    public String getIPAddress() {
        return getString(R.string.settings_network_ipaddress_key);
    }

    public boolean showURL() {
        return getBoolean(R.string.settings_developer_url_key);
    }

    public String getRedProgrammableButtonOnClickAction() {
        return getString(R.string.settings_programmable_buttons_red_button_action_key);
    }

    public String getRedProgrammableButtonOnLongClickAction() {
        return getString(R.string.settings_programmable_buttons_red_button_long_action_key);
    }

    public String getGreenProgrammableButtonOnClickAction() {
        return getString(R.string.settings_programmable_buttons_green_button_action_key);
    }

    public String getGreenProgrammableButtonOnLongClickAction() {
        return getString(R.string.settings_programmable_buttons_green_button_long_action_key);
    }

    public String getBlueProgrammableButtonOnClickAction() {
        return getString(R.string.settings_programmable_buttons_blue_button_action_key);
    }

    public String getBlueProgrammableButtonOnLongClickAction() {
        return getString(R.string.settings_programmable_buttons_blue_button_long_action_key);
    }

}
