package com.example.stepcounter.services;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;
import com.example.stepcounter.utils.Utils;

public class AutoHistoryService extends IntentService {

    public AutoHistoryService() {
        super("AddNewDay");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Application app = getApplication();
        HistoryRepository historyRepository = HistoryRepository.getInstance(app);
        GoalsRepository goalsRepository = GoalsRepository.getInstance(app);

        LiveData<HistoryEntity> today = historyRepository.getToday();
        if (today.getValue() == null) {
            Goal activeGoal = goalsRepository.getActiveGoal();
            HistoryEntity newDay = new HistoryEntity(Utils.getTodayNoTime(), 0, activeGoal.getName(), activeGoal.getSteps());
            historyRepository.insertHistory(newDay);
        }
        stopSelf();

    }
}
