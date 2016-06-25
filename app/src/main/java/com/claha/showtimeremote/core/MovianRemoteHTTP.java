package com.claha.showtimeremote.core;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class that handles the communication with Movian.
 * This class is used to send actions and search queries to Movian.
 *
 * @author Claes Hallstrom
 * @version 1.3.0
 */
public class MovianRemoteHTTP {

    /**
     * Base URL used for communication.
     */
    private static final String URL_BASE = "http://%s:%s/api/";

    /**
     * URL used for sending actions.
     */
    private static final String URL_ACTION = URL_BASE + "input/action/%s";

    /**
     * URL used for sending search queries.
     */
    //private static final String URL_SEARCH = URL_BASE + "open?url=search:%s";

    /**
     * ShowtimeSettings instance to access current IP address and port.
     */
    private final MovianRemoteSettings settings;

    /**
     * Create a Movian HTTP object.
     *
     * @param context The context of the current activity.
     */
    public MovianRemoteHTTP(Context context) {
        settings = new MovianRemoteSettings(context);
    }

    /**
     * Send an action to Movian.
     *
     * @param action The action to be sent.
     */
    public void sendAction(String action) {
        if (action != null && !action.equals("")) {
            String url = String.format(URL_ACTION, settings.getIPAddress(), MovianRemoteSettings.PORT, action);
            sendURL(url);
        }
    }

    /**
     * Send a search query to Movian.
     *
     * @param query The query to be sent.
     */
    /*public void search(String query) {
        query = query.replace(" ", "+");
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(URL_SEARCH, settings.getIPAddress(), settings.getPort(), query);
        sendURL(url);
    }*/

    /**
     * Send an url to Movian.
     *
     * @param url The url to be sent.
     */
    private void sendURL(final String url) {
        if (settings.showURL()) {
            Toast.makeText(settings.getContext(), url, Toast.LENGTH_SHORT).show();
        }
        Thread thread = new Thread((new Runnable() {
            @Override
            public void run() {
                URLConnection connection;
                try {
                    connection = new URL(url).openConnection();
                    connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
        thread.start();
    }
}
