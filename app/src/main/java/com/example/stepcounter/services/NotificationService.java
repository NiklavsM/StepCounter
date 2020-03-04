package com.example.stepcounter.services;

import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.R;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.HistoryRepository;
import com.example.stepcounter.utils.NotificationUtils;
import com.example.stepcounter.utils.Utils;

public class NotificationService extends LifecycleService {

    private boolean firstOpen = true;
    private boolean goalReachedNotified = false;
    private boolean goalHalfReachedNotified = false;
    private HistoryRepository historyRepository;
    private String currentGoalName;

    @Override
    public void onCreate() {
        super.onCreate();
        historyRepository = HistoryRepository.getInstance(getApplication());

        LiveData<HistoryEntity> today = historyRepository.getHistoryEntry(Utils.getTodayNoTime());
        today.observe(this, history -> {
            if (history != null) {
                notifyIfNeeded(history);
            }
        });
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
                NotificationUtils.goalNotification(getApplicationContext(), currentGoalName + getString(R.string.goal_completed), getString(R.string.well_done));
                goalReachedNotified = true;
            }
            if (goalCompleted >= 0.5 && goalCompleted < 1 && !goalHalfReachedNotified) {
                NotificationUtils.goalNotification(getApplicationContext(), getString(R.string.half_way_there), (int) (100 * goalCompleted) + getString(R.string.percentage_completed)
                );
                goalHalfReachedNotified = true;
            }
        }
    }
}
