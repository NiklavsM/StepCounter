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
            Goal defaultGoal = new Goal("Default goal", 2000, true);
            goalDao.insertGoal(defaultGoal);
            return null;
        }
    }
}
