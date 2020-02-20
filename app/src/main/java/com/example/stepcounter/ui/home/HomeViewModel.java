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
import com.example.stepcounter.utils.Utils;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private GoalsRepository goalsRepository;
    private LiveData<HistoryEntity> dayToEdit;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        historyRepository = HistoryRepository.getInstance(application);
        goalsRepository = GoalsRepository.getInstance(application);
    }

    public LiveData<HistoryEntity> getDayToEdit() {
        return dayToEdit;
    }

    public void setNewActiveGoal(Goal goal) {
        Goal oldGoal = goalsRepository.getActiveGoal();
        oldGoal.setActive(false);
        goalsRepository.updateGoal(oldGoal);
        HistoryEntity history = dayToEdit.getValue();
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
        HistoryEntity history = dayToEdit.getValue();
        history.setStepsTaken(dayToEdit.getValue().getStepsTaken() + steps);
        historyRepository.updateHistory(history);
    }

    public void removeHistory(int id) {
        historyRepository.deleteHistoryById(id);
    }

    public void setHistory(int historyId) {
        if (historyId < 0) {
            dayToEdit = historyRepository.getHistoryEntry(Utils.getTodayNoTime());
        } else {
            dayToEdit = historyRepository.getHistoryById(historyId);
        }
    }
}