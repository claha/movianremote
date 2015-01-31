package com.claha.showtimeremote.core;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ShowtimeHTTP {

    // Constants
    /*public static final String ACTION_UP = "Up";
    public static final String ACTION_DOWN = "Down";
    public static final String ACTION_LEFT = "Left";
    public static final String ACTION_RIGHT = "Right";
    public static final String ACTION_ACTIVATE = "Activate";
    public static final String ACTION_ENTER = "Enter";
    public static final String ACTION_SUBMIT = "Submit";
    public static final String ACTION_OK = "Ok";
    public static final String ACTION_CANCEL = "Cancel";
    public static final String ACTION_BS = "Backspace";
    public static final String ACTION_DELETE = "Delete";

    public static final String ACTION_MOVE_UP = "MoveUp";
    public static final String ACTION_MOVE_DOWN = "MoveDown";
    public static final String ACTION_MOVE_LEFT = "MoveLeft";
    public static final String ACTION_MOVE_RIGHT = "MoveRight";

    public static final String ACTION_NAV_FWD = "Forward";
    public static final String ACTION_NAV_BACK = "Back";

    public static final String ACTION_FOCUS_NEXT = "FocusNext";
    public static final String ACTION_FOCUS_PREV = "FocusPrev";

    public static final String ACTION_PAGE_UP = "PageUp";
    public static final String ACTION_PAGE_DOWN = "PageDown";

    public static final String ACTION_TOP = "Top";
    public static final String ACTION_BOTTOM = "Bottom";

    public static final String ACTION_INCR = "Increase";
    public static final String ACTION_DECR = "Decrease";

    public static final String ACTION_STOP = "Stop";
    public static final String ACTION_PLAYPAUSE = "PlayPause";
    public static final String ACTION_PLAY = "Play";
    public static final String ACTION_PAUSE = "Pause";
    public static final String ACTION_EJECT = "Eject";
    public static final String ACTION_RECORD = "Record";

    public static final String ACTION_SKIP_BACKWARD = "PreviousTrack";
    public static final String ACTION_SKIP_FORWARD = "NextTrack";
    public static final String ACTION_SEEK_FORWARD = "SeekForward";
    public static final String ACTION_SEEK_BACKWARD = "SeekReverse";

    public static final String ACTION_VOLUME_UP = "VolumeUp";
    public static final String ACTION_VOLUME_DOWN = "VolumeDown";
    public static final String ACTION_VOLUME_MUTE_TOGGLE = "VolumeMuteToggle";

    public static final String ACTION_MENU = "Menu";
    public static final String ACTION_ITEMMENU = "ItemMenu";
    public static final String ACTION_LOGWINDOW = "LogWindow";
    public static final String ACTION_SELECT = "Select";
    public static final String ACTION_SHOW_MEDIA_STATS = "MediaStats";
    public static final String ACTION_HOME = "Home";

    public static final String ACTION_SWITCH_VIEW = "ChangeView";
    public static final String ACTION_FULLSCREEN_TOGGLE = "FullscreenToggle";

    public static final String ACTION_NEXT_CHANNEL = "Channel+";
    public static final String ACTION_PREV_CHANNEL = "Channel-";

    public static final String ACTION_ZOOM_UI_INCR = "ZoomUI+";
    public static final String ACTION_ZOOM_UI_DECR = "ZoomUI-";
    public static final String ACTION_ZOOM_UI_RESET = "ZoomUIReset";
    public static final String ACTION_RELOAD_UI = "ReloadUI";

    public static final String ACTION_QUIT = "Quit";
    public static final String ACTION_STANDBY = "Standby";
    public static final String ACTION_POWER_OFF = "PowerOff";

    public static final String ACTION_SHUFFLE = "Shuffle";
    public static final String ACTION_REPEAT = "Repeat";

    public static final String ACTION_ENABLE_SCREENSAVER = "EnableScreenSaver";

    public static final String ACTION_CYCLE_AUDIO = "AudioTrack";
    public static final String ACTION_CYCLE_SUBTITLE = "SubtitleTrack";

    public static final String ACTION_RELOAD_DATA = "ReloadData";
    public static final String ACTION_PLAYQUEUE = "Playqueue";
    public static final String ACTION_SYSINFO = "Sysinfo";

    public static final String ACTION_SWITCH_UI = "SwitchUI";*/

    private final static String URL_BASE = "http://%s:%s/showtime/";
    private final static String URL_ACTION = URL_BASE + "input/action/%s";
    private final static String URL_SEARCH = URL_BASE + "open?url=search:%s";

    private final ShowtimeSettings showtimeSettings;

    private String ipAddress;
    private String port;

    public ShowtimeHTTP(Context context) {
        showtimeSettings = new ShowtimeSettings(context);
    }

    //TODO: Check return of http request, seems to return "Ok" when fine
    //TODO: Is threading the best way to do this? can't do it on mainthread so something is needed
    public void sendAction(String action) {
        updateSettings();
        if (action != null && !action.equals("")) {
            String url = String.format(URL_ACTION, ipAddress, port, action);
            sendURL(url);
        }
    }

    public void search(String text) {
        updateSettings();
        text = text.replace(" ", "+");
        String url = String.format(URL_SEARCH, ipAddress, port, text);
        sendURL(url);
    }

    private void sendURL(final String urlString) {
        Log.d("ShowtimeDebug", urlString);
        Thread thread = new Thread((new Runnable() {
            @Override
            public void run() {
                URL url;
                URLConnection connection;
                try {
                    url = new URL(urlString);
                    connection = url.openConnection();
                    connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
        thread.start();
    }

    private void updateSettings() {
        ipAddress = showtimeSettings.getIPAddress();
        port = showtimeSettings.getPort();
    }
}
