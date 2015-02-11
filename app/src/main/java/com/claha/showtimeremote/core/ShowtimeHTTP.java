package com.claha.showtimeremote.core;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ShowtimeHTTP {

    private final static String URL_BASE = "http://%s:%s/showtime/";
    private final static String URL_ACTION = URL_BASE + "input/action/%s";
    private final static String URL_SEARCH = URL_BASE + "open?url=search:%s";

    private final ShowtimeSettings showtimeSettings;

    private String ipAddress;
    private String port;

    public ShowtimeHTTP(Context context) {
        showtimeSettings = new ShowtimeSettings(context);
    }

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

    //TODO: Check return of http request, seems to return "Ok" when fine
    //TODO: Is threading the best way to do this? can't do it on main thread so something is needed
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
        port = showtimeSettings.PORT;
    }
}
