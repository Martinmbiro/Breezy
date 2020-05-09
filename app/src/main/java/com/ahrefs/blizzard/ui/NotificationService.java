package com.ahrefs.blizzard.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.ahrefs.blizzard.R;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class NotificationService extends JobIntentService {
    private static final String TAG = "NotificationService";
    private static final int JOB_ID = 1000;
    public static final String NOTIFICATION_CHANNEL_ID = "com.ahrefs.blizzard_notification_channel_id";
    private static final int PENDING_INTENT_REQUEST_CODE = 619;
    private static final int NOTIFICATION_ID = 404;

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, NotificationService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork: NotificationReceiver Called");
        createNotificationChannel();
        fireNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Job Destroyed");
    }

    private void createNotificationChannel() {
        /*Create Notification Channel first*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel weatherNotificationChannel = new
                    NotificationChannel(NOTIFICATION_CHANNEL_ID, "Weather Notifications", NotificationManager.IMPORTANCE_HIGH);

            weatherNotificationChannel.setDescription("Responsible for displaying notifications on background weather refresh");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(weatherNotificationChannel);
        }
    }

    private void fireNotification() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        /*To open MainActivity when the Notification is tapped:*/
        Intent nextIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(nextIntent);
        PendingIntent contentIntent = stackBuilder.getPendingIntent(PENDING_INTENT_REQUEST_CODE, 0);

        /*Set up Notification*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getResources().getColor(R.color.colorNotification))
                .setSmallIcon(R.drawable.sunny)
                .setAutoCancel(true)
                .setContentTitle("Weather Data Updated")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText("Update")
                        .bigText("Tap to view updated weather"))
                .setContentIntent(contentIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        /*Fire Notification*/
        managerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
