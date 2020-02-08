package com.example.stepcounter.ui.goals;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.repositories.StepCounterRepository;

import java.util.List;

public class GoalsViewModel extends AndroidViewModel {

    private StepCounterRepository repository;
    private LiveData<List<Goal>> goals;

    public GoalsViewModel(@NonNull Application application) {
        super(application);
        repository = StepCounterRepository.getInstance(application);
        goals = repository.getAllGoals();
    }


    public LiveData<List<Goal>> getGoals() {
        return goals;
    }
}