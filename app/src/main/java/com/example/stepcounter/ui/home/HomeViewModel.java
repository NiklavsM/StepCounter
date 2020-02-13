package com.example.stepcounter.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private GoalsRepository goalsRepository;
    private LiveData<HistoryEntity> today;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        historyRepository = HistoryRepository.getInstance(application);
        goalsRepository = GoalsRepository.getInstance(application);

        today = historyRepository.getToday();
    }

    public LiveData<HistoryEntity> getToday() {
        return today;
    }

    public void setNewActiveGoal(Goal goal) {
        Goal oldGoal = goalsRepository.getActiveGoal();
        oldGoal.setActive(false);
        goalsRepository.updateGoal(oldGoal);
        Log.d("NEW GOAL", "NEW GOAL " + today.getValue().getGoalName());
        HistoryEntity history = today.getValue();
        history.setGoalName(goal.getName());
        history.setGoalSteps(goal.getSteps());
        goal.setActive(true);
        goalsRepository.updateGoal(goal);
        historyRepository.updateHistory(history);
    }

    public List<Goal> getGoals() {
        return goalsRepository.getAllGoalsStatic();
    }

    public void addToHistory(int steps) {
        HistoryEntity history = today.getValue();
        history.setStepsTaken(today.getValue().getStepsTaken() + steps);
        historyRepository.updateHistory(history);
    }
}