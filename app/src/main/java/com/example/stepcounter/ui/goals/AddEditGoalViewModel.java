package com.example.stepcounter.ui.goals;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.repositories.StepCounterRepository;

import java.util.List;

public class AddEditGoalViewModel extends AndroidViewModel {

    private StepCounterRepository repository;
    private LiveData<List<Goal>> allGoals;

    public AddEditGoalViewModel(@NonNull Application application) {
        super(application);
        repository = StepCounterRepository.getInstance(application);
        allGoals = repository.getAllGoals();
    }

    public void addGoal(Goal goal) {
        repository.insertGoal(goal);
    }

    public void updateGoal(Goal goal) {
        repository.updateGoal(goal);
    }

    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }
}
