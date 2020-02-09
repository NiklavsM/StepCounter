package com.example.stepcounter.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;

public class HomeViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private GoalsRepository repository;
    private LiveData<HistoryEntity> today;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        historyRepository = HistoryRepository.getInstance(application);
        today = historyRepository.getToday();
        //addToday();
    }

    // Adds history if day already does not have an entry
    private void addToday() { //TODO maybe put somewhere else

        if (today.getValue() != null) {
            Log.d("History  !!!", String.valueOf(today.getValue().getStepsTaken()));
        } else {
            Log.d("History  !!!", "History not there");
            historyRepository.insertHistory();
        }
    }

    public LiveData<HistoryEntity> getToday() {
        return today;
    }

    public void addToHistory(int steps) {
        HistoryEntity history = today.getValue();
        history.setStepsTaken(today.getValue().getStepsTaken() + steps);
        historyRepository.updateHistory(history);
    }

    public void addNewDay() {
        historyRepository.insertHistory();
    }
}