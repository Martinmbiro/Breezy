package com.ahrefs.blizzard.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.ahrefs.blizzard.R;

public class InfoActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mToolbar = findViewById(R.id.info_toolbar);

        /*Set up Toolbar*/
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
