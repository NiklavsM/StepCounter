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

    public void insertHistory(HistoryEntity history) {
        new InsertHistoryAsyncTask(historyDao).execute(history);
    }

    public void updateHistory(HistoryEntity history) {
        new UpdateHistoryAsyncTask(historyDao).execute(history);
    }

    public void deleteHistory(HistoryEntity history) {
        new DeleteHistoryAsyncTask(historyDao).execute(history);
    }

    public void deleteHistoryById(Integer id) {
        new DeleteHistoryByIdAsyncTask(historyDao).execute(id);
    }

    public void deleteAllHistory() {
        new DeleteAllHistoryAsyncTask(historyDao).execute();
    }

    public LiveData<HistoryEntity> getHistoryEntry(long date) {
        try {
            return new GetHistoryEntryAsyncTask(historyDao).execute(date).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HistoryEntity getHistoryEntryStatic(long date) {
        try {
            return new GetHistoryEntryStaticAsyncTask(historyDao).execute(date).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<HistoryEntity> getHistoryById(int id) {
        try {
            return new GetHistoryByIdAsyncTask(historyDao).execute(id).get();
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

    private static class DeleteHistoryByIdAsyncTask extends AsyncTask<Integer, Void, Void> {

        private HistoryDao historyDao;

        private DeleteHistoryByIdAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            historyDao.deleteHistoryById(integers[0]);
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

    private static class GetHistoryEntryAsyncTask extends AsyncTask<Long, Void, LiveData<HistoryEntity>> {

        private HistoryDao historyDao;

        private GetHistoryEntryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected LiveData<HistoryEntity> doInBackground(Long... longs) {
            return historyDao.getHistoryEntry(longs[0]);
        }
    }

    private static class GetHistoryEntryStaticAsyncTask extends AsyncTask<Long, Void, HistoryEntity> {

        private HistoryDao historyDao;

        private GetHistoryEntryStaticAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected HistoryEntity doInBackground(Long... longs) {
            return historyDao.getHistoryEntryStatic(longs[0]);
        }
    }

    private static class GetHistoryByIdAsyncTask extends AsyncTask<Integer, Void, LiveData<HistoryEntity>> {

        private HistoryDao historyDao;

        private GetHistoryByIdAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected LiveData<HistoryEntity> doInBackground(Integer... integers) {
            return historyDao.getDayById(integers[0]);
        }
    }
}
