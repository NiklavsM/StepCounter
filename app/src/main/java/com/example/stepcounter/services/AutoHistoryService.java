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
import com.example.stepcounter.utils.NotificationUtils;
import com.example.stepcounter.utils.Utils;

public class AutoHistoryService extends LifecycleService {

    private boolean goalReachedNotified = false;
    private boolean goalHalfReachedNotified = false;

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
            } else {
                float goalCompleted = (float) v.getStepsTaken() / v.getGoalSteps();
                if (goalCompleted >= 1 && !goalReachedNotified) {
                    NotificationUtils.goalAccomplished(getApplicationContext());
                    goalReachedNotified = true;
                }
                if (goalCompleted >= 0.5 && goalCompleted < 1 && !goalHalfReachedNotified) {
                    NotificationUtils.goalHalfAccomplished(getApplicationContext());
                    goalHalfReachedNotified = true;
                }
            }
        });
        super.onStart(intent, startId);
    }
}
