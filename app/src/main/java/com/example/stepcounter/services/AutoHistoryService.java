package com.example.stepcounter.services;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.R;
import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;
import com.example.stepcounter.utils.NotificationUtils;
import com.example.stepcounter.utils.Utils;

public class AutoHistoryService extends LifecycleService {

    private boolean firstOpen = true;
    private boolean goalReachedNotified = false;
    private boolean goalHalfReachedNotified = false;
    private HistoryRepository historyRepository;
    private GoalsRepository goalsRepository;
    private String currentGoalName;

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Application app = getApplication();
        historyRepository = HistoryRepository.getInstance(app);
        goalsRepository = GoalsRepository.getInstance(app);

        LiveData<HistoryEntity> today = historyRepository.getHistoryEntry(Utils.getTodayNoTime());
        today.observe(this, history -> {
            if (history == null) {
                createNewHistoryEntry();
            } else {
                notifyIfNeeded(history);
            }
        });
        super.onStart(intent, startId);
    }

    private void notifyIfNeeded(HistoryEntity history) {
        float goalCompleted = (float) history.getStepsTaken() / history.getGoalSteps();
        if (firstOpen) {
            goalHalfReachedNotified = goalCompleted > 0.5;
            goalReachedNotified = goalCompleted > 1;
            firstOpen = false;
            currentGoalName = history.getGoalName();
        } else {
            if (currentGoalName != null) {
                if (!currentGoalName.equals(history.getGoalName())) {
                    goalReachedNotified = false;
                    goalHalfReachedNotified = false;
                    currentGoalName = history.getGoalName();
                }
            }
            if (goalCompleted >= 1 && !goalReachedNotified) {
                NotificationUtils.goalNotification(getApplicationContext(), getApplicationContext()
                        .getString(R.string.goal_reached_notification_title) + currentGoalName);
                goalReachedNotified = true;
            }
            if (goalCompleted >= 0.5 && goalCompleted < 1 && !goalHalfReachedNotified) {
                NotificationUtils.goalNotification(getApplicationContext(), getApplicationContext()
                        .getString(R.string.goal_half_reached_notification_title) + currentGoalName);
                goalHalfReachedNotified = true;
            }
        }
    }

    private void createNewHistoryEntry() {
        Goal activeGoal = goalsRepository.getActiveGoal();
        HistoryEntity newDay = new HistoryEntity(Utils.getTodayNoTime(), 0, activeGoal);
        historyRepository.insertHistory(newDay);
        currentGoalName = activeGoal.getName();
    }
}
