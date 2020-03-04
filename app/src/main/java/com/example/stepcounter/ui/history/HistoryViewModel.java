package com.example.stepcounter.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.repositories.GoalsRepository;
import com.example.stepcounter.repositories.HistoryRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;
    private GoalsRepository goalsRepository;
    private LiveData<List<HistoryEntity>> history;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        historyRepository = HistoryRepository.getInstance(application);
        history = historyRepository.getAllHistory();
        goalsRepository = GoalsRepository.getInstance(application);
    }

    public LiveData<List<HistoryEntity>> getHistory() {
        return history;
    }

    public void addHistory(HistoryEntity historyEntity) {
        historyRepository.insertHistory(historyEntity);
    }

    public HistoryEntity getHistoryByDate(long date) {
        return historyRepository.getHistoryEntryStatic(date);
    }

    public Goal getActiveGoal() {
        return goalsRepository.getActiveGoal();
    }
}