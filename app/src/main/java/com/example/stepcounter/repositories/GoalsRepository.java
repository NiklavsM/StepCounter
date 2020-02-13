package com.example.stepcounter.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.stepcounter.database.AppDatabase;
import com.example.stepcounter.database.Goal;
import com.example.stepcounter.database.GoalDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GoalsRepository {
    private static GoalsRepository instance;
    private LiveData<List<Goal>> allGoals;
    private GoalDao goalDao;

    private GoalsRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        goalDao = database.goalDao();
        allGoals = goalDao.loadAllGoals();
    }

    public static GoalsRepository getInstance(Application application) {
        if (instance == null) {
            instance = new GoalsRepository(application);
        }
        return instance;
    }

    public void insertGoal(Goal goal) {
        new InsertGoalAsyncTask(goalDao).execute(goal);
    }

    public void updateGoal(Goal goal) {
        new UpdateGoalAsyncTask(goalDao).execute(goal);
    }

    public void deleteGoal(Goal goal) {
        new DeleteGoalAsyncTask(goalDao).execute(goal);
    }

    public Goal getActiveGoal() {
        try {
            return new GetActiveGoalAsyncTask(goalDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllGoals() {
        new DeleteAllGoalsAsyncTask(goalDao).execute();
    }

    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }


    public List<Goal> getAllGoalsStatic() {
        try {
            return new GetAllGoalsStaticAsyncTask(goalDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class InsertGoalAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDao goalDao;

        private InsertGoalAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.insertGoal(goals[0]);
            return null;
        }
    }

    private static class UpdateGoalAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDao goalDao;

        private UpdateGoalAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.updateGoal(goals[0]);
            return null;
        }
    }

    private static class DeleteGoalAsyncTask extends AsyncTask<Goal, Void, Void> {

        private GoalDao goalDao;

        private DeleteGoalAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Goal... goals) {
            goalDao.deleteGoal(goals[0]);
            return null;
        }
    }

    private static class DeleteAllGoalsAsyncTask extends AsyncTask<Void, Void, Void> {

        private GoalDao goalDao;

        private DeleteAllGoalsAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            goalDao.deleteAllGoals();
            return null;
        }
    }

    private static class GetActiveGoalAsyncTask extends AsyncTask<Void, Void, Goal> {

        private GoalDao goalDao;

        private GetActiveGoalAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected Goal doInBackground(Void... voids) {
            return goalDao.getActiveGoal();
        }
    }

    private static class GetAllGoalsStaticAsyncTask extends AsyncTask<Void, Void, List<Goal>> {

        private GoalDao goalDao;

        private GetAllGoalsStaticAsyncTask(GoalDao goalDao) {
            this.goalDao = goalDao;
        }

        @Override
        protected List<Goal> doInBackground(Void... voids) {
            return goalDao.loadAllGoalsStatic();
        }
    }

}
