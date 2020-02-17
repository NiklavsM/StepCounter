package com.example.stepcounter.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDao {
    @Query("SELECT * FROM goal ORDER BY steps")
    LiveData<List<Goal>> loadAllGoals();

    @Query("SELECT * FROM goal ORDER BY steps")
    List<Goal> loadAllGoalsStatic();

    @Insert
    void insertGoal(Goal goal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Query("DELETE FROM goal")
    void deleteAllGoals();

    @Query("DELETE FROM goal WHERE id = :id")
    void deleteGoalWithId(int id);

    @Query("SELECT * FROM goal WHERE active = 1")
    Goal getActiveGoal();

    @Query("SELECT * FROM goal WHERE id = :id")
    Goal getGoalById(int id);

    @Query("SELECT * FROM goal WHERE name = :name")
    Goal getGoalByName(String name);
}
