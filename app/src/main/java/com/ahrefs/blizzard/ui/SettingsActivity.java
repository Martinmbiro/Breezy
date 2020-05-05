package com.ahrefs.blizzard.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.ahrefs.blizzard.R;
import com.ahrefs.blizzard.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        /*Set up Toolbar*/
        setSupportActionBar(mBinding.preferencesToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Set up Number Picker*/
        mBinding.numberPicker.setMinValue(15);
        mBinding.numberPicker.setMaxValue(60);

        mBinding.switchRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                displaySyncStates(b);
            }
        });

    }

    private void displaySyncStates(boolean b) {
        if(b){
            mBinding.refreshIcon.setImageResource(R.drawable.ic_sync);
            mBinding.refreshTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            mBinding.numberPickerCard.setVisibility(View.VISIBLE);
        }else{
            mBinding.refreshIcon.setImageResource(R.drawable.ic_sync_disabled);
            mBinding.refreshTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            mBinding.numberPickerCard.setVisibility(View.INVISIBLE);
            //mBinding.numberPicker.setValue(mBinding.numberPicker.getMinValue());
            //mBinding.numberPicker.setEnabled(false);
        }
    }
}
