package com.example.stepcounter.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.HistoryRepository;
import com.example.stepcounter.utils.Utils;

import static com.example.stepcounter.utils.NotificationUtils.getRunningInBackgroundNotification;

public class RecordStepsService extends Service implements SensorEventListener {

    int previousSteps = -1;
    SensorManager sensorManager;
    HistoryRepository historyRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        historyRepository = HistoryRepository.getInstance(getApplication());
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int eventSteps = (int) event.values[0];
        int toAdd = (eventSteps - previousSteps);

        if (previousSteps == -1) {
            previousSteps = eventSteps;
        } else {
            previousSteps = eventSteps;
            HistoryEntity today = historyRepository.getHistoryEntryStatic(Utils.getTodayNoTime());
            today.setStepsTaken(today.getStepsTaken() + toAdd);
            historyRepository.updateHistory(today);
        }
        Log.v("Step triggered ", "step triggered " + toAdd);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, getRunningInBackgroundNotification(this));
        return START_STICKY;
    }
}