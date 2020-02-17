package com.example.stepcounter.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.HistoryRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository repository;
    private LiveData<List<HistoryEntity>> history;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = HistoryRepository.getInstance(application);
        history = repository.getAllHistory();
    }

    public LiveData<List<HistoryEntity>> getHistory() {
        return history;
    }

    public void deleteHistory(HistoryEntity history) {
        repository.deleteHistory(history);
    }

    public void addHistory(HistoryEntity historyEntity) {
        repository.insertHistory(historyEntity);
    }
}