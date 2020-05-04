package com.ahrefs.blizzard.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ahrefs.blizzard.Repository;
import com.ahrefs.blizzard.model.room.Weather;
import com.ahrefs.blizzard.ui.MainActivity;
import com.ahrefs.blizzard.workmanager.OneTimeWorker;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import static com.ahrefs.blizzard.workmanager.EnqueuePeriodicService.PERIODIC_REQUEST_TAG;


public class WeatherViewModel extends AndroidViewModel {
    private static final String ONE_TIME_WORK_TAG = "com.ahrefs.blizzard_ONE-TIME-REFRESH-WORK-TAG";
    private OneTimeWorkRequest mOneTimeWorkRequest;
    private LiveData<Weather> mWeatherLiveData;
    private Repository mRepository;
    private Application mApplication;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        this.mApplication = application;
        mRepository = new Repository(application);
        mWeatherLiveData = mRepository.getWeatherLiveData();
    }

    /*Get Weather Livedata weather object*/
    public LiveData<Weather> getWeatherLiveData() {
        return mWeatherLiveData;
    }

    /*Return network connectivity state*/
    public Boolean isNetworkAvailable(){
        Boolean isConnected = false;
        /*TODO Refactor to accommodate Android 9 and above as this method is deprecated on higher API levels*/
        ConnectivityManager manager = (ConnectivityManager) mApplication.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //Get an instance of Network Info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            isConnected = true;
        }
        return isConnected;
    }

    /*Enqueue OneTimeRefresh work*/
    public void enqueueOneTimeRefresh() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        mOneTimeWorkRequest = new OneTimeWorkRequest.Builder(OneTimeWorker.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 3, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(mApplication.getApplicationContext())
                .enqueueUniqueWork(ONE_TIME_WORK_TAG, ExistingWorkPolicy.KEEP, mOneTimeWorkRequest);
    }

    /*Cancel AutoRefresh Periodic work*/
    public void cancelAutoRefresh(){
        WorkManager.getInstance(mApplication.getApplicationContext())
                .cancelAllWorkByTag(PERIODIC_REQUEST_TAG);
    }



    /*Factory Class for Instantiating the WeatherViewModel class without a constructor*/
    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private Application mApplication;

        public Factory(Application application) {
            this.mApplication = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new WeatherViewModel(mApplication);
        }
    }

}
