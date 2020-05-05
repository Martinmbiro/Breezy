package com.ahrefs.blizzard.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ahrefs.blizzard.R;
import com.ahrefs.blizzard.databinding.ActivityMainBinding;
import com.ahrefs.blizzard.model.room.Weather;
import com.ahrefs.blizzard.viewmodel.WeatherViewModel;
import com.pd.chocobar.ChocoBar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import static com.ahrefs.blizzard.viewmodel.WeatherViewModel.ONE_TIME_REQUEST_TAG;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private WeatherViewModel mViewModel;
    private WeatherViewModel.Factory mFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Initialize ViewModel First*/
        mFactory = new WeatherViewModel.Factory(this.getApplication());
        mViewModel = new ViewModelProvider(this, mFactory).get(WeatherViewModel.class);

        //Get WeatherData
        mViewModel.enqueueOneTimeRefresh();

        /*Set DataBinding in motion*/
        mBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        /*Set up ToolBar*/
        setSupportActionBar(mBinding.mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*Observe Weather Data changes*/
        mViewModel.getWeatherLiveData().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather != null) {
                    mViewModel.updateFields();
                    updateRestOfViews(weather);
                }
            }
        });

        /*Set up the OnRefreshContainer*/
        mBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.enqueueOneTimeRefresh();
                /*Check for Network Connection and respond appropriately*/
                if (!mViewModel.isNetworkAvailable()) {
                    ChocoBar.builder()
                            .setActivity(MainActivity.this)
                            .setIcon(R.drawable.ic_signal)
                            .setText("You might be offline")
                            //.centerText()
                            .setDuration(ChocoBar.LENGTH_LONG)
                            .build()
                            .show();
                }
                mBinding.swipeContainer.setRefreshing(false);
            }
        });

        /*Observe States of Work and respond appropriately*/
        WorkManager.getInstance(MainActivity.this)
                .getWorkInfosByTagLiveData(ONE_TIME_REQUEST_TAG)
                .observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                if(workInfos!=null){
                    if(workInfos.get(0).getState() == WorkInfo.State.SUCCEEDED){
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    /*This Method Updates the TimeTextView and Weather Icon Image*/
    private void updateRestOfViews(Weather weather) {
        /*Update Weather Icon*/
        switch (weather.getIcon()) {
            case "clear-day":
                mBinding.weatherIcon.setImageResource(R.drawable.clear_day);
                break;
            case "clear-night":
                mBinding.weatherIcon.setImageResource(R.drawable.clear_night);
                break;
            case "rain":
                mBinding.weatherIcon.setImageResource(R.drawable.rain);
                break;
            case "snow":
                mBinding.weatherIcon.setImageResource(R.drawable.snow);
                break;
            case "sleet":
                mBinding.weatherIcon.setImageResource(R.drawable.sleet);
                break;
            case "wind":
                mBinding.weatherIcon.setImageResource(R.drawable.wind);
                break;
            case "fog":
                mBinding.weatherIcon.setImageResource(R.drawable.fog);
                break;
            case "cloudy":
                mBinding.weatherIcon.setImageResource(R.drawable.cloudy);
                break;
            case "partly-cloudy-day":
                mBinding.weatherIcon.setImageResource(R.drawable.partly_cloudy_day);
                break;
            case "partly-cloudy-night":
                mBinding.weatherIcon.setImageResource(R.drawable.partly_cloudy_night);
                break;
            default:
                mBinding.weatherIcon.setImageResource(R.drawable.sunny);
        }

        /*Update Time to display Yesterday, a certain date or a certain time today*/
        Calendar todayMidnight = Calendar.getInstance();
        todayMidnight.set(Calendar.HOUR_OF_DAY, 0);
        todayMidnight.set(Calendar.MINUTE, 0);
        todayMidnight.set(Calendar.SECOND, 0);

        Calendar timeOfRequest = Calendar.getInstance();
        timeOfRequest.setTimeInMillis(weather.getTime());

        if (todayMidnight.get(Calendar.DAY_OF_MONTH) == timeOfRequest.get(Calendar.DAY_OF_MONTH)) {
            String timeToday = DateFormat.getTimeInstance(DateFormat.SHORT).format(timeOfRequest.getTime());
            mBinding.timeTextView.setText("At " + timeToday);
        } else if ((todayMidnight.get(Calendar.DAY_OF_MONTH) - timeOfRequest.get(Calendar.DAY_OF_MONTH)) == 1) {
            mBinding.timeTextView.setText("Yesterday");
        } else {
            String simpleDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(timeOfRequest.getTime());
            mBinding.timeTextView.setText("On " + simpleDate);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_info:
                //TODO: Insert Logic later
                Toast.makeText(this, "Start info Activity", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_preferences:
                //TODO: Insert Logic later
                Toast.makeText(this, "Start Preferences Activity", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
