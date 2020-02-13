package com.example.stepcounter;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;

public class MainViewModel extends AndroidViewModel {
    private HistoryRepository historyRepository;
    private GoalsRepository goalsRepository;

    public MainViewModel(Application application) {
        super(application);
        historyRepository = HistoryRepository.getInstance(application);
        goalsRepository = GoalsRepository.getInstance(application);
    }

//    private void addToday() { //TODO maybe put somewhere else
//
//        if (today.getValue() != null) {
//            Log.d("History  !!!", String.valueOf(today.getValue().getStepsTaken()));
//        } else {
//            Log.d("History  !!!", "History not there");
//            historyRepository.insertHistory();
//        }
//    }
}
