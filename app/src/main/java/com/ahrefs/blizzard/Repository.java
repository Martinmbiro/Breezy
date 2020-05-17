package com.ahrefs.blizzard;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ahrefs.blizzard.model.retrofit.BreezyAPI;
import com.ahrefs.blizzard.model.retrofit.BreezyResponse;
import com.ahrefs.blizzard.model.retrofit.Currently;
import com.ahrefs.blizzard.model.retrofit.RetrofitClient;
import com.ahrefs.blizzard.model.room.Weather;
import com.ahrefs.blizzard.model.room.WeatherDao;
import com.ahrefs.blizzard.model.room.WeatherDb;
import com.ahrefs.blizzard.ui.NotificationService;

import java.io.IOException;

import androidx.lifecycle.LiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*A single Access Point for Method calls related to Data Operations*/
public class Repository {
    private WeatherDao mWeatherDao;
    private LiveData<Weather> mWeatherLiveData;
    private static final String TAG = "Repository";
    private boolean mWasSuccessful;

    /*Class Constructor*/
    public Repository(Application application) {
        mWeatherDao = WeatherDb.getInstance(application).weatherDao();
        mWeatherLiveData = mWeatherDao.getWeather();
    }

    /*Get Weather from Db*/
    public LiveData<Weather> getWeatherLiveData() {
        return mWeatherLiveData;
    }

    /*Responsible for Refreshing Weather in the Db
     * Makes request to obtain new weather,
     * Deletes old Weather and Updates table with new Weather
     * Network request done SYNCHRONOUSLY since the Worker runs on an alternate thread from main anyway,
     * And we need to wait for the boolean returned so as to return Result.success() or Result.failure() */
    public boolean refreshWeatherSync(final Boolean isPeriodic, final Context context) {
        BreezyAPI breezyAPI = RetrofitClient.getInstance().create(BreezyAPI.class);
        Call<BreezyResponse> mCall = breezyAPI.getResponse();
        try {
            Response<BreezyResponse> response = mCall.execute();
            if (response.isSuccessful() && (response.body() != null ? response.body().getCurrently().getSummary() : null) != null) {
                mWasSuccessful = true;
                Log.d(TAG, "refreshWeatherSync: mWasSuccessful = " + mWasSuccessful);
                /*Only if response is successful, Empty the Table*/
                deleteOldWeather();

                /* Get currently Object from Response*/
                Currently newCurrently = response.body().getCurrently();
                String newTemperature = Math.round((int) newCurrently.getTemperature()) + "ºC";
                double halfBakedHumidity = newCurrently.getHumidity() * 100;
                String newHumidity = Math.round((int) halfBakedHumidity) + "%";

                /*Create a new Weather Object hence:*/
                Weather latestWeather = new Weather(System.currentTimeMillis(), newCurrently.getSummary(),
                        newCurrently.getIcon(), newTemperature, newHumidity, newCurrently.getUvIndex());

                /*Insert this Weather into DB*/
                insertWeather(latestWeather);

                /*Finally, make a Notification if the Refresh Call was made by AutoRefreshWorker*/
                if (isPeriodic && mWasSuccessful) {
                    NotificationService.enqueueWork(context.getApplicationContext(), new Intent());
                    Log.d(TAG, "onResponse: Call was made by AutoRefreshWorker");
                }

            } else {
                mWasSuccessful = false;
                Log.d(TAG, "refreshWeatherSync: mWasSuccessful = " + mWasSuccessful);
                Log.d(TAG, "onResponse: MESSAGE: " + response.message());
                Log.d(TAG, "onResponse: CODE: " + response.code());
            }

        } catch (IOException e) {
            mWasSuccessful = false;
            Log.d(TAG, "refreshWeatherSync: mWasSuccessful = " + mWasSuccessful);
            e.printStackTrace();
            Log.d(TAG, "refreshWeatherSync: " + e.getMessage());
            Log.d(TAG, "refreshWeatherSync: " + e.getCause().getMessage());
        }
        Log.d(TAG, "refreshWeatherSync: mWasSuccessful = " + mWasSuccessful);
        return mWasSuccessful;
    }

    /*Insert Weather into DB (Using RxJava)*/
    private void insertWeather(Weather weather) {
        Observable<Weather> insertObservable = Observable.just(weather)
                .subscribeOn(Schedulers.io());

        Observer<Weather> insertObserver = new Observer<Weather>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                this.disposable = d;
            }

            @Override
            public void onNext(@NonNull Weather weather) {
                mWeatherDao.insertWeather(weather);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: insertObservable" + e.getMessage());
            }

            @Override
            public void onComplete() {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        };

        insertObservable.subscribe(insertObserver);
    }

    /*Delete Weather (Using RxJava)*/
    private void deleteOldWeather() {
        /*new DeleteAllAsync(mWeatherDao).execute();*/
        /*The string in this case is merely a placeholder since we cannot
         * pass Void or Object in Observers or Observables*/
        Observable<String> deleteObservable = Observable.just("place_holder")
                .subscribeOn(Schedulers.io());

        Observer<String> deleteObserver = new Observer<String>() {
            private Disposable mDisposable;
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                this.mDisposable = d;
            }

            @Override
            public void onNext(@NonNull String s) {
                mWeatherDao.deleteOldWeather();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "deleteOldWeather(), onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                if(!mDisposable.isDisposed()){
                    mDisposable.dispose();
                }
            }
        };

        deleteObservable.subscribe(deleteObserver);
    }

}
