package com.claha.showtimeremote.core;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.claha.showtimeremote.R;

public class ShowtimeNotification extends NotificationCompat.Builder {

    private final Context context;
    private final NotificationManager notificationManager;
    private final String message;
    private String url = null;

    public ShowtimeNotification(Context context, String message) {
        super(context);
        this.context = context;
        this.message = message;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        setSmallIcon(R.drawable.ic_launcher);
        setContentTitle(context.getResources().getString(R.string.app_name));
        setContentText(message);
        setAutoCancel(true);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void show() {
        if (url != null) {
            Intent resultIntent = new Intent(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(url));

            PendingIntent pending = PendingIntent.getActivity(context, 0, resultIntent, 0);
            setContentIntent(pending);
        }
        notificationManager.notify(message.hashCode(), build());
    }
}