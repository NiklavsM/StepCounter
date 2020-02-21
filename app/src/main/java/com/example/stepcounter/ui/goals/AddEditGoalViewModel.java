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

    public AddEditGoalViewModel(@NonNull Application application) {
        super(application);
        repository = GoalsRepository.getInstance(application);
    }

    public boolean addGoal(Goal goal) {
        Goal existingGoal = repository.getGoalByName(goal.getName());
        if (existingGoal != null) {
            return false;
        }
        repository.insertGoal(goal);
        return true;

    }

    public void deleteGoal(int id) {
        repository.deleteGoalWithId(id);
    }

    public boolean updateGoal(Goal goal) {
        Goal existingGoal = repository.getGoalByName(goal.getName());
        if (existingGoal == null || existingGoal.getId() == goal.getId()) {
            repository.updateGoal(goal);
            return true;
        }
        return false;
    }


    public Goal getGoalToEdit(int id) {
        return repository.getGoalById(id);
    }
}
