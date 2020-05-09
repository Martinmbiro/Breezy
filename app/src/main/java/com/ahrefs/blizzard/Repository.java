package com.ahrefs.blizzard;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.ahrefs.blizzard.model.retrofit.BreezyAPI;
import com.ahrefs.blizzard.model.retrofit.BreezyResponse;
import com.ahrefs.blizzard.model.retrofit.Currently;
import com.ahrefs.blizzard.model.retrofit.RetrofitClient;
import com.ahrefs.blizzard.model.room.Weather;
import com.ahrefs.blizzard.model.room.WeatherDao;
import com.ahrefs.blizzard.model.room.WeatherDb;
import com.ahrefs.blizzard.ui.NotificationService;

import androidx.lifecycle.LiveData;
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
    //private Boolean mResponse;
    private WeatherDao mWeatherDao;
    private LiveData<Weather> mWeatherLiveData;
    private static final String TAG = "Repository";

    /*Class Constructor*/
    public Repository(Application application) {
        WeatherDb db = WeatherDb.getInstance(application);
        mWeatherDao = db.weatherDao();
        mWeatherLiveData = mWeatherDao.getWeather();
    }

    /*Get Weather from Db*/
    public LiveData<Weather> getWeatherLiveData() {
        return mWeatherLiveData;
    }

    /*Insert Weather into DB*/
    public void insertWeather(Weather weather) {
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

    /*Delete Weather*/
    public void deleteOldWeather() {
        new DeleteAllAsync(mWeatherDao).execute();
    }

    /*Responsible for Refreshing Weather in the Db
     * Makes request to obtain new weather,
     * Deletes old Weather and Updates table with new Weather*/
    public void refreshWeather(final Boolean isPeriodic, final Context context) {
        BreezyAPI breezyAPI = RetrofitClient.getInstance().create(BreezyAPI.class);
        Call<BreezyResponse> call = breezyAPI.getResponse();
        call.enqueue(new Callback<BreezyResponse>() {
            @Override
            public void onResponse(Call<BreezyResponse> call, Response<BreezyResponse> response) {
                if (response.isSuccessful() && response.body().getCurrently().getSummary() != null) {
                    /*Only if response is successful, Empty the Table*/
                    deleteOldWeather();

                    /* Get currently Object from Response*/
                    Currently newCurrently = response.body().getCurrently();
                    String newTemperature = Math.round((int) newCurrently.getTemperature()) + "ºC";
                    double halfBakedHumidity = newCurrently.getHumidity() * 100;
                    String newHumidity = Math.round((int) halfBakedHumidity) + "%";

                    /*Create a new Weather Object from this*/
                    Weather latestWeather = new 
                    Weather(System.currentTimeMillis(), 
                        newCurrently.getSummary(), 
                        newCurrently.getIcon(),
                            //newCurrently.getTemperature() + "ºC",
                            //newCurrently.getHumidity()*100 +"%",
                            newTemperature,
                            newHumidity,
                            newCurrently.getUvIndex());

                    /*Insert this weather into DB*/
                    insertWeather(latestWeather);

                    /*Finally, make a Notification if the Refresh Call was made by AutoRefreshWorker*/
                    if (isPeriodic) {
                        /*Make call for creating Notification*/
                        /*Intent intent = new Intent(context.getApplicationContext(), NotificationReceiver.class);
                        context.getApplicationContext().sendBroadcast(intent);*/
                        NotificationService.enqueueWork(context.getApplicationContext(), new Intent());
                        Log.d(TAG, "onResponse: Call was made by AutoRefreshWorker");
                    }

                } else {
                    Log.d(TAG, "onResponse: MESSAGE: " + response.message());
                    Log.d(TAG, "onResponse: CODE: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BreezyResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private WeatherDao weatherDao;

        /*Constructor for Class*/
        DeleteAllAsync(WeatherDao weatherDao) {
            this.weatherDao = weatherDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            weatherDao.deleteOldWeather();
            return null;
        }
    }
}
