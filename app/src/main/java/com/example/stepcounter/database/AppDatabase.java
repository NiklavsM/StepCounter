package com.example.stepcounter.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Goal.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "step_counter";
    private static AppDatabase sInstance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            Log.d(LOG_TAG, "Creating the DB instance");
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, AppDatabase.DATABASE_NAME)
                    .allowMainThreadQueries()// TODO run on a separate thread
                    .addCallback(roomCallback)
                    .build();
        }

        Log.d(LOG_TAG, "Getting the DB instance");
        return sInstance;
    }

    public abstract GoalDao goalDao();

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(sInstance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private GoalDao goalDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            goalDao = db.goalDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            goalDao.insertGoal(new Goal("Goal 1", 3000));
            goalDao.insertGoal(new Goal("Goal 2", 9000));
            goalDao.insertGoal(new Goal("Goal 3", 12000));
            return null;
        }
    }
}
