package com.example.stepcounter;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.stepcounter.repositories.HistoryRepository;

public class MainViewModel extends AndroidViewModel {
    private HistoryRepository historyRepository;

    public MainViewModel(Application application) {
        super(application);
        historyRepository = HistoryRepository.getInstance(application);
    }
}
