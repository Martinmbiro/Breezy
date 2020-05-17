package com.ahrefs.blizzard.ui;

import android.content.Intent;
import android.os.Bundle;

import com.ahrefs.blizzard.R;
import com.ahrefs.blizzard.databinding.ActivityEmptyWeatherBinding;
import com.ahrefs.blizzard.model.room.Weather;
import com.ahrefs.blizzard.viewmodel.WeatherViewModel;
import com.pd.chocobar.ChocoBar;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import static com.ahrefs.blizzard.viewmodel.WeatherViewModel.ONE_TIME_REQUEST_TAG;

public class EmptyWeather extends AppCompatActivity {
    ActivityEmptyWeatherBinding mBinding;
    WeatherViewModel mViewModel;
    WeatherViewModel.Factory mFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEmptyWeatherBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        /*Initiate ViewModel*/
        mFactory = new WeatherViewModel.Factory(this.getApplication());
        mViewModel = new ViewModelProvider(this, mFactory).get(WeatherViewModel.class);

        /*Refresh Weather First*/
        mViewModel.enqueueOneTimeRefresh();

        /*Then Observe Weather Data from DB*/
        mViewModel.getWeatherLiveData().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather != null) {
                    startActivity(new Intent(EmptyWeather.this, MainActivity.class));
                    finish();
                }
            }
        });

        /*Set Up refresh Container*/
        mBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*Check for Network Connection and respond appropriately*/
                if (!mViewModel.isNetworkAvailable()) {
                    ChocoBar.builder()
                            .setActivity(EmptyWeather.this)
                            .setIcon(R.drawable.ic_signal)
                            .setText("You might be offline")
                            .setDuration(ChocoBar.LENGTH_LONG)
                            .build()
                            .show();
                }
                mBinding.swipeContainer.setRefreshing(false);
            }
        });

        /*Observe States of Work and respond appropriately*/
        WorkManager.getInstance(EmptyWeather.this)
                .getWorkInfosByTagLiveData(ONE_TIME_REQUEST_TAG)
                .observe(this, new Observer<List<WorkInfo>>() {
                    @Override
                    public void onChanged(List<WorkInfo> workInfos) {
                /*if(workInfos!=null){
                    if(workInfos.get(0).getState() == WorkInfo.State.SUCCEEDED){
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                }*/
                        if (workInfos != null && workInfos.get(0).getState() == WorkInfo.State.FAILED && mViewModel.isNetworkAvailable()) {
                            ChocoBar.builder()
                                    .setActivity(EmptyWeather.this)
                                    .setIcon(R.drawable.ic_sync_error)
                                    .setText("Something went wrong")
                                    //.centerText()
                                    .setDuration(ChocoBar.LENGTH_LONG)
                                    .build()
                                    .show();
                        }
                    }
                });
    }
}
