package com.ahrefs.blizzard.workmanager;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ahrefs.blizzard.Repository;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*Worker Class responsible for Making OneTime refresh call*/
public class OneTimeWorker extends Worker {
    private Context mContext;
    private Repository mRepository;
    private static final String TAG = "OneTimeWorker";
    private Boolean mIsSuccessful;

    public OneTimeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mRepository = new Repository((Application) context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        mRepository.refreshWeather(false, mContext);
        return Result.success();
    }
}
