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

    private HistoryRepository historyRepository;
    private GoalsRepository goalsRepository;

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Application app = getApplication();
        historyRepository = HistoryRepository.getInstance(app);
        goalsRepository = GoalsRepository.getInstance(app);

        LiveData<HistoryEntity> today = historyRepository.getHistoryEntry(Utils.getTodayNoTime());
        today.observe(this, history -> {
            if (history == null) {
                createNewHistoryEntry();
            }
        });
        super.onStart(intent, startId);
    }


    private void createNewHistoryEntry() {
        Goal activeGoal = goalsRepository.getActiveGoal();
        HistoryEntity newDay = new HistoryEntity(Utils.getTodayNoTime(), 0, activeGoal);
        historyRepository.insertHistory(newDay);
    }
}
