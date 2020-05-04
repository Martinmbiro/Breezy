package com.ahrefs.blizzard.workmanager;

import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

/*This class is responsible for enqueueing Periodic, automatic refresh with WorkManager*/
public class EnqueuePeriodicService extends JobIntentService {
    private static final int JOB_ID = 300;
    public static final String PERIODIC_REQUEST_TAG = "com.ahrefs.blizzard_periodic_request_tag";
    public static final String PERIODIC_WORK_TAG = "com.ahrefs.blizzard_periodic_refresh_work_tag";

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context, EnqueuePeriodicService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        /*Define Constraints*/
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        /*Create request*/
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(AutoRefreshWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(PERIODIC_REQUEST_TAG)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 45, TimeUnit.SECONDS)
                .build();

        /*Enqueue request*/
        WorkManager.getInstance(this.getApplicationContext())
                .enqueueUniquePeriodicWork(PERIODIC_WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
    }
}
