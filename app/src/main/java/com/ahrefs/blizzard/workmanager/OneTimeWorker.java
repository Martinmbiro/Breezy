package com.ahrefs.blizzard.workmanager;

import android.app.Application;
import android.content.Context;

import com.ahrefs.blizzard.Repository;
import com.ahrefs.blizzard.model.retrofit.BreezyAPI;
import com.ahrefs.blizzard.model.retrofit.BreezyResponse;
import com.ahrefs.blizzard.model.retrofit.RetrofitClient;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Response;

/*Worker Class responsible for Making OneTime refresh call*/
public class OneTimeWorker extends Worker {
    private Context mContext;
    private Repository mRepository;

    public OneTimeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mRepository = new Repository((Application) context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        if (mRepository.refreshWeatherSync(false, mContext)){
            return Result.success();
        }else{
            return Result.failure();
        }
    }
}
