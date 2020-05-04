package com.ahrefs.blizzard.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ahrefs.blizzard.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/*Responsible for creating Notification Channels and Notifications*/
public class NotificationReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "com.ahrefs.blizzard_notification_channel_id";
    private static final int NOTIFICATION_ID = 2;
    private NotificationManagerCompat mManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new
                    NotificationChannel(NOTIFICATION_CHANNEL_ID, "Weather Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Responsible for displaying notifications on automatic weather refresh");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        mManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setPriority(Notification.PRIORITY_HIGH)
                //.setColor()
                .setSmallIcon(R.drawable.small_icon)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText("Weather Update")
                        .bigText("Tap to view updated weather"))
                .build();

        mManagerCompat.notify(NOTIFICATION_ID, notification);

    }

}
