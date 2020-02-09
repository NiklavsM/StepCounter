package com.example.stepcounter.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.AppDatabase;
import com.example.stepcounter.database.HistoryDao;
import com.example.stepcounter.database.HistoryEntity;
import com.example.stepcounter.utils.Utils;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class HistoryRepository {
    private static HistoryRepository instance;
    private LiveData<List<HistoryEntity>> allHistory;
    private HistoryDao historyDao;

    private HistoryRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        historyDao = database.historyDao();
        allHistory = historyDao.getHistory();
    }

    public static HistoryRepository getInstance(Application application) {
        if (instance == null) {
            instance = new HistoryRepository(application);
        }
        return instance;
    }

    public void insertHistory() {
       HistoryEntity history =  new HistoryEntity(Utils.getTodayNoTime(),0, "Default goal", 10000);
        new InsertHistoryAsyncTask(historyDao).execute(history);
    }

    public void updateHistory(HistoryEntity history) {
        new UpdateHistoryAsyncTask(historyDao).execute(history);
    }

    public void deleteHistory(HistoryEntity history) {
        new DeleteHistoryAsyncTask(historyDao).execute(history);
    }

    public void deleteAllHistory() {
        new DeleteAllHistoryAsyncTask(historyDao).execute();
    }

    public LiveData<HistoryEntity> getToday() {
        try {
            return new GetTodayHistoryAsyncTask(historyDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<HistoryEntity>> getAllHistory() {
        return allHistory;
    }

    private static class InsertHistoryAsyncTask extends AsyncTask<HistoryEntity, Void, Void> {

        private HistoryDao historyDao;

        private InsertHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(HistoryEntity... historys) {
            historyDao.setHistory(historys[0]);
            return null;
        }
    }

    private static class UpdateHistoryAsyncTask extends AsyncTask<HistoryEntity, Void, Void> {

        private HistoryDao historyDao;

        private UpdateHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(HistoryEntity... historys) {
            historyDao.updateHistory(historys[0]);
            return null;
        }
    }

    private static class DeleteHistoryAsyncTask extends AsyncTask<HistoryEntity, Void, Void> {

        private HistoryDao historyDao;

        private DeleteHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(HistoryEntity... historys) {
            historyDao.deleteHistory(historys[0]);
            return null;
        }
    }

    private static class DeleteAllHistoryAsyncTask extends AsyncTask<Void, Void, Void> {

        private HistoryDao historyDao;

        private DeleteAllHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            historyDao.deleteAllHistory();
            return null;
        }
    }

    private static class GetTodayHistoryAsyncTask extends AsyncTask<Void, Void, LiveData<HistoryEntity>> {

        private HistoryDao historyDao;

        private GetTodayHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected LiveData<HistoryEntity> doInBackground(Void... voids) {
            return historyDao.getToday(Utils.getTodayNoTime());
        }
    }

}
