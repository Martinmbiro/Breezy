package com.ahrefs.blizzard.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ahrefs.blizzard.Repository;
import com.ahrefs.blizzard.model.room.Weather;
import com.ahrefs.blizzard.workmanager.OneTimeWorker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    public static final String ONE_TIME_REQUEST_TAG = "com.ahrefs.blizzard_ONE-TIME-REFRESH-WORK-TAG";
    public static final String ONE_TIME_WORK_NAME = "com.ahrefs.blizzard_ONE-TIME-REFRESH-WORK-NAME";
    private OneTimeWorkRequest mOneTimeWorkRequest;
    private LiveData<Weather> mWeatherLiveData;
    private Repository mRepository;
    private Application mApplication;

    public MutableLiveData<String> mSummary = new MutableLiveData<>();
    public MutableLiveData<String> mHumidity = new MutableLiveData<>();
    public MutableLiveData<String> mTemperature = new MutableLiveData<>();
    public MutableLiveData<String> mUvIndex = new MutableLiveData<>();

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        this.mApplication = application;
        mRepository = new Repository(application);
        mWeatherLiveData = mRepository.getWeatherLiveData();
    }

    /*Get Weather LiveData object*/
    public LiveData<Weather> getWeatherLiveData() {
        return mWeatherLiveData;
    }

    /*Return network connectivity state*/
    public boolean isNetworkAvailable(){
        boolean isConnected = false;
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
                .addTag(ONE_TIME_REQUEST_TAG)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 30, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(mApplication.getApplicationContext())
                .enqueueUniqueWork(ONE_TIME_WORK_NAME, ExistingWorkPolicy.KEEP, mOneTimeWorkRequest);
    }

    /*Cancel AutoRefresh Periodic work*/
    public void cancelAutoRefresh(){
        WorkManager.getInstance(mApplication.getApplicationContext())
                .cancelAllWorkByTag(PERIODIC_REQUEST_TAG);
    }

    /*Sets new Values to the fields of type MutableLiveData*/
    public void updateFields(){
        mSummary.setValue(Objects.requireNonNull(mRepository.getWeatherLiveData().getValue()).getSummary());
        mHumidity.setValue(mRepository.getWeatherLiveData().getValue().getHumidity());
        mTemperature.setValue(mRepository.getWeatherLiveData().getValue().getTemperature());
        mUvIndex.setValue(String.valueOf(mRepository.getWeatherLiveData().getValue().getUvIndex()));
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
