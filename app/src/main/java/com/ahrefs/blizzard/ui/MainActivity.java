package com.ahrefs.blizzard.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ahrefs.blizzard.R;
import com.ahrefs.blizzard.viewmodel.WeatherViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.OneTimeWorkRequest;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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

    /*ºC*/

}
