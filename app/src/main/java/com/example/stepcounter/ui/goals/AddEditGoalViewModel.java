package com.example.stepcounter.ui.goals;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.repositories.GoalsRepository;

import java.util.List;

public class AddEditGoalViewModel extends AndroidViewModel {

    private GoalsRepository repository;
    private LiveData<List<Goal>> allGoals;

    public AddEditGoalViewModel(@NonNull Application application) {
        super(application);
        repository = GoalsRepository.getInstance(application);
        allGoals = repository.getAllGoals();
    }

    public void addGoal(Goal goal) {
        repository.insertGoal(goal);
    }

    public void deleteGoal(int id) {
        repository.deleteGoalWithId(id);
    }

    public void updateGoal(Goal goal) {
        repository.updateGoal(goal);
    }

    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }
}
