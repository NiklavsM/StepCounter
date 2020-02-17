package com.example.stepcounter.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;

import java.util.List;

public class AddEditHistoryViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private GoalsRepository goalsRepository;

    public AddEditHistoryViewModel(@NonNull Application application) {
        super(application);
        historyRepository = HistoryRepository.getInstance(application);
        goalsRepository = GoalsRepository.getInstance(application);
    }

    public void addHistory(HistoryEntity history) {
        historyRepository.insertHistory(history);
    }

    public void updateHistory() {

    }

    public Goal getActiveGoal() {
        return goalsRepository.getActiveGoal();
    }
    public Goal getGoalByName(String name) {
        return goalsRepository.getGoalByName(name);
    }


    public List<Goal> getGoals() {
        return goalsRepository.getAllGoalsStatic();
    }

    public void removeHistory(int id) {
        historyRepository.deleteHistoryById(id);
    }

    public HistoryEntity getHistory(int id) {
        return historyRepository.getHistoryById(id);
    }
}
