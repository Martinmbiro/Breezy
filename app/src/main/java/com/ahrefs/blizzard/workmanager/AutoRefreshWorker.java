package com.ahrefs.blizzard.workmanager;

import android.app.Application;
import android.content.Context;

import com.ahrefs.blizzard.Repository;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/*Worker class responsible for Periodic AutoRefresh calls*/
public class AutoRefreshWorker extends Worker {
    private Repository mRepository;
    private Context mContext;

    public AutoRefreshWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        mRepository = new Repository((Application) context);
    }

    @NonNull
    @Override
    public Result doWork() {
        mRepository.refreshWeather(true, mContext);
        return Result.success();
    }
}
