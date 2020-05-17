package com.ahrefs.blizzard.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ahrefs.blizzard.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private static final String REPO_URL = "https://github.com/Martinmbiro/Breezy";

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

    private void openProjectRepo() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(InfoActivity.REPO_URL)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_github:
                openProjectRepo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
