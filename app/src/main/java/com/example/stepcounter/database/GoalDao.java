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

    @Insert
    void insertGoal(Goal goal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Query("DELETE FROM goal")
    void deleteAllGoals();
}
