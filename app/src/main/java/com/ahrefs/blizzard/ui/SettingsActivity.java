package com.ahrefs.blizzard.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ahrefs.blizzard.R;
import com.ahrefs.blizzard.databinding.ActivitySettingsBinding;
import com.ahrefs.blizzard.viewmodel.WeatherViewModel;
import com.ahrefs.blizzard.workmanager.EnqueuePeriodicService;

public class SettingsActivity extends AppCompatActivity {
    private WeatherViewModel mViewModel;
    private WeatherViewModel.Factory mFactory;
    private ActivitySettingsBinding mBinding;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        /*Initiate ViewModel*/
        mFactory = new WeatherViewModel.Factory(this.getApplication());
        mViewModel = new ViewModelProvider(this, mFactory).get(WeatherViewModel.class);

        /*Initiate SharePreferences*/
        mPrefs = getSharedPreferences(getString(R.string.shared_preferences),MODE_PRIVATE);
        mEditor  = mPrefs.edit();


        /*Set up Toolbar*/
        setSupportActionBar(mBinding.preferencesToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Set up Number Picker*/
        mBinding.numberPicker.setMinValue(15);
        mBinding.numberPicker.setMaxValue(60);

        /*Set Up views appropriately*/
        setUpViews();

        mBinding.switchRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                displaySyncStates(b);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveChanges();
    }

    private void saveChanges(){
        if(mBinding.switchRefresh.isChecked()){
            //Switch is on
            if(mPrefs.getBoolean(getString(R.string.wasSwitchOn),false)){
                /*If switch was previously on,
                * Check if the current value of number picker is the same as saved refreshInterval
                * If not, assign the current value of number picker as the new value of refreshInterval
                * Save those changes, and Enqueue AutoRefresh work with these new changes*/
                if(mBinding.numberPicker.getValue() != mPrefs.getInt(getString(R.string.refreshInterval),15)){
                    mEditor.putInt(getString(R.string.refreshInterval ), mBinding.numberPicker.getValue());
                    mEditor.putBoolean(getString(R.string.wasSwitchOn), true);
                    mEditor.apply();

                    EnqueuePeriodicService.enqueueWork(SettingsActivity.this, new Intent());
                    Toast.makeText(this, "Time Interval was changed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "saveChanges: Time Interval changed");
                    Log.d(TAG, "saveChanges: "+ mPrefs.getInt(getString(R.string.refreshInterval),15));
                }
            }else{
                /*Else, if the Switch was previously off,
                * Assign the refreshInterval value to the current value in the number picker
                * Save those changes and enqueue AutoRefresh work with the new changes*/
                mEditor.putInt(getString(R.string.refreshInterval), mBinding.numberPicker.getValue());
                mEditor.putBoolean(getString(R.string.wasSwitchOn), true);
                mEditor.apply();

                EnqueuePeriodicService.enqueueWork(SettingsActivity.this, new Intent());
                Log.d(TAG, "saveChanges: "+ mPrefs.getInt(getString(R.string.refreshInterval),15));
                Toast.makeText(this, "Settings Changed", Toast.LENGTH_SHORT).show();
            }

        }else{
            /*If the switch was previously on,
            * Cancel the AutoRefresh Work*/
            if(mPrefs.getBoolean(getString(R.string.wasSwitchOn), false)){
                mViewModel.cancelAutoRefresh();
                Toast.makeText(this, "Work Cancelled", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "saveChanges: Work Cancelled");
            }
            mEditor.putBoolean(getString(R.string.wasSwitchOn), false);
            mEditor.apply();
        }
    }

    private void setUpViews() {
        if(mPrefs.getBoolean(getString(R.string.wasSwitchOn), false)){
            mBinding.switchRefresh.setChecked(true);
            mBinding.refreshIcon.setImageResource(R.drawable.ic_sync);
            mBinding.refreshTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mBinding.numberPickerCard.setVisibility(View.VISIBLE);
            mBinding.numberPicker.setValue(mPrefs.getInt(getString(R.string.refreshInterval),15));
        }else{
            mBinding.switchRefresh.setChecked(false);
            mBinding.refreshIcon.setImageResource(R.drawable.ic_sync_disabled);
            mBinding.refreshTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            mBinding.numberPickerCard.setVisibility(View.INVISIBLE);
        }
    }

    private void displaySyncStates(boolean b) {
        if(b){
            mBinding.refreshIcon.setImageResource(R.drawable.ic_sync);
            mBinding.refreshTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mBinding.numberPickerCard.setVisibility(View.VISIBLE);
            mBinding.numberPicker.setValue(mPrefs.getInt(getString(R.string.refreshInterval),15));
        }else{
            mBinding.refreshIcon.setImageResource(R.drawable.ic_sync_disabled);
            mBinding.refreshTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            mBinding.numberPickerCard.setVisibility(View.INVISIBLE);
        }
    }
}
