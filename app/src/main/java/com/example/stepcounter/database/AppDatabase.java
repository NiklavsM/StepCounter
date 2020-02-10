package com.example.stepcounter.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Goal.class, HistoryEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "step_counter";
    private static AppDatabase sInstance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            Log.d(LOG_TAG, "Creating the DB instance");
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, AppDatabase.DATABASE_NAME)
                    .addCallback(roomCallback)
                    .build();
        }

        Log.d(LOG_TAG, "Getting the DB instance");
        return sInstance;
    }

    public abstract GoalDao goalDao();

    public abstract HistoryDao historyDao();

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(sInstance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private GoalDao goalDao;
        private HistoryDao historyDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            goalDao = db.goalDao();
            historyDao = db.historyDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            goalDao.insertGoal(new Goal("CDC's minimum", 2000));
            goalDao.insertGoal(new Goal("4 digits reached", 1000));
            goalDao.insertGoal(new Goal("Iron man", 20000));
            goalDao.insertGoal(new Goal("10 000 steps", 10000));

            historyDao.setHistory(new HistoryEntity(1580186339584f, 5435, "Iron man", 20000));
            historyDao.setHistory(new HistoryEntity(1570186339584f, 2345, "CDC's minimum", 2000));
            historyDao.setHistory(new HistoryEntity(1560186339584f, 950, "4 digits reached", 1000));
            return null;
        }
    }
}
