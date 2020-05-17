package com.ahrefs.blizzard.ui;

import android.content.Intent;
import android.os.Bundle;

import com.ahrefs.blizzard.model.room.Weather;
import com.ahrefs.blizzard.viewmodel.WeatherViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class SplashScreen extends AppCompatActivity {
    WeatherViewModel mViewModel;
    WeatherViewModel.Factory mFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Initiate ViewModel*/
        mFactory = new WeatherViewModel.Factory(this.getApplication());
        mViewModel = new ViewModelProvider(this, mFactory).get(WeatherViewModel.class);

        /*Check if Weather is null, and respond appropriately*/
        mViewModel.getWeatherLiveData().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if(weather == null){
                    startActivity(new Intent(SplashScreen.this, EmptyWeather.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}
