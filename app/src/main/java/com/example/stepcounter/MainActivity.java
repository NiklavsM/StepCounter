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
import com.example.stepcounter.services.NotificationService;
import com.example.stepcounter.services.RecordStepsService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startServices();
        setContentView(R.layout.activity_main);
        setBottomNavigation();

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
        if (item.getItemId() == R.id.settings_navvan) {
            navController.navigate(R.id.navigation_settings);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void startServices() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        startService(new Intent(this, AutoHistoryService.class));

        Intent autoRecordService = new Intent(this, RecordStepsService.class);
        Intent notificationService = new Intent(this, NotificationService.class);

        if (autoRecordEnabled()) {
            startService(autoRecordService);
        }
        if (notificationsEnabled()) {
            startService(notificationService);
        }
        listener = (preferences, key) -> {
            if (key.equals(getString(R.string.auto_recording))) {
                if (autoRecordEnabled()) {
                    startService(autoRecordService);
                } else {
                    stopService(autoRecordService);
                }
            }
            if (key.equals(getString(R.string.notifications_enabled))) {
                if (notificationsEnabled()) {
                    startService(notificationService);
                } else {
                    stopService(notificationService);
                }
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    private void setBottomNavigation() {
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

    private boolean autoRecordEnabled() {
        return !sharedPreferences.getBoolean(getString(R.string.auto_recording), false);
    }

    private boolean notificationsEnabled() {
        return sharedPreferences.getBoolean(getString(R.string.notifications_enabled), true);
    }

    private void setUpPreferencesListener(){

    }

}
