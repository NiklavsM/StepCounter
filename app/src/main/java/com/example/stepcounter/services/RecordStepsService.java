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

public class RecordStepsService extends Service {

    HistoryRepository historyRepository = HistoryRepository.getInstance(getApplication());
    double magnitudePrevious = 0;
    SensorManager sensorManager;
    SensorEventListener stepDetector;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (sensor != null) {
                    float x_accel = event.values[0];
                    float y_accel = event.values[1];
                    float z_accel = event.values[2];

                    double magnitude = Math.sqrt(x_accel * x_accel + y_accel * y_accel + z_accel * z_accel);
                    double magnitudeDelta = magnitude - magnitudePrevious;
                    magnitudePrevious = magnitude;
                    if (magnitudeDelta > 7) {
                        HistoryEntity today = historyRepository.getTodayStatic();
                        today.setStepsTaken(today.getStepsTaken() + 1);
                        historyRepository.updateHistory(today);
                        Log.v("Step triggered ", "step triggered");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(stepDetector);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}