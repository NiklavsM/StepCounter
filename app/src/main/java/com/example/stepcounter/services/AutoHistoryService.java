package com.example.stepcounter.services;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;
import com.example.stepcounter.utils.Utils;

public class AutoHistoryService extends LifecycleService {

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Application app = getApplication();
        HistoryRepository historyRepository = HistoryRepository.getInstance(app);
        GoalsRepository goalsRepository = GoalsRepository.getInstance(app);

        LiveData<HistoryEntity> today = historyRepository.getHistoryEntry(Utils.getTodayNoTime());
        today.observe(this, v -> {
            if (v == null) {
                Goal activeGoal = goalsRepository.getActiveGoal();
                HistoryEntity newDay = new HistoryEntity(Utils.getTodayNoTime(), 0, activeGoal);
                historyRepository.insertHistory(newDay);
            }
        });
        super.onStart(intent, startId);
    }
}
