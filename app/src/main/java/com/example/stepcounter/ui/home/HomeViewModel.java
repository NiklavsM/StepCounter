package com.example.stepcounter.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;

import java.util.List;

import static com.example.stepcounter.utils.Utils.getTodayNoTime;

public class HomeViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private GoalsRepository goalsRepository;
    private LiveData<HistoryEntity> dayToEdit;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        historyRepository = HistoryRepository.getInstance(application);
        goalsRepository = GoalsRepository.getInstance(application);
    }

    public LiveData<HistoryEntity> getDay(int historyId) {
        if (historyId < 0) {
            return dayToEdit = historyRepository.getHistoryEntry(getTodayNoTime());
        }
        return dayToEdit = historyRepository.getHistoryById(historyId);

    }

    public void setNewActiveGoal(Goal goal) {
        HistoryEntity day = dayToEdit.getValue();
        if (day.getDay() == getTodayNoTime()) {
            Goal oldGoal = goalsRepository.getActiveGoal();
            oldGoal.setActive(false);
            goalsRepository.updateGoal(oldGoal);
            goalsRepository.updateGoal(goal);
            goal.setActive(true);
        }
        day.setGoalName(goal.getName());
        day.setGoalSteps(goal.getSteps());
        historyRepository.updateHistory(day);
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

}