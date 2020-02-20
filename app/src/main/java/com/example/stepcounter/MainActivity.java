package com.example.stepcounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.stepcounter.services.AutoHistoryService;
import com.example.stepcounter.services.RecordStepsService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, AutoHistoryService.class));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent autoRecordService = new Intent(this, RecordStepsService.class);
        if (sharedPreferences.getBoolean(getString(R.string.auto_recording), false)) {
            Log.v("Started1", "started1");
            startService(autoRecordService);
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if (!sharedPreferences.getBoolean(getString(R.string.auto_recording), false)) {
                stopService(autoRecordService);
            } else {
                startService(autoRecordService);
            }
        });

        setContentView(R.layout.activity_main);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_goals, R.id.navigation_history)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.AddEditGoalFragment) {
                navView.setVisibility(View.GONE);
            } else {
                navView.setVisibility(View.VISIBLE);
            }
        });
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (item.getItemId()) {
            case R.id.settings_navvan:
                navController.navigate(R.id.navigation_settings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
